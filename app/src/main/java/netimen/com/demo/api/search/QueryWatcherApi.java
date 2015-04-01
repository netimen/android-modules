/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo.api.search;

import android.text.TextUtils;
import android.widget.EditText;

import com.netimen.annotations.Module;
import com.netimen.annotations.Request;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.R;
import netimen.com.demo.WorkFragment;
import netimen.com.demo.api.BaseApi;
import netimen.com.demo.api.events.InputChanged;

@EBean
public class QueryWatcherApi extends BaseApi {
    @ViewById
    EditText editQuery;

    @Module
    WorkFragment workFragment;

    @TextChange(R.id.edit_query)
    void onQueryChanged(CharSequence query) {
        bus.event(new InputChanged(workFragment.name, TextUtils.isEmpty(query)));
    }

    @Request
    String getQuery() {
        return editQuery.getText().toString();
    }
}
