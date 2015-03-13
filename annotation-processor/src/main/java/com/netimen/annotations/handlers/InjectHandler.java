/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.annotations.handlers;

import com.netimen.annotations.helpers.InjectInstanceProvider;
import com.netimen.annotations.Inject;
import com.netimen.annotations.MethodNames;
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

import static com.sun.codemodel.JExpr.ref;

public class InjectHandler extends BaseAnnotationHandler<EComponentHolder> {

    private final TargetAnnotationHelper annotationHelper;

    public InjectHandler(ProcessingEnvironment processingEnvironment) {
        super(Inject.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
    }

    @Override
    public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
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

        holder.getInitBody().assign(injectField, codeModel().ref(InjectInstanceProvider.class).staticInvoke(MethodNames.GET).arg(injectedClass.dotclass())); // field = InjectInstanceProvider.get()
    }
}
