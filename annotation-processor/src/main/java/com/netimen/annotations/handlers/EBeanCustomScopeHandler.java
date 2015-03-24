/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.netimen.annotations.helpers.Module;
import com.netimen.annotations.EBeanCustomScope;
import com.netimen.annotations.MethodNames;
import com.netimen.annotations.androidannotationsfix.EBeanHolderFix;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;

import org.androidannotations.handler.BaseGeneratingAnnotationHandler;
import org.androidannotations.holder.EBeanHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;
import org.androidannotations.process.ProcessHolder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JMod.PUBLIC;
import static com.sun.codemodel.JMod.STATIC;

/**
 * couldn't extend EBeanHolder, so just copied some code
 */
public class EBeanCustomScopeHandler extends BaseGeneratingAnnotationHandler<EBeanHolder> {
    public EBeanCustomScopeHandler(ProcessingEnvironment processingEnvironment) {
        super(EBeanCustomScope.class, processingEnvironment);
    }

    @Override
    public EBeanHolder createGeneratedClassHolder(ProcessHolder processHolder, TypeElement annotatedComponent) throws Exception {
        return new EBeanHolderFix(processHolder, annotatedComponent);
    }

    @Override
    public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        super.validate(element, validatedElements, valid);

        validatorHelper.isNotPrivate(element, valid);

        validatorHelper.isAbstractOrHasEmptyOrContextConstructor(element, valid);
    }

    @Override
    public void process(Element element, EBeanHolder holder) throws Exception {

        final JDefinedClass generatedClass = holder.getGeneratedClass();
        generatedClass.constructors().next().body().staticInvoke(holder.refClass(Module.class), MethodNames.MODULE_SET).arg(generatedClass.dotclass()).arg(_this());
        holder.invokeInitInConstructor();

        JMethod getMethod = generateFactoryMethod(holder, EBeanHolder.GET_INSTANCE_METHOD_NAME);
        getMethod.body()._return(holder.refClass(Module.class).staticInvoke(MethodNames.MODULE_GET).arg(generatedClass.dotclass()));

        JMethod initMethod = generateFactoryMethod(holder, MethodNames.INIT_INSTANCE);
        initMethod.body()._return(_new(generatedClass).arg(initMethod.listParams()[0]));
    }

    JMethod generateFactoryMethod(EBeanHolder holder, String methodName) {
        JMethod getMethod = holder.getGeneratedClass().method(PUBLIC | STATIC, holder.getGeneratedClass(), methodName);
        getMethod.param(classes().CONTEXT, "context");
        return getMethod;
    }
}
