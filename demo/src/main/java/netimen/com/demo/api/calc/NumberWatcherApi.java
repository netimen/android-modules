/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api.calc;

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
public class NumberWatcherApi extends BaseApi {

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
