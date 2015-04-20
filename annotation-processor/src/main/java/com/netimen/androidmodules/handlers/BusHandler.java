/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 * <p/>
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.androidmodules.handlers;

import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.helpers.Bus;
import com.netimen.androidmodules.helpers.ModuleCodeGenerator;
import com.netimen.androidmodules.helpers.ModuleObjectsShare;
import com.netimen.androidmodules.helpers.SourceHelper;
import com.netimen.androidmodules.helpers.Utility;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.helper.AnnotationHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JExpr.lit;
import static com.sun.codemodel.JMod.PUBLIC;

public abstract class BusHandler extends BaseAnnotationHandler<EComponentHolder> {
    private final AnnotationHelper annotationHelper;
    private ParamType firstParamType = ParamType.NONE, secondParamType = ParamType.NONE;

    BusHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
        annotationHelper = new AnnotationHelper(processingEnv);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

        validatorHelper.enclosingElementHasEnhancedComponentAnnotation(element, validatedElements, valid);

        ExecutableElement executableElement = (ExecutableElement) element;

        validatorHelper.isNotPrivate(element, valid);

        validatorHelper.doesntThrowException(executableElement, valid);

    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {

        final String methodName = element.getSimpleName().toString(); // isPublic
        parseParameters((ExecutableElement) element);
        final JClass eventOrRequestClass = getEventOrRequestClass((ExecutableElement) element, methodName); // IsPublic.class
        if (eventOrRequestClass == null)
            return;

        List<String> modulesNames = new ArrayList<>();
        final String[] modulesNamesParam = annotationHelper.extractAnnotationParameter(element, getTarget(), "moduleName");
        if (modulesNamesParam != null)
            if (modulesNamesParam.length == 1 && Event.ANY_MODULE.equals(modulesNamesParam[0])) {
                registerAll(holder, element, methodName, eventOrRequestClass);
                return;
            } else
                for (String moduleName : modulesNamesParam)
                    if (!Utility.isEmpty(moduleName))
                        modulesNames.add(moduleName);

        final List<DeclaredType> moduleClasses = annotationHelper.extractAnnotationClassArrayParameter(element, getTarget(), "moduleClass");
        if (moduleClasses != null)
            for (DeclaredType t : moduleClasses)
                modulesNames.add(t.toString());

        registerMany(holder, element, methodName, eventOrRequestClass, modulesNames);
    }

    ////

    protected abstract void addMethodCall(JBlock body, JInvocation processorMethod);

    protected abstract String getProcessingMethodName();

    protected abstract JType getProcessingMethodReturnType();

    protected abstract JClass getProcessingClass(JClass eventOrRequestClass, Element element);

    ////


    private void printError(Element element, String methodName, String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Bus. " + message + " " + element.getEnclosingElement().getSimpleName() + "." + methodName + "()");
    }

    private void methodNameEqualsClassNameError(Element element, String methodName) {
        printError(element, methodName, "we can't generate correct code if the method name is equal to the event/request class name. ");
    }

    private void classNotFoundError(Element element, String methodName, String className) {
        printError(element, methodName, "couldn't load class: " + className + " for");
    }

    ///

    private void parseParameters(ExecutableElement element) {
        firstParamType = parseParameter(element, 0);
        secondParamType = parseParameter(element, 1);
    }

    private ParamType parseParameter(ExecutableElement element, int paramNo) {
        if (element.getParameters().size() > paramNo) {
            final TypeMirror typeMirror = element.getParameters().get(paramNo).asType();
            return ModuleObjectsShare.IModule.class.getCanonicalName().equals(typeMirror.toString()) ? ParamType.MODULE : ParamType.EVENT_OR_REQUEST;
        }
        return ParamType.NONE;
    }

    ////

    JClass getEventOrRequestClass(ExecutableElement element, String methodName) {
        if (Character.isUpperCase(methodName.charAt(0))) {
            methodNameEqualsClassNameError(element, methodName);
            return null;
        }

        TypeMirror classType = annotationHelper.extractAnnotationClassParameter(element, getTarget());

        if (classType == null && firstParamType == ParamType.EVENT_OR_REQUEST) // trying to extract the event/request class from parameters
            classType = element.getParameters().get(0).asType();
        if (classType == null && secondParamType == ParamType.EVENT_OR_REQUEST) // trying to extract the event/request class from parameters
            classType = element.getParameters().get(1).asType();

        String className, extractedName = null;
        if (classType != null)
            className = classType.toString();
        else {
            extractedName = methodName.startsWith("on") ? methodName.substring(2) : methodName;
            extractedName = extractedName.substring(0, 1).toUpperCase() + extractedName.substring(1); // making it start from an uppercase letter
            className = SourceHelper.getClassesFullNameMap(processingEnv).get(extractedName); // trying to guess the package
            if (className == null) {
                classNotFoundError(element, methodName, extractedName);
                return null;
            }
        }

        try {
            JClass jClass = SourceHelper.getClass(className); // for some reason if I call codeModel().ref twice on the same class it doesn't get imported correctly in the generated code. So I reuse previous JClass instead
            if (jClass == null) {
                jClass = codeModel().ref(className);
                SourceHelper.addClass(className, jClass);
            }
            return jClass;
        } catch (Exception e) {
            classNotFoundError(element, methodName, (className == null || className.length() == 0 ? extractedName : className));
            return null;
        }
    }

