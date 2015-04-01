/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo;

import android.widget.TextView;

import com.netimen.annotations.EModule;
import com.netimen.annotations.Event;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import netimen.com.demo.api.events.Search;
import netimen.com.demo.api.search.PerformSearchApi;
import netimen.com.demo.api.search.QueryWatcherApi;

@EFragment(R.layout.fragment_search)
@EModule(submodules = {PerformSearchApi.class, QueryWatcherApi.class})
public class SearchFragment extends WorkFragment {

    @ViewById
    TextView resultSearch;

    @StringRes
    String searchSome, searchWordEnd;

    @Event
    void search(Search search) {
        resultSearch.setText(" " + searchSome + " " + search.query + searchWordEnd);
        workDone();
    }
}
