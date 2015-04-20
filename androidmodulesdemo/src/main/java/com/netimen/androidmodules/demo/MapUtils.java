/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.04.15
 */
package com.netimen.androidmodules.demo;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.List;

/**
 * helper methods for some map operations
 */
public class MapUtils {
    private MapUtils() {

    }

    public static String getLocationAddress(Geocoder geocoder, LatLng latLng) {
        try {
            final List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0)
                return getAddressString(addresses.get(0));
        } catch (IOException ignored) {
        }
        return null;
    }

    public static String getAddressString(Address address) {
        StringBuilder addressString = new StringBuilder();

        for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
            addressString.append(i == 0 ? "" : ", ").append(address.getAddressLine(i));

        return addressString.toString();
    }

    public static void addPolylinePoint(Polyline polyline, LatLng latLng) {
        final List<LatLng> points = polyline.getPoints(); // I'm not very good with Google Maps so may be there is a better way to add new point to Polyline
        points.add(latLng);
        polyline.setPoints(points);
    }

    /**
     * @return distance in km
     */
    public static float calcTotalDistance(Polyline polyline) {
        float totalDistance = 0;
        for (int i = 0; i < polyline.getPoints().size() - 1; i++) {
            final LatLng point1 = polyline.getPoints().get(i);
            final LatLng point2 = polyline.getPoints().get(i + 1);
            float[] results = new float[1];
            Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results);
            totalDistance += results[0];
        }
        return totalDistance / 1000;
    }
}
