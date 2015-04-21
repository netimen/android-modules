/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.androidmodules.demo;

import android.widget.TextView;

import com.netimen.androidmodules.annotations.EModule;
import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.api.events.Search;
import com.netimen.androidmodules.api.search.PerformSearchSubmodule;
import com.netimen.androidmodules.api.search.QueryWatcherSubmodule;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_search)
@EModule(submodules = {PerformSearchSubmodule.class, QueryWatcherSubmodule.class})
public class SearchFragment extends WorkFragment {

    @ViewById
    TextView resultSearch;

    @StringRes
    String searchSome;

    public SearchFragment() {
        super("search");
    }

    @Event
    void search(Search search) {
        resultSearch.setText(String.format(" " + searchSome, search.query));
        workDone();
    }
}
