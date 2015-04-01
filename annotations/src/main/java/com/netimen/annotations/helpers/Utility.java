/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   01.04.15
 */
package com.netimen.annotations.helpers;

public class Utility {
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
