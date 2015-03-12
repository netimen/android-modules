package com.netimen.annotations;

import org.androidannotations.handler.AnnotationHandler;
import org.androidannotations.holder.GeneratedClassHolder;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessor extends AndroidAnnotationProcessorFix {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        add(new EventHandler(processingEnv));
        add(new CustomInjectHandler(processingEnv));
        add(0, new CustomInjectInstanceHandler(processingEnv)); // should go before BeanHandler, so beans could use CustomInject correctly
    }

    private void add(int position, CustomInjectInstanceHandler handler) {
       annotationHandlers.get().add(position, handler);
        annotationHandlers.getDecorating().add(position, handler);
    }

    private void add(AnnotationHandler<? extends GeneratedClassHolder> handler) {
        addToList(annotationHandlers.get(), handler);
        addToList(annotationHandlers.getDecorating(), handler);
    }

    /**
     * the problem is that we need our handlers to go before AfterInject handler, so our code would be placed before @AfterInject is called
     */
    private void addToList(List<AnnotationHandler<? extends GeneratedClassHolder>> annotationHandlers, AnnotationHandler<? extends GeneratedClassHolder> handler) {
        annotationHandlers.add(annotationHandlers.size() - 20, handler); // ugly solution, hope AA will provide better API
    }
}
