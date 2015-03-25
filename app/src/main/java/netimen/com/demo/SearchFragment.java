/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo;

import android.app.Fragment;
import android.widget.TextView;

import com.netimen.annotations.Event;
import com.netimen.annotations.ModuleBean;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import netimen.com.demo.api.events.Search;
import netimen.com.demo.api.search.SearchApi;

@EFragment(R.layout.fragment_search)
public class SearchFragment extends Fragment {
    @ViewById
    TextView resultSearch;

    @ModuleBean(moduleName = "search")
    SearchApi searchApi;

    @StringRes
    String searchSome, searchWordEnd;

    @Event
    void Search(Search search) {
        resultSearch.setText(searchSome + search.query + searchWordEnd);
    }
}
