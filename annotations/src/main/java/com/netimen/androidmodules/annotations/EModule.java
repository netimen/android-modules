/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   13.03.15
 */
package com.netimen.androidmodules.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The main annotation of the library. Used when we have some activity or fragment performing several almost independent tasks, but using the same UI for this. For example, when we have some fragment with WebView or MapView performing many tasks on it. This annotation helps to split this code into several files, providing better code readability, maintainability and reuse.
 * <p>
 * Currently the class MUST be an enhanced compononent (so annotated with {@link org.androidannotations.annotations.EBean}, {@link org.androidannotations.annotations.EFragment} etc). Sumbodules MUST be classes annotated with {@link org.androidannotations.annotations.EBean}. Submodules lifecycle is tied to the module lifecycle.
 * </p>
 * <p>
 * The submodules are supposed to avoid direct calling each other's methods and to communicate via {@link com.netimen.androidmodules.helpers.Bus} instead.
 * </p>
 * <pre>
 * &#064;EActivity(R.layout.activity_maps)
 * &#064;EModule(submodules = {FindPlaceSubmodule.class, CalcDistanceSubmodule.class})
 * public class MapsActivity {
 * }
 * </pre>
 * In the example above we can now put the all the code related to place searching into FindPlaceSubmodule.java and all the code related to distance calculation into CalcDistanceSubmodule.java. Since all the code is now split into logical units, we have the following benefits:
 * <ol>
 *     <li>Code readability and maintainability is improved</li>
 *     <li>Code reuse</li>
 *     <li>We can easily combine submodules, replace them or turn them off, so we can provide several "flavors" of the functionality</li>
 * </ol>
 */
@SuppressWarnings("UnusedDeclaration")
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface EModule {
    String moduleName() default "";
    Class<?> [] submodules() default {};
}