    ////

    JDefinedClass createProcessingClass(EComponentHolder holder, Element element, String methodName, JClass eventOrRequestClass, JExpression moduleName) {
        JDefinedClass processingClass = codeModel().anonymousClass(getProcessingClass(eventOrRequestClass, element)); // Bus.RequestProcessor<Boolean, IsPublic>
        JMethod processingMethod = processingClass.method(PUBLIC, getProcessingMethodReturnType(), getProcessingMethodName()); // Boolean process()
        processingMethod.annotate(Override.class);
        final JVar param = processingMethod.param(eventOrRequestClass, "param");
        addMethodCall(processingMethod.body(), callProcessorMethod(holder, methodName, param, moduleName)); // return isPublic(param);
        return processingClass;
    }

    private JInvocation callProcessorMethod(EComponentHolder holder, String methodName, JVar eventOrRequestVar, JExpression moduleName) {
        final JInvocation call = invoke(methodName);
        addArgument(holder, call, firstParamType, eventOrRequestVar, moduleName);
        addArgument(holder, call, secondParamType, eventOrRequestVar, moduleName);
        return call;
    }

    private void addArgument(EComponentHolder holder, JInvocation call, ParamType argType, JVar eventOrRequestVar, JExpression moduleName) {
        if (argType == ParamType.EVENT_OR_REQUEST) // passing parameters if needed
            call.arg(eventOrRequestVar);
        else if (argType == ParamType.MODULE)
            call.arg(ModuleCodeGenerator.getModule(holder, moduleName));
    }

    ////

    private void registerAll(EComponentHolder holder, Element element, String methodName, JClass eventOrRequestClass) {
        JMethod method = getMethodForRegistering(holder);
        final JClass moduleProviderClass = refClass(ModuleObjectsShare.class);
        final JInvocation modulesNames = moduleProviderClass.staticInvoke("modulesNames");
        final JConditional isEmpty = method.body()._if(modulesNames.invoke("isEmpty"));
        register(holder, element, methodName, eventOrRequestClass, lit(""), isEmpty._then());
        final JForEach forEach = isEmpty._else().forEach(refClass(String.class), "moduleName", modulesNames);
        forEach.body().directStatement("// we need to store module name in some final variable to use it in the listener");
        final JVar moduleNameToPass = forEach.body().decl(JMod.FINAL, refClass(String.class), "moduleNameToPass", forEach.var());
        register(holder, element, methodName, eventOrRequestClass, moduleNameToPass, forEach.body());
    }

    private void registerMany(EComponentHolder holder, Element element, String methodName, JClass eventOrRequestClass, List<String> modulesNames) {
        JMethod method = getMethodForRegistering(holder);
        if (modulesNames.size() > 0)
            for (String moduleName : modulesNames)
                register(holder, element, methodName, eventOrRequestClass, moduleName, method);
        else // registering to default module
            register(holder, element, methodName, eventOrRequestClass, "", method);
    }

    private JMethod getMethodForRegistering(EComponentHolder holder) {
        JMethod method = ModuleCodeGenerator.findMethod(holder.getGeneratedClass(), "onViewChanged");
        if (method != null)
            method.body().directStatement("// register in onViewChanged, because bus may have been initialized in a fragment");
        if (method == null) method = holder.getInit();
        return method;
    }

    private void register(EComponentHolder holder, Element element, String methodName, JClass eventOrRequestClass, String moduleName, JMethod method) {
        final JInvocation getBus = ModuleCodeGenerator.moduleGetInstanceOrAddDefaultIfNeeded(holder, holder.getGeneratedClass(), method, codeModel().ref(Bus.class), moduleName);
        performRegister(eventOrRequestClass, createProcessingClass(holder, element, methodName, eventOrRequestClass, lit(moduleName)), method.body(), getBus);
    }

    private void register(EComponentHolder holder, Element element, String methodName, JClass eventOrRequestClass, JExpression moduleName, JBlock block) {
        final JInvocation getBus = ModuleCodeGenerator.moduleGetInstanceOrAddDefault(holder, block, codeModel().ref(Bus.class), moduleName);
        performRegister(eventOrRequestClass, createProcessingClass(holder, element, methodName, eventOrRequestClass, moduleName), block, getBus);
    }

    private void performRegister(JClass eventOrRequestClass, JDefinedClass processingClass, JBlock block, JInvocation getBus) {
        final JInvocation register = getBus.invoke("register").arg(eventOrRequestClass.dotclass()).arg(_new(processingClass));
        block.add(register);
    }

    private enum ParamType {
        NONE, EVENT_OR_REQUEST, MODULE
    }

}
