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
import com.google.android.gms.maps.model.Marker;
import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.demo.events.MarkerRemoved;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionRes;

/**
 * when we touch a marker there appear two standard buttons in the right-corner of the screen just where our action button is placed. We want our button to move a bit upwards in this case
 * we don't want to mix this logic with other code, so we create a separate submodule for this
 */
@EBean
public class AnimateActionButtonSubmodule extends BaseMapSubmodule {

    @ViewById
    ImageButton toggleRuler;

    @DimensionRes
    float markerButtonsHeightGuess;

    /**
     * stores the button normal poisition y (when no marker is selected).
     */
    private float toggleRulerNormalPositionY = -1;

    /**
     * we store the selected marker for later check if it was removed.
     */
    private Marker selectedMarker;

    @AfterViews
    void ready() {
        getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                evaluateInitialY();

                selectedMarker = marker;
                toggleRuler.animate().y(toggleRulerNormalPositionY - markerButtonsHeightGuess).setStartDelay(0);
                return false; // we also want default processing: it shows title and action buttons
            }
        });
    }

    /**
     * We need to put our action button down again when marker is no more selected. This happens when user touches a button
     * But {@link CalcDistanceSubmodule} already added an OnMapClickListener, so we use the event system to get a touch notification from that submodule.
     * Actually there are other design patterns for this simple case: for instance we could just add a listener in a base class {@link BaseMapSubmodule}
     */
    @Event
    void onMapTouched() {
        returnButtonToNormalPosition();
    }

    @Event
    void onClearMap() {
        returnButtonToNormalPosition();
    }

    @Event
    void onMarkerRemoved(MarkerRemoved event) {
        if (event.marker.equals(selectedMarker))
            returnButtonToNormalPosition();
    }

    private void returnButtonToNormalPosition() {
        evaluateInitialY();

        selectedMarker = null;
        toggleRuler.animate().y(toggleRulerNormalPositionY).setStartDelay(300); // start delay is to wait while marker buttons hide
    }

    /**
     * we use lazy evaluation to calculate the normal position y (other option use some LayoutChange listener in ready())
     */
    private void evaluateInitialY() {
        if (toggleRulerNormalPositionY < 0) // happens on first time only
            toggleRulerNormalPositionY = toggleRuler.getY();
    }

}
