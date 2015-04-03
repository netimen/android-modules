/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   11.03.15
 */
package com.netimen.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@SuppressWarnings("UnusedDeclaration")
public @interface Event {
    Class<?> value() default Object.class;
    String[] moduleName() default "";
    Class<?>[] moduleClass() default Object.class;

    public static String ANY_MODULE = "_any_module_";
}
