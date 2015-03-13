/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.netimen.annotations.BeanInitScope;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import static com.sun.codemodel.JExpr.ref;

public class BeanInitScopeHandler extends BaseAnnotationHandler<EComponentHolder> {
    public static final String INIT_INSTANCE_METHOD_NAME = "initInstance_";

    private final TargetAnnotationHelper annotationHelper;

    public BeanInitScopeHandler(ProcessingEnvironment processingEnvironment) {
        super(BeanInitScope.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        validatorHelper.enclosingElementHasEnhancedComponentAnnotation(element, validatedElements, valid);

        validatorHelper.isNotPrivate(element, valid);
    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {

        // I just copied this block from BeanHandler, I'm not sure I understand it
        TypeMirror typeMirror = annotationHelper.extractAnnotationClassParameter(element);
        if (typeMirror == null) {
            typeMirror = element.asType();
            typeMirror = holder.processingEnvironment().getTypeUtils().erasure(typeMirror);
        }

        JClass injectedClass = refClass(annotationHelper.generatedClassQualifiedNameFromQualifiedName(typeMirror.toString()));
        JFieldRef beanField = ref(element.getSimpleName().toString());
        JInvocation initInstance = injectedClass.staticInvoke(INIT_INSTANCE_METHOD_NAME).arg(holder.getContextRef());

        holder.getInitBody().assign(beanField, initInstance);
    }

}
