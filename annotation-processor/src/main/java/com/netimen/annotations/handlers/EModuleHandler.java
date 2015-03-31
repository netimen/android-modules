/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.annotations.handlers;

import com.bookmate.bus.Bus;
import com.netimen.annotations.EModule;
import com.netimen.annotations.helpers.ModuleHelper;
import com.netimen.annotations.helpers.ModuleProvider;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.helper.ModelConstants;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import static com.sun.codemodel.JExpr._this;

public class EModuleHandler extends BaseAnnotationHandler<EComponentHolder> {

    private final TargetAnnotationHelper annotationHelper;

    public EModuleHandler(ProcessingEnvironment processingEnvironment) {
        super(EModule.class, processingEnvironment);
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
        holder.getGeneratedClass()._implements(ModuleProvider.IModule.class);
        final JFieldVar instances = holder.getGeneratedClass().field(JMod.PRIVATE, ModuleProvider.InstancesHolder.class, "instances_");
        holder.getInitBody().assign(instances, ModuleHelper.createModule(element.getAnnotation(EModule.class).moduleName(), holder));
        holder.getInitBody().directStatement("// submodules communicate via {@link " + refClass(Bus.class).fullName() + "}, so we only need to store them");
        holder.getInitBody().add(instances.invoke("setInaccessibleInstances").arg(ModuleHelper.generateSubmodulesArray(element, holder, annotationHelper, getTarget())));
        holder.getInitBody().add(ModuleHelper.moduleSetInstance(holder, refClass(ModuleProvider.IModule.class), _this()));
//        final JFieldVar instances = holder.getGeneratedClass().field(PRIVATE, refClass(Map.class).narrow(Object.class, WeakReference.class), "instances_", _new(refClass(HashMap.class)));
//        holder.getGeneratedClass().method()
//        ModuleHelper.addSubmodulesField(holder.getGeneratedClass());
//        ModuleHelper.addSubmodules(element, holder, holder.getInitBody(), annotationHelper, getTarget(), ref(ModuleHelper.SUBMODULES_FIELD));
    }

    private boolean skipGeneratedClass(Element element) {
        return element.getSimpleName().toString().endsWith("_");
    }
}


