/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   20.03.15
 */
package com.netimen.androidmodules.helpers;

import com.sun.codemodel.JClass;

import org.androidannotations.helper.OptionsHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

/**
 * needed to guess class package by name. The problem is that when we write @Event void onMyEvent(), the event class name is MyEvent, but we can't know the package.
 * This class tries to guess the sources dir by the manifest file name and then parses that directory looking for java classes and guesses the package name by path
 */
public class SourceHelper {
    private static Map<String, String> classNameToFullName;
    private static final Map<String, JClass> classes = new HashMap<>();

    private static void init(ProcessingEnvironment processingEnv) {
        classNameToFullName = new HashMap<>();
        final OptionsHelper optionsHelper = new OptionsHelper(processingEnv);
        final String androidManifestFile = optionsHelper.getAndroidManifestFile();
        final String srcPath = androidManifestFile.substring(0, androidManifestFile.indexOf("build")) + "src/main/java";
        final File srcDir = new File(srcPath);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Source helper. src dir: " + srcPath + " exists: " + srcDir.exists());

        final long l = System.currentTimeMillis();
        final Iterator<File> fileIterator = FileUtils.iterateFiles(srcDir, new String[]{"java"}, true);
        while (fileIterator.hasNext()) {
            final File file = fileIterator.next();
            final String absolutePath = file.getAbsolutePath();
            classNameToFullName.put(FilenameUtils.removeExtension(file.getName()), absolutePath.substring(srcPath.length() + 1, absolutePath.length() - 5).replace(File.separatorChar, '.')); // result: com.bookmate.events.IsPublic
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Source helper processed " + classNameToFullName.size() + " classes in " + (System.currentTimeMillis() - l) + " ms");
    }

    public static Map<String, String> getClassesFullNameMap(ProcessingEnvironment processingEnv) {
        if (classNameToFullName == null)
            init(processingEnv);
        return classNameToFullName;
    }

    public static JClass getClass(String className) {
        return classes.get(className);
    }

    public static void addClass(String className, JClass jClass) {
        classes.put(className, jClass);
    }
}
