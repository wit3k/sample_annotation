// It was my initial idea to generate new classes and methods at (pre)compile time and then execute generated code
// following Micronaut approach. Finally I switched to runtime reflection because it's much easier to implement

package com.sample.annotation;

import org.example.annotation.ScheduleEveryFiveMins;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@javax.annotation.processing.SupportedAnnotationTypes("org.example.annotation.ScheduleEveryFiveMins")
@javax.annotation.processing.SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_23)
public class ScheduleEveryFiveMinsProcessor extends AbstractProcessor {

    private final Map<String, java.lang.Runnable> scheduledMethods = new ConcurrentHashMap();
    private final Set<Runnable> scheduledMethods2 = new ConcurrentSkipListSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // Initialization code, if needed
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("Process annotations");


        for (Element element : roundEnv.getElementsAnnotatedWith(ScheduleEveryFiveMins.class)) {

            System.out.println("Annotation element: "
                    + "\nkind: " + element.getKind()
                    + "\nsimple name: " + element.getSimpleName()
                    + "\nenclosing: " + element.getEnclosingElement().toString()
                    + "\n" + element.asType().getClass()
            );
            // Process each element
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing: " + element.getSimpleName());

            // Your processing logic here
            //if (element.getKind() == ElementKind.METHOD) {
                this.scheduledMethods2.add(new Runnable(element.getEnclosingElement().toString(), element.getSimpleName().toString()));
            this.scheduledMethods2.forEach(runnable -> {
                System.out.println(runnable.methodName);
            });


            System.out.println(this.scheduledMethods2.toString());
            //}
        }
        return true; // No further processing of this annotation type
    }

    private record Runnable(String className, String methodName) implements Comparable<Runnable> {
        @Override
        public int compareTo(Runnable o) {
            if (o.className().compareTo(className) == 0) {
                return o.methodName().compareTo(methodName);
            } else {
                return o.className().compareTo(className);
            }
        }
    }

}