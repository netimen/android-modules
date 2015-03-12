/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.simplebrowser.api;

import com.bookmate.bus.Bus;
import com.bookmate.bus.BusProvider;
import com.bookmate.bus.CustomInjectProvider;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class Api {
    private final Bus bus = BusProvider.initBus();

    @Bean
    InputApi inputApi;

    @Bean
    SelectionApi selectionApi;

    Api() {
        CustomInjectProvider.set(Api.class, this); // CUR make an annotation
    }
    @AfterInject
    void ready() {
        toString();
    }
}
