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
    private final Map<Class<?>, WeakReference<?>> instances = new HashMap<>();
    private static final Map<String, Module> modules = new HashMap<>();
    private static Module currentModule = new Module();

    private Module() {

    }

    private <T> T set(Class<T> cls, T instance) {
        instances.put(cls, new WeakReference<>(instance));
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> T get(Class<T> cls) {
        return (T) instances.get(cls).get();
    }

    private static <T> T setInstance(Module module, Class<T> cls, T instance) {
        return module.set(cls, instance);
    }

    private static <T> T getInstance(Module module, Class<T> cls) {
        return module.get(cls);
    }

    public static <T> T setInsatnce(Class<T> cls, T instance) {
        return setInstance(currentModule, cls, instance);
    }

    public static <T> T getInstance(Class<T> cls) {
        return getInstance(currentModule, cls);
    }

    public static <T> T getInstance(String moduleName, Class<T> cls) {
        return getInstance(modules.get(moduleName), cls);
    }

    public static void initModule(String moduleName) {
        currentModule = new Module();
        if (moduleName != null && moduleName.length() != 0)
            modules.put(moduleName, currentModule);
    }

    public static boolean moduleExists(String moduleName) {
        return modules.get(moduleName) != null;
    }
}
