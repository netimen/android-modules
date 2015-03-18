/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api;

import android.text.TextUtils;
import android.widget.EditText;

import com.netimen.annotations.Request;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.R;
import netimen.com.demo.api.events.UrlChanged;

@EBean
public class UrlWatcherApi extends BaseApi {

    @ViewById
    EditText editUrl;

    @TextChange(R.id.edit_url)
    void onAddressChanged(CharSequence url) {
        bus.event(new UrlChanged(TextUtils.isEmpty(url)));
    }

    @Request
    String getUrl() {
        return editUrl.getText().toString();
    }
}
