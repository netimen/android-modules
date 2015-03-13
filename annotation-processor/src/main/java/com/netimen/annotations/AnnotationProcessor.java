package com.netimen.annotations;

import com.netimen.annotations.androidannotationsfix.AndroidAnnotationProcessorFix;
import com.netimen.annotations.androidannotationsfix.BeanHandlerFix;
import com.netimen.annotations.handlers.CustomInjectHandler;
import com.netimen.annotations.handlers.CustomInjectInstanceHandler;
import com.netimen.annotations.handlers.EBeanCustomScopeHandler;
import com.netimen.annotations.handlers.EventHandler;

import org.androidannotations.annotations.EIntentService;
import org.androidannotations.handler.AnnotationHandler;
import org.androidannotations.handler.BeanHandler;
import org.androidannotations.helper.ModelConstants;
import org.androidannotations.holder.GeneratedClassHolder;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessor extends AndroidAnnotationProcessorFix {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ModelConstants.VALID_ENHANCED_COMPONENT_ANNOTATIONS.set(ModelConstants.VALID_ENHANCED_COMPONENT_ANNOTATIONS.indexOf(EIntentService.class), EBeanCustomScope.class); // very bad, but we can't add a component, because asList doesn't support add operations, so we can only replace one of existing

        annotationHandlers.add(new EBeanCustomScopeHandler(processingEnv));

        // we need @Bean accept @EBeanCustomScope, so replace standard handler with ours
        final BeanHandlerFix beanHandlerFix = new BeanHandlerFix(processingEnv);
        replaceHandlerInList(annotationHandlers.getDecorating(), beanHandlerFix);
        replaceHandlerInList(annotationHandlers.get(), beanHandlerFix);

        addDecoratingHandler(new EventHandler(processingEnv));
        addDecoratingHandler(new CustomInjectHandler(processingEnv));
        addDecoratingHandler(0, new CustomInjectInstanceHandler(processingEnv)); // should go before BeanHandler, so beans could use CustomInject correctly
    }

    void replaceHandlerInList(List<AnnotationHandler<? extends GeneratedClassHolder>> handlers, AnnotationHandler<? extends GeneratedClassHolder> handlerFix) {
        for (int i = 0; i < handlers.size(); i++)
            if (handlers.get(i) instanceof BeanHandler) {
                handlers.set(i, handlerFix);
                break;
            }
    }

    private void addDecoratingHandler(int position, CustomInjectInstanceHandler handler) {
        annotationHandlers.get().add(position, handler);
        annotationHandlers.getDecorating().add(position, handler);
    }

    private void addDecoratingHandler(AnnotationHandler<? extends GeneratedClassHolder> handler) {
        addToEndOfList(annotationHandlers.get(), handler);
        addToEndOfList(annotationHandlers.getDecorating(), handler);
    }

    /**
     * the problem is that we need our handlers to go before AfterInject handler, so our code would be placed before @AfterInject is called
     */
    private void addToEndOfList(List<AnnotationHandler<? extends GeneratedClassHolder>> annotationHandlers, AnnotationHandler<? extends GeneratedClassHolder> handler) {
        annotationHandlers.add(annotationHandlers.size() - 20, handler); // ugly solution, hope AA will provide better API
    }
}
