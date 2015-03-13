/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.annotations.handlers;

import com.netimen.annotations.helpers.InjectInstanceProvider;
import com.netimen.annotations.CustomInjectInstance;
import com.netimen.annotations.MethodNames;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JInvocation;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import static com.sun.codemodel.JExpr._this;

// CUR remove this
public class CustomInjectInstanceHandler extends BaseAnnotationHandler<EComponentHolder> {

    public CustomInjectInstanceHandler(ProcessingEnvironment processingEnvironment) {
        super(CustomInjectInstance.class, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
//        validatorHelper.enclosingElementHasEnhancedComponentAnnotation(element, validatedElements, valid);
    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {
        JClass providerClass = refClass(InjectInstanceProvider.class);
        final JInvocation set = providerClass.staticInvoke(MethodNames.SET).arg(codeModel().ref(element.getSimpleName().toString()).dotclass()).arg(_this());
        try {
            holder.getInitBody().add(set);
        } catch (Exception e) { // I don't know why there is exception here
            e.printStackTrace();
        }
    }
}
