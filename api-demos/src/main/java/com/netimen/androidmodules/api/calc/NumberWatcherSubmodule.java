/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.androidmodules.api.calc;

import android.text.TextUtils;
import android.widget.EditText;

import com.netimen.androidmodules.annotations.Module;
import com.netimen.androidmodules.annotations.Request;
import com.netimen.androidmodules.api.BaseSubmodule;
import com.netimen.androidmodules.api.events.InputChanged;
import com.netimen.androidmodules.demo.R;
import com.netimen.androidmodules.demo.WorkFragment;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EBean
public class NumberWatcherSubmodule extends BaseSubmodule {

    @ViewById
    EditText editNumber;

    @Module
    WorkFragment workFragment;

    @TextChange(R.id.edit_number)
    void onNumberChanged(CharSequence number) {
        bus.event(new InputChanged(workFragment.name, TextUtils.isEmpty(number)));
    }

    @Request
    Integer getNumber() {
        return Integer.valueOf(editNumber.getText().toString());
    }
}
