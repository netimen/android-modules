/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 * <p/>
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package com.netimen.androidmodules.helpers;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import org.androidannotations.helper.AnnotationHelper;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EBeanHolder;
import org.androidannotations.holder.EComponentHolder;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._null;
import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JExpr.lit;
import static com.sun.codemodel.JExpr.newArray;

public class ModuleCodeGenerator {

//    static public void returnNewInstance(EComponentHolder holder, JMethod method, JDefinedClass instanceCls, JInvocation newInstance) {
//        method.body()._return(getInstance(holder, method, instanceCls, newInstance));
//    }
//
//    static private JVar getInstance(EComponentHolder holder, JMethod method, JDefinedClass instanceCls, JInvocation newInstance) {
//        final JVar instance = method.body().decl(JMod.NONE, instanceCls, "instance", moduleGetInstance(holder, instanceCls));
//        method.body()._if(instance.eq(_null()))._then().assign(instance, newInstance);
//        return instance;
//    }


    public static JInvocation getModule(EComponentHolder holder) {
        return getModule(holder, lit(""));
    }

    public static JInvocation getModule(EComponentHolder holder, JExpression moduleName) {
        return moduleGetInstance(holder, moduleName, holder.refClass(ModuleObjectsShare.IModule.class));
    }

//    public static JInvocation moduleGetInstance(EComponentHolder holder, JClass instanceCls) {
//        return moduleGetInstance(holder, "", instanceCls);
//    }

    public static JInvocation moduleGetInstance(EComponentHolder holder, String moduleName, JClass instanceCls) {
        return moduleGetInstance(holder, lit(moduleName), instanceCls);
    }

    public static JInvocation moduleGetInstance(EComponentHolder holder, JExpression moduleName, JClass instanceCls) {
        return holder.refClass(ModuleObjectsShare.class).staticInvoke("getInstance").arg(moduleName).arg(instanceCls.dotclass());
    }

//    public static JInvocation moduleSetInstance(EComponentHolder holder, JClass instanceCls) {
//        return moduleSetInstance(holder, "", instanceCls);
//    }

    private static JInvocation getNewInstance(EComponentHolder holder, AnnotationHelper annotationHelper, JClass instanceCls, boolean isEBean) {
        return isEBean ? instantiateGeneratedClass(holder, annotationHelper, instanceCls.fullName()) : _new(instanceCls);
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, JClass instanceCls, JExpression newInstance) {
        return moduleSetInstance(holder, "", instanceCls, newInstance);
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, AnnotationHelper annotationHelper, String moduleName, JClass instanceCls, boolean isEBean) {
        return moduleSetInstance(holder, moduleName, instanceCls, getNewInstance(holder, annotationHelper, instanceCls, isEBean));
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, JExpression moduleName, JClass instanceCls) {
        return moduleSetInstance(holder, moduleName, instanceCls, getNewInstance(null, null, instanceCls, false));
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, String moduleName, JClass instanceCls, JExpression newInstance) {
        return moduleSetInstance(holder, lit(moduleName), instanceCls, newInstance);
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, JExpression moduleName, JClass instanceCls, JExpression newInstance) {
        return holder.refClass(ModuleObjectsShare.class).staticInvoke("setInstance").arg(moduleName).arg(instanceCls.dotclass()).arg(newInstance);
    }

    public static JInvocation moduleGetInstanceOrAddDefault(EComponentHolder holder, JBlock block, JClass instanceCls, JExpression moduleName) {
        block._if(moduleGetInstance(holder, moduleName, instanceCls).eq(_null()))._then().add(ModuleCodeGenerator.moduleSetInstance(holder, moduleName, instanceCls));
        return moduleGetInstance(holder, moduleName, instanceCls);
    }

    public static JInvocation moduleGetInstanceOrAddDefaultIfNeeded(EComponentHolder holder, AnnotationHelper annotationHelper, JDefinedClass generatedClass, JMethod method, JClass instanceCls, String moduleName, boolean isEBean) {
        final String setInstanceMethodName = "set" + instanceCls.name() + "_" + Math.abs(moduleName.hashCode());
        if (findMethod(generatedClass, setInstanceMethodName) == null) { // if we already have such a method generated it means the instance is already initialized, so don't add unnecessary ifs
            final JMethod setNewInstance = holder.getGeneratedClass().method(JMod.PRIVATE, void.class, setInstanceMethodName);
            setNewInstance.body().add(ModuleCodeGenerator.moduleSetInstance(holder, annotationHelper, moduleName, instanceCls, isEBean));
            method.body().directStatement("// this is needed to ensure we really have instance of " + instanceCls.name());
            method.body()._if(moduleGetInstance(holder, moduleName, instanceCls).eq(_null()))._then().add(_this().invoke(setNewInstance));
        }
        return moduleGetInstance(holder, moduleName, instanceCls);
    }

    public static JMethod findMethod(JDefinedClass definedClass, String methodName) {
        for (JMethod method : definedClass.methods())
            if (method.name().equals(methodName))
                return method;
        return null;
    }

    public static JInvocation createModule(String moduleName, EComponentHolder holder) {
        return holder.refClass(ModuleObjectsShare.class).staticInvoke("createModule").arg(moduleName);
    }

    public static JArray generateSubmodulesArray(Element element, EComponentHolder holder, TargetAnnotationHelper annotationHelper, String annotationName) {
        final List<DeclaredType> submodules = annotationHelper.extractAnnotationClassArrayParameter(element, annotationName, "submodules");
        if (submodules != null && submodules.size() > 0) {
            final JArray submodulesArray = newArray(holder.refClass(Object.class));
            for (DeclaredType type : submodules) {
                submodulesArray.add(instantiateGeneratedClass(holder, annotationHelper, type.toString()));
            }
            return submodulesArray;
        }
        return null;
    }

    private static JInvocation instantiateGeneratedClass(EComponentHolder holder, AnnotationHelper annotationHelper, String className) {
        final JClass submoduleClass = holder.refClass(annotationHelper.generatedClassQualifiedNameFromQualifiedName(className));
        return submoduleClass.staticInvoke(EBeanHolder.GET_INSTANCE_METHOD_NAME).arg(holder.getContextRef());
    }
}
