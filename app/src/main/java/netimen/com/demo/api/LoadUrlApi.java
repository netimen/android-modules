/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api;

import android.webkit.WebView;
import android.widget.Button;

import com.netimen.annotations.Event;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.events.GetUrl;
import netimen.com.demo.api.events.UrlChanged;

@EBean
public class LoadUrlApi extends BaseApi {

    @ViewById
    WebView webView;

    @ViewById
    Button go;

    @Event
    void onUrlChanged(UrlChanged event) {
        go.setEnabled(!event.isEmpty);
    }

    @Click
    void go() {
        webView.loadUrl(bus.request(new GetUrl()));
    }

}
