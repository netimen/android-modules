/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.bookmate.bus;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class InjectInstanceProvider {
    private static final Map<Class<?>, WeakReference<?>> instances = new HashMap<>();

    public static <T> T set(Class<T> cls, T instance) {
        instances.put(cls, new WeakReference<>(instance));
        return instance;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> cls) {
        return (T) instances.get(cls).get();
    }
}
