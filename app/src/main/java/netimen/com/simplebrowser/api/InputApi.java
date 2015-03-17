/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.simplebrowser.api;

import com.netimen.annotations.Event;
import com.netimen.annotations.Request;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class InputApi extends BaseApi {

    @Event
    void onPageShown() {
        bus.event(new ItemLoaded());
    }

    @Request
    List<Boolean> arePublic() {
        return new ArrayList<>();
    }

    @Request
    Boolean isPublic() {
        return true;
    }

    @Request
    boolean onIsPublic() {
        return false;
    }
}
