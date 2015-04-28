/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   11.03.15
 */
package com.netimen.androidmodules.handlers;

import com.netimen.androidmodules.helpers.Bus;
import com.netimen.androidmodules.annotations.Event;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JType;

import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public class EventHandler extends BusHandler {

    public EventHandler(ProcessingEnvironment processingEnvironment) {
        super(Event.class, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        super.validate(element, validatedElements, valid);

        validatorHelper.returnTypeIsVoid((ExecutableElement) element, valid);

    }

    @Override
    protected JType getProcessingMethodReturnType() {
        return codeModel().VOID;
    }

    @Override
    protected JClass getProcessingClass(JClass eventOrRequestClass, Element element) {
        return codeModel().ref(Bus.EventListener.class).narrow(eventOrRequestClass);
    }

    @Override
    protected String getProcessingMethodName() {
        return "onEvent";
    }

    @Override
    protected void addMethodCall(JBlock body, JInvocation processorMethod) {
        body.add(processorMethod);
    }

}
