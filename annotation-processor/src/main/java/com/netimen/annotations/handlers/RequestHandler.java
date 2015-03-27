/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.bookmate.bus.Bus;
import com.netimen.annotations.Request;
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
    protected JClass getProcessingClass(JClass cls, Element element) throws ClassNotFoundException {
        resultClass = codeModel().parseType(((ExecutableElement) element).getReturnType().toString()).boxify();
        return codeModel().ref(Bus.RequestProcessor.class).narrow(resultClass, cls);
    }

    // CUR document, test, validate submodules (no '_' classes) and target == EBCScope
}
