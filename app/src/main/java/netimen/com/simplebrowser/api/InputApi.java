/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.simplebrowser.api;

import org.androidannotations.annotations.EBean;

import netimen.com.annotations.Event;

@EBean
public class InputApi extends BaseApi {

    @Event
    void onPageShown() {
        api.event(new ItemLoaded());
    }

}
