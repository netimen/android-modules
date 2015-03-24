/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.netimen.annotations.helpers.Module;
import com.netimen.annotations.InjectInitScope;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JFieldRef;

import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr.ref;

public class InjectInitScopeHandler extends BaseAnnotationHandler<EComponentHolder> {
    private final TargetAnnotationHelper annotationHelper;

    public InjectInitScopeHandler(ProcessingEnvironment processingEnvironment) {
        super(InjectInitScope.class, processingEnvironment);
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

        JClass injectedClass = refClass(typeMirror.toString());
        JFieldRef injectField = ref(element.getSimpleName().toString());

        holder.getInitBody().assign(injectField, codeModel().ref(Module.class).staticInvoke("setInstance").arg(injectedClass.dotclass()).arg(_new(injectedClass))); // field = InjectInstanceProvider.set(new ...)
    }
}
