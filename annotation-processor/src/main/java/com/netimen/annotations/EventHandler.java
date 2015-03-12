/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   11.03.15
 */
package com.netimen.annotations;

import com.bookmate.bus.Bus;
import com.bookmate.bus.BusProvider;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.helper.AnnotationHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;

import netimen.com.annotations.Event;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JMod.PUBLIC;

public class EventHandler extends BaseAnnotationHandler<EComponentHolder> {
    private final AnnotationHelper annotationHelper;

    public EventHandler(ProcessingEnvironment processingEnvironment) {
        super(Event.class, processingEnvironment);
        annotationHelper = new AnnotationHelper(processingEnv);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

        validatorHelper.enclosingElementHasEnhancedComponentAnnotation(element, validatedElements, valid);

        ExecutableElement executableElement = (ExecutableElement) element;

        validatorHelper.returnTypeIsVoid(executableElement, valid);

        validatorHelper.isNotPrivate(element, valid);

        validatorHelper.doesntThrowException(executableElement, valid);

    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {

        final String onEventMethodName = element.getSimpleName().toString(); // onPageShown

        final DeclaredType eventClassType = annotationHelper.extractAnnotationClassParameter(element, getTarget());
        final String eventClassName = eventClassType == null ? onEventMethodName.substring(2) : eventClassType.toString(); // PageShown, trying to extract from method name, omitting 'on'
        final JClass eventClass = holder.refClass(eventClassName);

        JDefinedClass listenerClass = codeModel().anonymousClass(codeModel().ref(Bus.EventListener.class).narrow(eventClass)); // EventListener<PageShown>
        JMethod onEventMethod = listenerClass.method(PUBLIC, codeModel().VOID, "onEvent");
        onEventMethod.annotate(Override.class);
        final JVar eventVar = onEventMethod.param(eventClass, "event");

        final JInvocation onEventCall = invoke(onEventMethodName);
        if (((ExecutableElement) element).getParameters().size() > 0) // passing parameters if needed
            onEventCall.arg(eventVar);
        onEventMethod.body().add(onEventCall);

        JClass busProviderClass = refClass(BusProvider.class);
        final JInvocation getBus = busProviderClass.staticInvoke("getBus").arg(holder.getContextRef());
        final JInvocation register = getBus.invoke("register").arg(eventClass.dotclass()).arg(_new(listenerClass));
        holder.getInitBody().add(register);
    }
}
