/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   11.03.15
 */
package com.netimen.annotations.handlers;

import com.bookmate.bus.Bus;
import com.netimen.annotations.Event;
import com.netimen.annotations.MethodNames;
import com.netimen.annotations.helpers.InjectInstanceProvider;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;

import org.androidannotations.holder.EComponentHolder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JMod.PUBLIC;

public class EventHandler extends BusHandler {

    public EventHandler(ProcessingEnvironment processingEnvironment) {
        super(Event.class, processingEnvironment);
    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {

        final String methodName = element.getSimpleName().toString(); // onPageShown

        final JClass cls = getEventOrRequestClass(element, holder, methodName);

        JDefinedClass listenerClass = codeModel().anonymousClass(codeModel().ref(Bus.EventListener.class).narrow(cls)); // EventListener<PageShown>
        JMethod onEventMethod = listenerClass.method(PUBLIC, codeModel().VOID, "onEvent");
        onEventMethod.annotate(Override.class);
        final JVar eventVar = onEventMethod.param(cls, "event");

        final JInvocation onEventCall = invoke(methodName);
        if (((ExecutableElement) element).getParameters().size() > 0) // passing parameters if needed
            onEventCall.arg(eventVar);
        onEventMethod.body().add(onEventCall);

        JClass providerClass = refClass(InjectInstanceProvider.class);
        final JInvocation getBus = providerClass.staticInvoke(MethodNames.GET).arg(codeModel().ref(Bus.class).dotclass());
        final JInvocation register = getBus.invoke("register").arg(cls.dotclass()).arg(_new(listenerClass));
        holder.getInitBody().add(register);
    }

}
