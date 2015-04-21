/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.apidemos.submodules;

import android.content.Context;
import android.location.Geocoder;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;
import com.netimen.androidmodules.annotations.Inject;
import com.netimen.androidmodules.apidemos.R;
import com.netimen.androidmodules.helpers.Bus;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

/**
 * contains some basic stuff used by all the submodules
 */
@EBean
public abstract class BaseMapSubmodule {

    @FragmentById(R.id.map)
    SupportMapFragment mapFragment;

    @ViewById
    View clearAll;

    @RootContext
    Context context;

    /**
     * used for communication between submodules. The library makes sure that all submodules use same instance of Bus.
     */
    @Inject
    Bus bus;

    protected Geocoder geocoder;

    @AfterInject
    void createGeocoder() {
        geocoder = new Geocoder(context);
    }

    protected com.google.android.gms.maps.GoogleMap getMap() {
        return mapFragment.getMap();
    }

    protected void enableClearAll(boolean enable) {
       clearAll.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
}
