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

import org.androidannotations.annotations.Bean;

@EBeanCustomScope
public class Api {

    @InjectInitScope
    Bus bus;

    @Bean
    LoadUrlApi loadUrlApi;

    @Bean
    UrlWatcherApi urlWatcherApi;

}
