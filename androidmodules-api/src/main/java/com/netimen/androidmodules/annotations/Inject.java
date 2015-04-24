/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 * <p/>
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.androidmodules.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Used to inject objects stroed in the class annotated with {@link EModule} with the help of {@link com.netimen.androidmodules.helpers.ModuleObjectsShare}. It's like {@link org.androidannotations.annotations.Bean}, but can be used to inject ANY class, not only {@link org.androidannotations.annotations.EBean} etc.
 *
 * <pre>
 * &#064;EBean
 * public class Submodule {
 *
 * 	&#064;Inject
 * 	Bus bus;
 * }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Inject {
}
