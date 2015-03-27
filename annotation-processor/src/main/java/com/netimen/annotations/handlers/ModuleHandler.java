/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.annotations.handlers;

import com.netimen.annotations.Module;
import com.netimen.annotations.helpers.ModuleHelper;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class ModuleHandler extends BaseAnnotationHandler<EComponentHolder> {

    public ModuleHandler(ProcessingEnvironment processingEnvironment) {
        super(Module.class, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        validatorHelper.enclosingElementHasEnhancedComponentAnnotation(element, validatedElements, valid);

        validatorHelper.isNotPrivate(element, valid);
    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {
        holder.getInitBody().add(ModuleHelper.initModule(holder, element.getAnnotation(Module.class).value()));
    }
}


