/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.bookmate.bus.Bus;
import com.netimen.annotations.Event;
import com.netimen.annotations.helpers.ModuleHelper;
import com.netimen.annotations.helpers.ModuleProvider;
import com.netimen.annotations.helpers.SourceHelper;
import com.netimen.annotations.helpers.Utility;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JForEach;
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

        List<String> modulesNames = new ArrayList<>();
        final String[] modulesNamesParam = annotationHelper.extractAnnotationParameter(element, getTarget(), "moduleName");
        if (modulesNamesParam != null)
            if (modulesNamesParam.length == 1 && Event.ANY_MODULE.equals(modulesNamesParam[0]))
                registerAll(holder, cls, processingClass);
            else
                for (String moduleName : modulesNamesParam)
                    if (!Utility.isEmpty(moduleName))
                        modulesNames.add(moduleName);

        final List<DeclaredType> moduleClasses = annotationHelper.extractAnnotationClassArrayParameter(element, getTarget(), "moduleClass");
        if (moduleClasses != null)
            for (DeclaredType t : moduleClasses)
                modulesNames.add(t.toString());

        registerMany(holder, cls, processingClass, modulesNames);
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

    private JInvocation callProcessorMethod(ExecutableElement element, String methodName, JVar eventOrRequestVar) {
        final JInvocation call = invoke(methodName);
        if (((ExecutableElement) element).getParameters().size() > 0) // passing parameters if needed
            call.arg(eventOrRequestVar);
        return call;
    }

    private void registerAll(EComponentHolder holder, JClass cls, JDefinedClass listenerClass) {
        JMethod method = getMethodForRegistering(holder);
        final JClass moduleProviderClass = refClass(ModuleProvider.class);
        final JInvocation modulesNames = moduleProviderClass.staticInvoke("modulesNames");
        final JConditional isEmpty = method.body()._if(modulesNames.invoke("isEmpty"));
        final JForEach forEach = isEmpty._then().forEach(refClass(String.class), "moduleName", modulesNames);
        register(holder, cls, listenerClass, forEach.var(), forEach.body());
//            register(holder, cls, listenerClass, "", method);
    }

    private void registerMany(EComponentHolder holder, JClass cls, JDefinedClass listenerClass, List<String> modulesNames) {
        JMethod method = getMethodForRegistering(holder);
        if (modulesNames.size() > 0)
            for (String moduleName : modulesNames)
                register(holder, cls, listenerClass, moduleName, method);
        else // registering to default module
            register(holder, cls, listenerClass, "", method);
    }

    private JMethod getMethodForRegistering(EComponentHolder holder) {
        JMethod method = ModuleHelper.findMethod(holder.getGeneratedClass(), "onViewChanged");
        if (method != null)
            method.body().directStatement("// register in onViewChanged, because bus may have been initialized in a fragment");
        if (method == null) method = holder.getInit();
        return method;
    }

    private void register(EComponentHolder holder, JClass cls, JDefinedClass listenerClass, String moduleName, JMethod method) {
        final JInvocation getBus = ModuleHelper.moduleGetInstanceOrAddDefaultIfNeeded(holder, holder.getGeneratedClass(), method, codeModel().ref(Bus.class), moduleName);
        performRegister(cls, listenerClass, method.body(), getBus);
    }

    private void register(EComponentHolder holder, JClass cls, JDefinedClass listenerClass, JExpression moduleName, JBlock block) {
        final JInvocation getBus = ModuleHelper.moduleGetInstanceOrAddDefault(holder, block, codeModel().ref(Bus.class), moduleName);
        performRegister(cls, listenerClass, block, getBus);
    }

    private void performRegister(JClass cls, JDefinedClass listenerClass, JBlock block, JInvocation getBus) {
        final JInvocation register = getBus.invoke("register").arg(cls.dotclass()).arg(_new(listenerClass));
        block.add(register);
    }


}
