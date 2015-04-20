/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo.events;

import com.google.android.gms.maps.model.LatLng;

/**
 * needed to pass data about map touch between modules
 */
public class MapTouched {
    public final LatLng latLng;

    public MapTouched(LatLng latLng) {

        this.latLng = latLng;
    }
}
