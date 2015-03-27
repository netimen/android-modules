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
public class ModuleInstancesHolder {

    private static final Map<String, WeakReference<ModuleInstancesHolder>> modules = new HashMap<>();
    private static final ModuleInstancesHolder defaultModule = new ModuleInstancesHolder();
    private static WeakReference<ModuleInstancesHolder> currentModule = new WeakReference<>(defaultModule);
    private static String currentModuleName = "";

    private final Map<Class<?>, WeakReference<?>> instances = new HashMap<>();

    private ModuleInstancesHolder() {

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

    private void clear() {
        instances.clear();
    }

    private static ModuleInstancesHolder getModule(String moduleName) {
        final WeakReference<ModuleInstancesHolder> moduleWeakReference = modules.get(moduleName);
        return moduleWeakReference == null ? null : moduleWeakReference.get();
    }

    private static <T> T setInstance(ModuleInstancesHolder moduleInstancesHolder, Class<T> cls, T instance) {
        return moduleInstancesHolder == null ? null : moduleInstancesHolder.set(cls, instance);
    }

    private static <T> T getInstance(ModuleInstancesHolder moduleInstancesHolder, Class<T> cls) {
        return moduleInstancesHolder == null ? null : moduleInstancesHolder.get(cls);
    }

    public static <T> T setInstance(Class<T> cls, T instance) {
        return setInstance(currentModule.get(), cls, instance);
    }

    public static <T> T getInstance(Class<T> cls) {
        return getInstance(currentModule.get(), cls);
    }

    public static <T> T getInstance(String moduleName, Class<T> cls) {
        return getInstance(getModule(moduleName), cls);
    }


    /**
     * this method creates a new module if the moduleName is empty or if currentModuleName differs from moduleName. It recreates any previous existing module with the same name (except current).
     * We reset instances by default because we don't know whether the module with same name is dead or not: WeakReference.get() may return not-null, but all the actual references are already dead.
     */
    public static ModuleInstancesHolder initModule(String moduleName) {
        return initModule(moduleName, true);
    }

    public static ModuleInstancesHolder initModule(String moduleName, boolean reset) {
        final boolean moduleNameNotEmpty = moduleName != null && moduleName.length() != 0;
        ModuleInstancesHolder existingModuleInstancesHolder = getModule(moduleName);
        if (existingModuleInstancesHolder != null && currentModuleName.equals(moduleName) && moduleNameNotEmpty) // if module name is empty we always create a new module
            return existingModuleInstancesHolder;

        if (existingModuleInstancesHolder == null)
            existingModuleInstancesHolder = new ModuleInstancesHolder();
        if (reset)
            existingModuleInstancesHolder.clear();
        currentModule = new WeakReference<>(existingModuleInstancesHolder);

        if (moduleNameNotEmpty) {
            currentModuleName = moduleName;
            modules.put(moduleName, currentModule);
        }
        return existingModuleInstancesHolder;
    }

    public static boolean moduleExists(String moduleName) {
        return getModule(moduleName) != null;
    }

    public static void clearModules() {
        modules.clear();
        currentModuleName = "";
        defaultModule.clear();
        currentModule = new WeakReference<>(defaultModule);
    }

}
