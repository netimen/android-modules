/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.androidmodules.api.search;

import android.text.TextUtils;
import android.widget.EditText;

import com.netimen.androidmodules.annotations.Module;
import com.netimen.androidmodules.annotations.Request;
import com.netimen.androidmodules.api.BaseApi;
import com.netimen.androidmodules.api.events.InputChanged;
import com.netimen.androidmodules.demo.R;
import com.netimen.androidmodules.demo.WorkFragment;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

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