/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.androidmodules.apidemos.events;

public class InputChanged {

    public final boolean isEmpty;
    public final String moduleName;

    public InputChanged(String moduleName, boolean isEmpty) {
        this.moduleName = moduleName;
        this.isEmpty = isEmpty;
    }
}
