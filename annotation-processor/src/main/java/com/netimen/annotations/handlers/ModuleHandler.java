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
import org.androidannotations.helper.ModelConstants;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import static com.sun.codemodel.JExpr.ref;

public class ModuleHandler extends BaseAnnotationHandler<EComponentHolder> {

    private final TargetAnnotationHelper annotationHelper;

    public ModuleHandler(ProcessingEnvironment processingEnvironment) {
        super(Module.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        if (skipGeneratedClass(element)) // I can't prevent copying @Module to generated class, so just don't process it
            return;
        if (!annotationHelper.hasOneOfClassAnnotations(element, ModelConstants.VALID_ENHANCED_COMPONENT_ANNOTATIONS)) {
            valid.invalidate();
            annotationHelper.printAnnotationError(element, "%s can only be used on a class annotated with @EBean etc.");
        }
    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {
        if (skipGeneratedClass(element)) // I can't prevent copying @Module to generated class, so just don't process it
            return;
        holder.getInitBody().add(ModuleHelper.initModule(holder, element.getAnnotation(Module.class).moduleName()));
        ModuleHelper.addSubmodulesField(holder.getGeneratedClass());
        ModuleHelper.addSubmodules(element, holder, holder.getInitBody(), annotationHelper, getTarget(), ref(ModuleHelper.SUBMODULES_FIELD));
    }

    private boolean skipGeneratedClass(Element element) {
        return element.getSimpleName().toString().endsWith("_");
    }
}


