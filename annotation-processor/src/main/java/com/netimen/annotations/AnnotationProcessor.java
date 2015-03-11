package com.netimen.annotations;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessor extends AndroidAnnotationProcessorFix {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        annotationHandlers.add(new EventHandler(processingEnv));
    }

//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        catch (Exception e) {
//            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
//        }
//        return false;
//    }
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "AAAA");
//        for (Element elem : roundEnv.getElementsAnnotatedWith(Event.class)) {
//            Event complexity = elem.getAnnotation(Event.class);
//            String message = "annotation found in " + elem.getSimpleName()
//                    + " with complexity " ;
//            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
//            if (elem.getKind() == ElementKind.CLASS) {
//                TypeElement classElement = (TypeElement) elem;
//                PackageElement packageElement =
//                        (PackageElement) classElement.getEnclosingElement();
//
//                try {
////                    processingEnv.getFiler().getResource()
//                    final String srcName = String.valueOf(classElement.getSimpleName());
//                    if (srcName.endsWith("_"))
//                        continue;
//                    final String suffix = "_";
//                    final String name = srcName + suffix;
//                    JavaFileObject jfo = processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + suffix);
//                    BufferedWriter bw = new BufferedWriter(jfo.openWriter());
//                    bw.append("package ");
//                    bw.append(packageElement.getQualifiedName());
//                    bw.append(";");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.append("class ").append(name).append(" extends ").append(srcName).append(" {}");
//                    bw.close();
//                } catch (IOException e) {
//                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, e.toString());
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        return true; // no further processing of this annotation type
//    }

    @Override
    public Set<String> getSupportedAnnotationTypes() { // CUR
        final Set<String> supportedAnnotationTypes = new HashSet<>(super.getSupportedAnnotationTypes());
        supportedAnnotationTypes.add("netimen.com.annotations.Event");
        return supportedAnnotationTypes;
    }
}
