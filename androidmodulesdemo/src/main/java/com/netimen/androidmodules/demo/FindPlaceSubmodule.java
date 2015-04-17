/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

@EBean
public class FindPlaceSubmodule extends Submodule {

    @ViewById
    SearchView search;

    @ViewById
    View clearAll;

    @RootContext
    Context context;

    private Geocoder geocoder;

    @AfterViews
    void ready() {
        geocoder = new Geocoder(context);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (addPlace(query) != null)
                    clearAll.setEnabled(true);
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
            getMap().addMarker(new MarkerOptions().position(position).title(address.getLocality()));
            getMap().animateCamera(CameraUpdateFactory.newLatLng(position));
            return address;
        } catch (IOException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Click
    void clearAll() {
        getMap().clear();
        clearAll.setEnabled(false);
    }
}
