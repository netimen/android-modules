/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   11.03.15
 */
package com.netimen.annotations;

import com.bookmate.bus.Bus;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import netimen.com.annotations.Event;

public class EventHandler extends BaseAnnotationHandler<EComponentHolder> {
    public EventHandler(ProcessingEnvironment processingEnvironment) {
        super(Event.class, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {

//        JDefinedClass anonymousReceiverClass = codeModel().anonymousClass(classes().BROADCAST_RECEIVER);
//        JExpression receiverInit = JExpr._new(anonymousReceiverClass);
//        holder.getGeneratedClass().field(JMod.PRIVATE | JMod.FINAL, classes().BROADCAST_RECEIVER, "aaaa", receiverInit);

        JFieldRef fullScreen = classes().WINDOW_MANAGER_LAYOUT_PARAMS.staticRef("FLAG_FULLSCREEN");
        JClass busProviderClass = refClass(Bus.class);
        final JInvocation bus = busProviderClass.staticInvoke("getBus");
        final JInvocation register = bus.invoke("register");
        holder.getInitBody().add(register);
    }
}
