/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package com.netimen.annotations;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import org.androidannotations.holder.EComponentHolder;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._null;
import static com.sun.codemodel.JExpr._this;

public class ModuleHelper {
    static public void returnNewInstance(EComponentHolder holder, JMethod method, JDefinedClass instanceCls, JInvocation newInstance) {
        method.body()._return(getInstance(holder, method, instanceCls, newInstance));
    }

    static private JVar getInstance(EComponentHolder holder, JMethod method, JDefinedClass instanceCls, JInvocation newInstance) {
        final JVar instance = method.body().decl(JMod.NONE, instanceCls, "instance", moduleGetInstance(holder, instanceCls));
        method.body()._if(instance.eq(_null()))._then().assign(instance, newInstance);
        return instance;
    }

    public static JInvocation moduleGetInstance(EComponentHolder holder, JClass instanceCls) {
        return holder.refClass(com.netimen.annotations.helpers.Module.class).staticInvoke("getInstance").arg(instanceCls.dotclass());
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, JClass instanceCls) {
        return moduleSetInstance(holder, instanceCls, _new(instanceCls));
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, JClass instanceCls, JExpression newInstance) {
        return holder.refClass(com.netimen.annotations.helpers.Module.class).staticInvoke("setInstance").arg(instanceCls.dotclass()).arg(newInstance);
    }

    public static JInvocation moduleGetInstanceOrAddDefault(EComponentHolder holder, JDefinedClass generatedClass, JMethod method, JClass instanceCls) {
        final String setInstanceMethodName = "set" + instanceCls.name() + "_";
        if (findMethod(generatedClass, setInstanceMethodName) == null) {
            final JMethod setNewInstance = holder.getGeneratedClass().method(JMod.PRIVATE, instanceCls, setInstanceMethodName);
            setNewInstance.body()._return(ModuleHelper.moduleSetInstance(holder, instanceCls));
            method.body().directStatement("// this is needed to ensure we really have instance of " + instanceCls.name());
            method.body()._if(moduleGetInstance(holder, instanceCls).eq(_null()))._then().add(_this().invoke(setNewInstance));
        }
        return moduleGetInstance(holder, instanceCls);
    }

    private static JMethod findMethod(JDefinedClass definedClass, String methodName) {
        for (JMethod method : definedClass.methods())
            if (method.name().equals(methodName))
                return method;
        return null;
    }
}
