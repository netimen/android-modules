/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo.submodules;

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.demo.MapUtils;
import com.netimen.androidmodules.demo.R;
import com.netimen.androidmodules.demo.events.ClearMap;
import com.netimen.androidmodules.demo.events.MapTouched;
import com.netimen.androidmodules.demo.events.MarkerRemoved;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionRes;

import java.util.ArrayList;
import java.util.List;

@EBean
public class CalcDistanceSubmodule extends BaseMapSubmodule {

    @ColorRes
    int accent;

    @DimensionRes
    float rulerWidth;

    @ViewById
    ImageButton toggleRuler;

    @ViewById
    TextView distance;

    private Polyline polyline;
    private final List<Marker> markers = new ArrayList<>();

    @Event
    void mapTouched(MapTouched event) {
        if (polyline == null) // means that we aren't currently editing the ruler
            return;

        markers.add(getMap().addMarker(new MarkerOptions().position(event.latLng).title(MapUtils.getLocationAddress(geocoder, event.latLng)))); // creates a marker for each point

        MapUtils.addPolylinePoint(polyline, event.latLng); // adds point tuo the ruler

        if (polyline.getPoints().size() > 1)
            distance.setText(context.getResources().getString(R.string.distance, MapUtils.calcTotalDistance(polyline)));

        enableClearAll(true);
    }

    @Click
    void toggleRuler() {
        if (polyline == null) {
            polyline = getMap().addPolyline(new PolylineOptions().color(accent).width(rulerWidth));
            toggleRuler.setImageResource(R.drawable.abc_ic_clear_mtrl_alpha);
        } else {
            polyline.remove();
            for (Marker marker : markers) {
                bus.event(new MarkerRemoved(marker));
                marker.remove();
            }
            clearRuler();
        }
    }

    @Event(ClearMap.class)
    void clearRuler() {
        polyline = null;
        markers.clear();
        distance.setText(null);
        toggleRuler.setImageResource(R.drawable.ruler);
    }
}
