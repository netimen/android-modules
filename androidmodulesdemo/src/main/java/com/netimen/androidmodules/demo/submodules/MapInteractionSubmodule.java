/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   20.04.15
 */
package com.netimen.androidmodules.demo.submodules;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.netimen.androidmodules.demo.events.ClearMap;
import com.netimen.androidmodules.demo.events.MapTouched;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;

/**
 * We need to perform many different actions when user touches map or marker. But we can add only one listener to the map, so it's logical to put it to a separate module
 * Also we need to perform many actions when user clears the map.
 * Alternatively we could put this code into the DemoActivity, but putting it in a separate module simplifies possible code reuse
 */
@EBean
public class MapInteractionSubmodule extends BaseMapSubmodule {

    @AfterViews
    void ready() {
        getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bus.event(new MapTouched(latLng));
            }
        });
    }

    @Click
    void clearAll() {
        bus.event(new ClearMap());
        getMap().clear();
        enableClearAll(false);
    }
}
