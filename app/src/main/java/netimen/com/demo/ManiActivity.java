/**
 * Copyright (c) 2014 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   07.12.14
 */
package netimen.com.demo;

import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.bookmate.bus.Bus;
import com.netimen.annotations.BeanInitScope;
import com.netimen.annotations.Inject;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import netimen.com.demo.api.Api;
import netimen.com.demo.api.events.PageShown;

@EActivity(R.layout.better_activity)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class ManiActivity extends Activity {

    @ViewById(R.id.web_view)
    WebView webView;

    @ViewById(R.id.edit_address)
    EditText edit;

    @ViewById(R.id.go)
    Button go;

    @ViewById
    ViewGroup container;

    @BeanInitScope
    Api api;

    @Inject
    Bus bus;

    @AfterInject
    void ready(){
        bus.event(new PageShown());
    }

    @Click
    void go() {
        webView.loadUrl(edit.getText().toString());
    }

    @TextChange(R.id.edit_address)
    void onAddressChanged(CharSequence address) {
        go.setEnabled(!TextUtils.isEmpty(address));
    }

}
