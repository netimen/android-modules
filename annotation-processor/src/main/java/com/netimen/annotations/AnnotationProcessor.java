package com.netimen.annotations;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessor extends AndroidAnnotationProcessorFix {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        annotationHandlers.add(new EventHandler(processingEnv));
        annotationHandlers.add(new CustomInjectHandler(processingEnv));
    }
}
