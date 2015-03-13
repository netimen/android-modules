/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.simplebrowser.api;

import com.netimen.annotations.Event;

import org.androidannotations.annotations.EBean;

@EBean
public class InputApi extends BaseApi {

    @Event
    void onPageShown() {
        bus.event(new ItemLoaded());
    }

}
