/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.androidannotationsfix;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JClass;

import org.androidannotations.holder.EBeanHolder;
import org.androidannotations.holder.GeneratedClassHolder;
import org.androidannotations.process.ProcessHolder;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import static com.sun.codemodel.JMod.FINAL;
import static com.sun.codemodel.JMod.PUBLIC;
import static com.sun.codemodel.JMod.STATIC;
import static org.androidannotations.helper.ModelConstants.GENERATION_SUFFIX;

/**
 * needed only to prevent copying of EBeanCustomScope annotation to the generated class
 */
public class EBeanHolderFix extends EBeanHolder {
    public EBeanHolderFix(ProcessHolder processHolder, TypeElement annotatedComponent) throws Exception {
        super(processHolder, annotatedComponent);
    }

    @Override
    protected void setGeneratedClass() throws Exception {
        String annotatedComponentQualifiedName = annotatedElement.getQualifiedName().toString();
        annotatedClass = codeModel().directClass(annotatedElement.asType().toString());

        if (annotatedElement.getNestingKind().isNested()) {
            Element enclosingElement = annotatedElement.getEnclosingElement();
            GeneratedClassHolder enclosingHolder = processHolder.getGeneratedClassHolder(enclosingElement);
            String generatedBeanSimpleName = annotatedElement.getSimpleName().toString() + GENERATION_SUFFIX;
            generatedClass = enclosingHolder.getGeneratedClass()._class(PUBLIC | FINAL | STATIC, generatedBeanSimpleName, ClassType.CLASS);
        } else {
            String generatedClassQualifiedName = annotatedComponentQualifiedName + GENERATION_SUFFIX;
            generatedClass = codeModel()._class(PUBLIC | FINAL, generatedClassQualifiedName, ClassType.CLASS);
        }
        for (TypeParameterElement typeParam : annotatedElement.getTypeParameters()) {
            JClass bound = codeModelHelper.typeBoundsToJClass(this, typeParam.getBounds());
            generatedClass.generify(typeParam.getSimpleName().toString(), bound);
        }
        setExtends();

        // all the above just copied from super
        addNonAAAnnotations(generatedClass, annotatedElement.getAnnotationMirrors(), this);
    }

    private void addNonAAAnnotations(JAnnotatable annotatable, List<? extends AnnotationMirror> annotationMirrors, GeneratedClassHolder holder) {
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            JClass annotationClass = codeModelHelper.typeMirrorToJClass(annotationMirror.getAnnotationType(), holder);
            if (!annotationClass.fullName().startsWith("org.androidannotations") && !isOurAnnotation(annotationClass)) { // avoiding our annotations
                codeModelHelper.addAnnotation(annotatable, annotationMirror, holder);
            }
        }
    }

    private boolean isOurAnnotation(JClass annotationClass) {
        final String packageName = getClass().getPackage().getName();
        return annotationClass.fullName().startsWith(packageName.substring(0, packageName.lastIndexOf('.'))); // removing .androidannotationsfix
    }
}
