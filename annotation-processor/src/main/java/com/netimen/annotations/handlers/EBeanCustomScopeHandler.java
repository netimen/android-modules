/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.annotations.handlers;

import com.bookmate.bus.CustomInjectProvider;
import com.netimen.annotations.EBeanCustomScope;
import com.netimen.annotations.androidannotationsfix.EBeanHolderFix;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JInvocation;
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
        JMethod getMethod = generateFactoryMethod(holder, EBeanHolder.GET_INSTANCE_METHOD_NAME);
        getMethod.body()._return(holder.refClass(CustomInjectProvider.class).staticInvoke("get").arg(generatedClass.dotclass())); // CUR "get"

        JMethod initMethod = generateFactoryMethod(holder, BeanInitScopeHandler.INIT_INSTANCE_METHOD_NAME);
        final JInvocation newInstance = _new(generatedClass).arg(initMethod.listParams()[0]);
        initMethod.body()._return(holder.refClass(CustomInjectProvider.class).staticInvoke("set").arg(generatedClass.dotclass()).arg(newInstance));
    }

    JMethod generateFactoryMethod(EBeanHolder holder, String methodName) {
        JMethod getMethod = holder.getGeneratedClass().method(PUBLIC | STATIC, holder.getGeneratedClass(), methodName);
        getMethod.param(classes().CONTEXT, "context");
        return getMethod;
    }
}
