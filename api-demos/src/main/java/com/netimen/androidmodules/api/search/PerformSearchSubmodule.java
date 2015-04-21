/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.androidmodules.api.search;

import android.widget.Button;

import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.api.BaseSubmodule;
import com.netimen.androidmodules.api.events.GetQuery;
import com.netimen.androidmodules.api.events.InputChanged;
import com.netimen.androidmodules.api.events.Search;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;


@EBean
public class PerformSearchSubmodule extends BaseSubmodule {
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
