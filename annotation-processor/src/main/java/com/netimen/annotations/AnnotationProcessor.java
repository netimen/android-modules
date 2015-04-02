package com.netimen.annotations;

import com.netimen.annotations.androidannotationsfix.AndroidAnnotationProcessorFix;
import com.netimen.annotations.handlers.EModuleHandler;
import com.netimen.annotations.handlers.EventHandler;
import com.netimen.annotations.handlers.InjectHandler;
import com.netimen.annotations.handlers.ModuleHandler;
import com.netimen.annotations.handlers.RequestHandler;

import org.androidannotations.handler.AnnotationHandler;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.GeneratedClassHolder;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessor extends AndroidAnnotationProcessorFix {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

//        hackEnhancedComponentsList();

        // we need @Bean accept @EBeanCustomScope, so replace standard handler with ours
//        final BeanHandlerFix beanHandlerFix = new BeanHandlerFix(processingEnv);
//        replaceHandlerInList(annotationHandlers.getDecorating(), beanHandlerFix);
//        replaceHandlerInList(annotationHandlers.get(), beanHandlerFix);

        addDecoratingHandler(new InjectHandler(processingEnv));
        addDecoratingHandler(new EventHandler(processingEnv));
        addDecoratingHandler(new RequestHandler(processingEnv));
        addDecoratingHandler(new ModuleHandler(processingEnv));
        addDecoratingHandler(0, new EModuleHandler(processingEnv));
    }

//    private void hackEnhancedComponentsList() {
//        final ArrayList<Class<? extends Annotation>> newEnhancedComponentsList = new ArrayList<>(ModelConstants.VALID_ENHANCED_COMPONENT_ANNOTATIONS);
//        newEnhancedComponentsList.add(EBeanCustomScope.class);
//        try {
//            setFinalStatic(ModelConstants.class.getField("VALID_ENHANCED_COMPONENT_ANNOTATIONS"), newEnhancedComponentsList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    void replaceHandlerInList(List<AnnotationHandler<? extends GeneratedClassHolder>> handlers, AnnotationHandler<? extends GeneratedClassHolder> handlerFix) {
//        for (int i = 0; i < handlers.size(); i++)
//            if (handlers.get(i) instanceof BeanHandler) {
//                handlers.set(i, handlerFix);
//                break;
//            }
//    }
//
//    static void setFinalStatic(Field field, Object newValue) throws Exception {
//        field.setAccessible(true);
//
//        Field modifiersField = Field.class.getDeclaredField("modifiers");
//        modifiersField.setAccessible(true);
//        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//
//        field.set(null, newValue);
//    }
    private void addDecoratingHandler(int position, BaseAnnotationHandler<? extends GeneratedClassHolder> handler) {
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
