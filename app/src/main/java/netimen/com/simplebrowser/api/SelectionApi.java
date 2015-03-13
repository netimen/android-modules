/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.simplebrowser.api;

import org.androidannotations.annotations.EBean;

import com.netimen.annotations.Event;

@EBean
public class SelectionApi extends BaseApi {

    @Event(ItemLoaded.class)
    void onLoaded(ItemLoaded event) {
        event.toString();
    }

}