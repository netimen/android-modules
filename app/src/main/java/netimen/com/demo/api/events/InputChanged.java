/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api.events;

public class InputChanged {

    public final boolean isEmpty;

    public InputChanged(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}