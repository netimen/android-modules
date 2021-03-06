/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.androidmodules.handlers;

import com.netimen.androidmodules.helpers.Bus;
import com.netimen.androidmodules.annotations.Request;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public class RequestHandler extends BusHandler {
    private JClass resultClass;

    public RequestHandler(ProcessingEnvironment processingEnvironment) {
        super(Request.class, processingEnvironment);
    }

    @Override
    protected void addMethodCall(JBlock body, JInvocation processorMethod) {
        body._return(processorMethod);
    }

    @Override
    protected String getProcessingMethodName() {
        return "process";
    }

    @Override
    protected JType getProcessingMethodReturnType() {
        return resultClass;
    }

    @Override
    protected JClass getProcessingClass(JClass eventOrRequestClass, Element element) {
        try {
            resultClass = codeModel().parseType(((ExecutableElement) element).getReturnType().toString()).boxify();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return codeModel().ref(Bus.RequestProcessor.class).narrow(resultClass, eventOrRequestClass);
    }

    // TODO @SubmoduleExclude, @SafeUIThread, @TargeAPI, optimize viewbyid
}
