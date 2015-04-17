/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo.submodules;

import com.netimen.androidmodules.demo.events.ClearMap;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;

/**
 * the clear functionality is common for both FindPlace and CalcDistance submodules, because it clears the markers as well as the ruler. So we put it into a differnt submodule to keep the logic clear
 */
@EBean
public class ClearMapSubmodule extends BaseMapSubmodule {

    @Click
    void clearAll() {
        bus.event(new ClearMap());
        getMap().clear();
        enableClearAll(false);
    }
}
