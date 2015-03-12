/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.bookmate.bus;

import java.lang.ref.WeakReference;

public class BusProvider {

    //    private static final Map<Object, Bus> buses = new WeakHashMap<>();
//
//    public static Bus getBus(Object key) {
//        Bus bus = buses.get(key);
//        if (bus == null) {
//            bus = new Bus();
//            buses.put(key, bus);
//        }
//        return bus;
//    }
    private static WeakReference<Bus> busRef;

    public static Bus initBus() {
        Bus bus = new Bus();
        busRef = new WeakReference<>(bus);
        return bus;
    }

    public static Bus getBus() {
        return busRef == null ? null : busRef.get();
    }
}
