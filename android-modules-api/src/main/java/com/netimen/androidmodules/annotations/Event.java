/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   11.03.15
 */
package com.netimen.androidmodules.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to subscribe to an event. By default, it uses the {@link com.netimen.androidmodules.helpers.Bus} instance stored in the class annotated with {@link EModule} with the help of {@link com.netimen.androidmodules.helpers.ModuleObjectsShare}, so every submodule uses the same bus.
 *
 * <pre>
 * &#064;EBean
 * public class Submodule {
 *
 * 	&#064;Event
 * 	void someEvent() { // subscribes to SomeEvent event, event name is inferred from method name
 * 	}
 *
 * 	&#064;Event
 * 	void onOtherEvent() { // subscribes to OtherEvent event, event name is inferred from method name, 'on' is omitted
 * 	}
 *
 * 	&#064;Event
 * 	void thirdEventHappened(ThirdEvent event) { // subscribes to ThirdEvent event, event name is inferred from parameter; method name doesn't matter
 * 	}
 *
 *
 * 	&#064;Event(FourthEvent.class)
 * 	void fourthEventHappened() { // subscribes to FourthEvent event
 * 	}
 *
 * 	&#064;Event(moduleClass = {FragmentOne.class, FragmentTwo.class})
 * 	void fithEvent() { // subscribes to FithEvent event, coming from submodules of modules FragmentOne or FragmentTwo. May be useful in the activity if it has several modularized fragment.
 * 	}
 * }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@SuppressWarnings("UnusedDeclaration")
public @interface Event {
    Class<?> value() default Object.class;
    String[] moduleName() default "";
    Class<?>[] moduleClass() default Object.class;

    public static String ANY_MODULE = "_any_module_";
}
