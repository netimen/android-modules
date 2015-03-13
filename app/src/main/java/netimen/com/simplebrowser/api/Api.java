/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.simplebrowser.api;

import com.bookmate.bus.Bus;
import com.bookmate.bus.InjectInstanceProvider;
import com.netimen.annotations.EBeanCustomScope;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;

@EBeanCustomScope
public class Api {
    private final Bus bus = InjectInstanceProvider.set(Bus.class, new Bus()); // CUR BusHolder

    @Bean
    InputApi inputApi;

    @Bean
    SelectionApi selectionApi;

    @AfterInject
    void ready() {
        bus.event(new PageShown());
    }

}
