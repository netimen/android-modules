/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.androidmodules.helpers;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * inspired by Dagger's module concept
 */
@SuppressWarnings("UnusedDeclaration")
public class ModuleProvider {

    /**
     * this interface is used for injecting the class annotated with @EModule into the submodule classes.
     */
    public interface IModule {
    }

    public static class InstancesHolder {
        private final Map<Object, Object> accessibleInstances = new HashMap<>();
        private Object[] inaccessibleInstances; // just hold these objects, they communicate by Bus;

        private InstancesHolder() {
        }

        private <T> T set(Class<T> cls, T instance) {
            accessibleInstances.put(cls, instance);
            return instance;
        }

        @SuppressWarnings("unchecked")
        private <T> T get(Class<T> cls) {
            return (T) accessibleInstances.get(cls);
        }

    }

    private static final Map<String, WeakReference<InstancesHolder>> modules = new HashMap<>();
    private static WeakReference<InstancesHolder> currentModule = null;

    private static InstancesHolder getInstanceHolder(String moduleName) {
        final WeakReference<InstancesHolder> moduleWeakReference = Utility.isEmpty(moduleName) ? currentModule : modules.get(moduleName);
        return moduleWeakReference == null ? null : moduleWeakReference.get();
    }

    private static <T> T setInstance(InstancesHolder instancesHolder, Class<T> cls, T instance) {
        return instancesHolder == null ? null : instancesHolder.set(cls, instance);
    }

    private static <T> T getInstance(InstancesHolder instancesHolder, Class<T> cls) {
        return instancesHolder == null ? null : instancesHolder.get(cls);
    }

    public static <T> T setInstance(Class<T> cls, T instance) {
        return setInstance("", cls, instance);
    }

    public static <T> T setInstance(String moduleName, Class<T> cls, T instance) {
        return setInstance(getInstanceHolder(moduleName), cls, instance);
    }

    public static <T> T getInstance(Class<T> cls) {
        return getInstance("", cls);
    }

    public static <T> T getInstance(String moduleName, Class<T> cls) {
        return getInstance(getInstanceHolder(moduleName), cls);
    }

    public static InstancesHolder createModule(String moduleName) {
        final InstancesHolder instancesHolder = new InstancesHolder();
        currentModule = new WeakReference<>(instancesHolder);

        modules.put(moduleName, currentModule);
        return instancesHolder;
    }

    public static boolean moduleExists(String moduleName) {
        return getInstanceHolder(moduleName) != null;
    }

    public static Set<String> modulesNames() {
        return modules.keySet();
    }

    public static void clearModules() {
        modules.clear();
        currentModule = null;
    }

}
