/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 * <p/>
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.androidmodules.handlers;

import com.netimen.androidmodules.annotations.EModule;
import com.netimen.androidmodules.helpers.Bus;
import com.netimen.androidmodules.helpers.ModuleCodeGenerator;
import com.netimen.androidmodules.helpers.ModuleObjectsShare;
import com.netimen.androidmodules.helpers.Utility;
import com.sun.codemodel.JArray;
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
        try {
            element.getAnnotation(EModule.class);
        } catch (Exception e) { // usually something bad happens when someone has added a generated class as a submodule
            valid.invalidate();
            annotationHelper.printAnnotationError(element, "something bad happened. Did you try to use a generated class as a submodule? Use non-generated (without '_') instead");
        }
    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {
        if (skipGeneratedClass(element)) // I can't prevent copying @Module to generated class, so just don't process it
            return;
        holder.getGeneratedClass()._implements(ModuleObjectsShare.IModule.class);
        final JFieldVar instances = holder.getGeneratedClass().field(JMod.PRIVATE, ModuleObjectsShare.InstancesHolder.class, "instances_");

        String moduleName = element.getAnnotation(EModule.class).moduleName();
        if (Utility.isEmpty(moduleName))
            moduleName = element.asType().toString();
        holder.getInitBody().assign(instances, ModuleCodeGenerator.createModule(moduleName, holder));

        holder.getInitBody().add(ModuleCodeGenerator.moduleSetInstance(holder, refClass(ModuleObjectsShare.IModule.class), _this()));
        holder.getInitBody().directStatement("// submodules communicate via {@link " + refClass(Bus.class).fullName() + "}, so we only need to store them");

        final JArray submodules = ModuleCodeGenerator.generateSubmodulesArray(element, holder, annotationHelper, getTarget());
        if (submodules != null) {
            final JFieldVar submodulesField = holder.getGeneratedClass().field(JMod.PRIVATE, Object[].class, "submodules_");
            holder.getInitBody().assign(submodulesField, submodules);
        }
    }

    private boolean skipGeneratedClass(Element element) {
        return element.getSimpleName().toString().endsWith("_");
    }
}


