/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api.events;

public class NumberChanged {

    public final boolean isEmpty;

    public NumberChanged(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
