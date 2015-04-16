/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo.api.search;

import android.widget.Button;

import com.netimen.androidmodules.annotations.Event;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.BaseApi;
import netimen.com.demo.api.events.GetQuery;
import netimen.com.demo.api.events.InputChanged;
import netimen.com.demo.api.events.Search;

@EBean
public class PerformSearchApi extends BaseApi{
    @ViewById
    Button search;

    @Event
    void onInputChanged(InputChanged event) {
        search.setEnabled(!event.isEmpty);
    }

    @Click
    void search() {
        bus.event(new Search(bus.request(new GetQuery())));
    }

    @EditorAction
    void editQuery() {
        search();
    }
}
