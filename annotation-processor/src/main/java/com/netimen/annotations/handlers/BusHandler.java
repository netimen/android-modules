/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.bookmate.bus.Bus;
import com.netimen.annotations.MethodNames;
import com.netimen.annotations.helpers.InjectInstanceProvider;
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

        final JClass cls = getEventOrRequestClass(element, methodName); // IsPublic.class
        if (cls == null)
            return;

        JDefinedClass processingClass = codeModel().anonymousClass(getProcessingClass(cls, element)); // Bus.RequestProcessor<Boolean, IsPublic>
        JMethod processingMethod = processingClass.method(PUBLIC, getProcessingMethodReturnType(), getProcessingMethodName()); // Boolean process()
        processingMethod.annotate(Override.class);
        final JVar param = processingMethod.param(cls, "param");
        addMethodCall(processingMethod.body(), callProcessorMethod((ExecutableElement) element, methodName, param)); // return isPublic(param);

        register(holder, cls, processingClass);
    }

    protected abstract void addMethodCall(JBlock body, JInvocation processorMethod);

    protected abstract String getProcessingMethodName();

    protected abstract JType getProcessingMethodReturnType();

    protected abstract JClass getProcessingClass(JClass cls, Element element) throws ClassNotFoundException;


    JClass getEventOrRequestClass(Element element, String methodName) throws MalformedURLException, ClassNotFoundException {
        final DeclaredType classType = annotationHelper.extractAnnotationClassParameter(element, getTarget());

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
            return codeModel().ref(className);
        } catch (Exception e) {
            classNotFoundError(element, methodName, (className == null || className.length() == 0 ? extractedName : className));
            return null;
        }
    }

    private void classNotFoundError(Element element, String methodName, String className) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Bus. couldn't load class: " + className + " for " + element.getEnclosingElement().getSimpleName() + "." + methodName);
    }

    JInvocation callProcessorMethod(ExecutableElement element, String methodName, JVar eventOrRequestVar) {
        final JInvocation call = invoke(methodName);
        if (((ExecutableElement) element).getParameters().size() > 0) // passing parameters if needed
            call.arg(eventOrRequestVar);
        return call;
    }

    void register(EComponentHolder holder, JClass cls, JDefinedClass listenerClass) {
        JClass providerClass = refClass(InjectInstanceProvider.class);
        final JInvocation getBus = providerClass.staticInvoke(MethodNames.GET).arg(codeModel().ref(Bus.class).dotclass());
        final JInvocation register = getBus.invoke("register").arg(cls.dotclass()).arg(_new(listenerClass));
        holder.getInitBody().add(register);
    }


}
