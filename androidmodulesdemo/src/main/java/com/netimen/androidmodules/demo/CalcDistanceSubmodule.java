/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionRes;

import java.util.List;

@EBean
public class CalcDistanceSubmodule extends Submodule {
    private Polyline polyline;

    @ColorRes
    int accent;

    @DimensionRes
    float rulerWidth;

    @AfterViews
    void ready() {
        getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (polyline == null)
                    return;

                final List<LatLng> points = polyline.getPoints();
                points.add(latLng);
                polyline.setPoints(points);
            }
        });
    }

    @Click
    void calcDistance() {
        polyline = getMap().addPolyline(new PolylineOptions().color(accent).width(rulerWidth));
    }
}
