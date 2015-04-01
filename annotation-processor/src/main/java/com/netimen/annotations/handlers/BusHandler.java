/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.bookmate.bus.Bus;
import com.netimen.annotations.helpers.ModuleHelper;
import com.netimen.annotations.helpers.SourceHelper;
import com.netimen.annotations.helpers.Utility;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.helper.AnnotationHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import java.net.MalformedURLException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JMod.PUBLIC;

public abstract class BusHandler extends BaseAnnotationHandler<EComponentHolder> {
    final AnnotationHelper annotationHelper;

    public BusHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
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

        final JClass cls = getEventOrRequestClass((ExecutableElement) element, methodName); // IsPublic.class
        if (cls == null)
            return;

        JDefinedClass processingClass = codeModel().anonymousClass(getProcessingClass(cls, element)); // Bus.RequestProcessor<Boolean, IsPublic>
        JMethod processingMethod = processingClass.method(PUBLIC, getProcessingMethodReturnType(), getProcessingMethodName()); // Boolean process()
        processingMethod.annotate(Override.class);
        final JVar param = processingMethod.param(cls, "param");
        addMethodCall(processingMethod.body(), callProcessorMethod((ExecutableElement) element, methodName, param)); // return isPublic(param);

        String moduleName = annotationHelper.extractAnnotationParameter(element, getTarget(), "moduleName");
        if (Utility.isEmpty(moduleName)) {
            final DeclaredType moduleClass = annotationHelper.extractAnnotationClassParameter(element, getTarget(), "moduleClass");
            moduleName = moduleClass == null ? "" : moduleClass.toString();
        }
        register(holder, cls, processingClass, moduleName);
    }

    protected abstract void addMethodCall(JBlock body, JInvocation processorMethod);

    protected abstract String getProcessingMethodName();

    protected abstract JType getProcessingMethodReturnType();

    protected abstract JClass getProcessingClass(JClass cls, Element element) throws ClassNotFoundException;


    JClass getEventOrRequestClass(ExecutableElement element, String methodName) throws MalformedURLException, ClassNotFoundException {
        if (Character.isUpperCase(methodName.charAt(0))) {
            methodNameEqualsClassNameError(element, methodName);
            return null;
        }

        TypeMirror classType = annotationHelper.extractAnnotationClassParameter(element, getTarget());

        if (classType == null && element.getParameters().size() > 0) // trying to extract the event/request class from parameters
            classType = element.getParameters().get(0).asType();

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

    private void printError(Element element, String methodName, String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Bus. " + message + " " + element.getEnclosingElement().getSimpleName() + "." + methodName + "()");
    }

    private void methodNameEqualsClassNameError(Element element, String methodName) {
        printError(element, methodName, "we can't generate correct code if the method name is equal to the event/request class name. ");
    }

    private void classNotFoundError(Element element, String methodName, String className) {
        printError(element, methodName, "couldn't load class: " + className + " for");
    }

    JInvocation callProcessorMethod(ExecutableElement element, String methodName, JVar eventOrRequestVar) {
        final JInvocation call = invoke(methodName);
        if (((ExecutableElement) element).getParameters().size() > 0) // passing parameters if needed
            call.arg(eventOrRequestVar);
        return call;
    }

    void register(EComponentHolder holder, JClass cls, JDefinedClass listenerClass, String moduleName) {
        final JInvocation getBus = ModuleHelper.moduleGetInstanceOrAddDefault(holder, holder.getGeneratedClass(), holder.getInit(), codeModel().ref(Bus.class), moduleName);
        final JInvocation register = getBus.invoke("register").arg(cls.dotclass()).arg(_new(listenerClass));
        JMethod method = ModuleHelper.findMethod(holder.getGeneratedClass(), "onViewChanged");
        if (method != null)
            method.body().directStatement("// register in onViewChanged, because bus may have been initialized in a fragment");
        if (method == null) method = holder.getInit();
        method.body().add(register);
    }


}
