/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api;

import com.bookmate.bus.Bus;
import com.netimen.annotations.EBeanCustomScope;
import com.netimen.annotations.InjectInitScope;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;

import netimen.com.demo.api.events.PageShown;

@EBeanCustomScope
public class Api {

    @InjectInitScope
    Bus bus;

    @Bean
    InputApi inputApi;

    @Bean
    SelectionApi selectionApi;

    @AfterInject
    void ready() {
        bus.event(new PageShown());
    }

}
