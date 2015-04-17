/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo;

import com.google.android.gms.maps.SupportMapFragment;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.FragmentById;

@EBean
public abstract class Submodule {
    @FragmentById(R.id.map)
    SupportMapFragment mapFragment;

    protected com.google.android.gms.maps.GoogleMap getMap() {
        return mapFragment.getMap();
    }
}
