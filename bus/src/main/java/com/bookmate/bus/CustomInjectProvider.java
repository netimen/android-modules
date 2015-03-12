/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.bookmate.bus;

import java.util.HashMap;
import java.util.Map;

public class CustomInjectProvider {
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    public static <T> void set(Class<T> cls, T instance) {
        instances.put(cls, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> cls) {
        return (T) instances.get(cls);
    }
}
