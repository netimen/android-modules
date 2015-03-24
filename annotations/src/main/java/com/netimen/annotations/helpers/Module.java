/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.annotations.helpers;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * inspired by Dagger's module concept
 */
@SuppressWarnings("UnusedDeclaration")
public class Module {

    private static final Map<String, Module> modules = new HashMap<>();
    private static Module currentModule = new Module();
    private static String currentModuleName = "";

    private final Map<Class<?>, WeakReference<?>> instances = new HashMap<>();

    private Module() {

    }

    private <T> T set(Class<T> cls, T instance) {
        instances.put(cls, new WeakReference<>(instance));
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> T get(Class<T> cls) {
        final WeakReference<?> reference = instances.get(cls);
        return reference == null ? null : (T) reference.get();
    }

    private static <T> T setInstance(Module module, Class<T> cls, T instance) {
        return module.set(cls, instance);
    }

    private static <T> T getInstance(Module module, Class<T> cls) {
        return module.get(cls);
    }

    public static <T> T setInstance(Class<T> cls, T instance) {
        return setInstance(currentModule, cls, instance);
    }

    public static <T> T getInstance(Class<T> cls) {
        return getInstance(currentModule, cls);
    }

    public static <T> T getInstance(String moduleName, Class<T> cls) {
        return getInstance(modules.get(moduleName), cls);
    }

    /**
     * this method creates a new module if the moduleName is empty or if currentModuleName differs from moduleName. It recreates any previous existing module with the same name (except current).
     */
    public static void initModule(String moduleName) {
        final boolean moduleNameNotEmpty = moduleName != null && moduleName.length() != 0;
        if (currentModuleName.equals(moduleName) && moduleNameNotEmpty) // if module name is empty we always create a new module
            return;

        currentModule = new Module();
        if (moduleNameNotEmpty) {
            currentModuleName = moduleName;
            modules.put(moduleName, currentModule);
        }
    }

    public static boolean moduleExists(String moduleName) {
        return modules.get(moduleName) != null;
    }

    public static void clearModules() {
        modules.clear();
        currentModuleName = "";
        currentModule = new Module();
    }
}
