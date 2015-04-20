/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   20.04.15
 */
package com.netimen.androidmodules.demo.events;

import com.google.android.gms.maps.model.Marker;

public class MarkerRemoved {
    public final Marker marker;

    public MarkerRemoved(Marker marker) {
        this.marker = marker;
    }
}
