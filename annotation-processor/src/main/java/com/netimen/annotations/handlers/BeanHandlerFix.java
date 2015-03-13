/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.netimen.annotations.EBeanCustomScope;

import org.androidannotations.annotations.EBean;
import org.androidannotations.handler.BeanHandler;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

/**
 * needed to make @Bean work with @EBeanCustomScope (fixes validation errors)
 */
public class BeanHandlerFix extends BeanHandler {

    private final TargetAnnotationHelper annotationHelper;

    public BeanHandlerFix(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
    }

    @Override
    public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        validatorHelper.enclosingElementHasEnhancedComponentAnnotation(element, validatedElements, valid);

        validatorHelper.isNotPrivate(element, valid);

        typeHasAnnotations(Arrays.asList(EBean.class, EBeanCustomScope.class), element.asType(), element, valid);
    }

    /// all the following just adds missing functionality to ValidatorHelper

    public void typeHasAnnotations(List<Class<? extends Annotation>> annotations, TypeMirror elementType, Element reportingElement, IsValid valid) {
        Element typeElement = annotationHelper.getTypeUtils().asElement(elementType);

        for (Class<? extends Annotation> annotation : annotations)
            if (elementHasAnnotationSafe(annotation, typeElement))
                return;

        valid.invalidate();
        annotationHelper.printAnnotationError(reportingElement, "%s can only be used on an element annotated with " + getFormattedValidEnhancedBeanAnnotationTypes(annotations));
    }

    private boolean elementHasAnnotationSafe(Class<? extends Annotation> annotation, Element element) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getName())) {
                return true;
            }
        }
        return false;
    }

    private String getFormattedValidEnhancedBeanAnnotationTypes(List<Class<? extends Annotation>> annotations) {
        StringBuilder sb = new StringBuilder();
        if (!annotations.isEmpty()) {
            sb.append(TargetAnnotationHelper.annotationName(annotations.get(0)));

            for (int i = 1; i < annotations.size(); i++) {
                sb.append(", ");
                sb.append(TargetAnnotationHelper.annotationName(annotations.get(i)));
            }
        }

        return sb.toString();
    }
}
