/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo;

import android.widget.TextView;

import com.netimen.annotations.Event;
import com.netimen.annotations.Module;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import netimen.com.demo.api.events.Search;
import netimen.com.demo.api.search.SearchApi;

@EFragment(R.layout.fragment_search)
public class SearchFragment extends WorkFragment {
    public static final String MODULE_NAME = "search";

    @ViewById
    TextView resultSearch;

    @Module(MODULE_NAME)
    @Bean
    SearchApi searchApi;

    @StringRes
    String searchSome, searchWordEnd;

    @Event
    void search(Search search) {
        resultSearch.setText(" " + searchSome + " " + search.query + searchWordEnd);
        workDone();
    }
}
