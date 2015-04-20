/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo.submodules;

import android.location.Address;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.demo.MapUtils;
import com.netimen.androidmodules.demo.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

@EBean
public class FindPlaceSubmodule extends BaseMapSubmodule {

    @ViewById
    SearchView search;

    @AfterViews
    void ready() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (addPlace(query) != null)
                    enableClearAll(true);
                else
                    Toast.makeText(context, R.string.location_not_found, Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private Address addPlace(String locationName) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            final Address address = addresses.get(0);
            final LatLng position = new LatLng(address.getLatitude(), address.getLongitude());
            getMap().addMarker(new MarkerOptions().position(position).title(MapUtils.getAddressString(address)));
            getMap().animateCamera(CameraUpdateFactory.newLatLng(position));
            return address;
        } catch (IOException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * we want soft keyboard to be hidden when user touches the map. But {@link CalcDistanceSubmodule} already added an OnMapClickListener, so we use the event system to get a touch notification from that submodule.
     * Actually there are other design patterns for this simple case: for instance we could just add a listener in a base class {@link BaseMapSubmodule}
     */
    @Event
    void onMapTouched() {
        search.clearFocus();
    }

    /**
     * Like in Android Annotations library there is no need to specify the event directly - Android Modules tries to guess it from the method name.
     */
    @Event
    void clearMap() {
        search.clearFocus();
    }

}
