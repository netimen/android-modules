/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.bookmate.bus;

import java.util.Map;
import java.util.WeakHashMap;

public class BusProvider {

    private static final Map<Object, Bus> buses = new WeakHashMap<>();

    public static Bus getBus(Object key) {
        Bus bus = buses.get(key);
        if (bus == null)
            bus = buses.put(key, new Bus());
        return bus;
    }
}
