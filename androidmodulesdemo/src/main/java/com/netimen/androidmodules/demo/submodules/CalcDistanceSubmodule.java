/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo.submodules;

import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.demo.R;
import com.netimen.androidmodules.demo.events.ClearMap;
import com.netimen.androidmodules.demo.events.MapTouched;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;
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

    @ViewById
    ImageButton toggleRuler;

    @AfterViews
    void ready() {
        getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bus.event(new MapTouched());

                if (polyline == null)
                    return;

                final List<LatLng> points = polyline.getPoints(); // I'm not very good with Google Maps so may be there is a better way to add new point to Polyline
                points.add(latLng);
                polyline.setPoints(points);
                getMap().addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    @Click
    void toggleRuler() {
        if (polyline == null) {
            polyline = getMap().addPolyline(new PolylineOptions().color(accent).width(rulerWidth));
            toggleRuler.setImageResource(R.drawable.abc_ic_clear_mtrl_alpha);
        } else {
            polyline.remove();
            clearRuler();
        }
    }

    @Event(ClearMap.class)
    void clearRuler() {
        polyline = null;
        toggleRuler.setImageResource(R.drawable.ruler);
    }
} // CUR circle in start, better cross icon, show clear button when adding points, calc distance
