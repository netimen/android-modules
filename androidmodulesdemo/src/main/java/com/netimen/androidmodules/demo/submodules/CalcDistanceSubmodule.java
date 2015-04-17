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
import com.google.android.gms.maps.model.Marker;
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

import java.util.ArrayList;
import java.util.List;

@EBean
public class CalcDistanceSubmodule extends Submodule {

    @ColorRes
    int accent;

    @DimensionRes
    float rulerWidth;

    @ViewById
    ImageButton toggleRuler;

    private Polyline polyline;
    private final List<Marker> markers = new ArrayList<>();

    @AfterViews
    void ready() {
        getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bus.event(new MapTouched());

                if (polyline == null) // means that we aren't currently editing the ruler
                    return;

                MapUtils.addPolylinePoint(polyline, latLng);

                markers.add(getMap().addMarker(new MarkerOptions().position(latLng).title(MapUtils.getLocationAddress(geocoder, latLng))));

                enableClearAll(true);
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
            for (Marker marker : markers)
                marker.remove();
            clearRuler();
        }
    }

    @Event(ClearMap.class)
    void clearRuler() {
        polyline = null;
        markers.clear();
        toggleRuler.setImageResource(R.drawable.ruler);
    }
} // CUR  calc distance,  move action button up on marker selected
