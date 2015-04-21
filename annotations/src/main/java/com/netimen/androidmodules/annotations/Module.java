/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.androidmodules.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used in submodules to inject instance of the class annotated with {@link EModule}}. It's like {@link org.androidannotations.annotations.App}.
 *
 * <pre>
 * &#064;EBean
 * public class Submodule {
 *
 * 	&#064;Module
 * 	SomeModuleParentClass module; // here the type of the variable can be the actual module class or one of its parents or interfaces
 * }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Module {
}
