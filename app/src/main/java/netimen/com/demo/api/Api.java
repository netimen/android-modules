/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api;

import com.netimen.annotations.EBeanCustomScope;

import org.androidannotations.annotations.Bean;

@EBeanCustomScope
public class Api {

    @Bean
    CalcApi calcApi;

    @Bean
    NumberWatcherApi numberWatcherApi;
}
