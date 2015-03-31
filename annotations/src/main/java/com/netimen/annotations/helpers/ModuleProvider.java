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
public class ModuleProvider {

    public static interface IModule {

    }

    private static final Map<String, WeakReference<InstancesHolder>> modules = new HashMap<>();
    private static WeakReference<InstancesHolder> currentModule = null;
    private static String currentModuleName = "";

    public static class InstancesHolder {
        private final Map<Object, Object> accessibleInstances = new HashMap<>();
        private Object[] inaccessibleInstances; // just hold these objects, they communicate by Bus;

        private InstancesHolder() {
        }

        /**
         * we don't initialize these in constructor because we need this objects to be created later than this InstanceHolder, because they will use it.
         */
        public void setInaccessibleInstances(Object[] inaccessibleInstances) {
            this.inaccessibleInstances = inaccessibleInstances;
        }

        private <T> T set(Class<T> cls, T instance) {
            accessibleInstances.put(cls, instance);
            return instance;
        }

        @SuppressWarnings("unchecked")
        private <T> T get(Class<T> cls) {
            return (T) accessibleInstances.get(cls);
        }

        private void clear() {
            accessibleInstances.clear();
        }
    }

    private static InstancesHolder getInstanceHolder(String moduleName) {
        final WeakReference<InstancesHolder> moduleWeakReference = isEmpty(moduleName) ? currentModule : modules.get(moduleName);
        return moduleWeakReference == null ? null : moduleWeakReference.get();
    }

    private static <T> T setInstance(InstancesHolder instancesHolder, Class<T> cls, T instance) {
        return instancesHolder == null ? null : instancesHolder.set(cls, instance);
    }

    private static <T> T getInstance(InstancesHolder instancesHolder, Class<T> cls) {
        return instancesHolder == null ? null : instancesHolder.get(cls);
    }

    public static <T> T setInstance(Class<T> cls, T instance) {
        return setInstance(getInstanceHolder(null), cls, instance);
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

        currentModuleName = moduleName;
        modules.put(moduleName, currentModule);
        return instancesHolder;
    }

    public static boolean moduleExists(String moduleName) {
        return getInstanceHolder(moduleName) != null;
    }

    public static void clearModules() {
        modules.clear();
        currentModuleName = "";
        currentModule = null;
    }

    ///

    private static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

}
