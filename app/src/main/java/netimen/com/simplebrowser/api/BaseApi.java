/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.simplebrowser.api;

import com.bookmate.bus.Bus;
import com.netimen.annotations.Inject;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public abstract class BaseApi {
    @Bean
    Api api;

    @Inject
    Bus bus;
}
