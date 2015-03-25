/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo.api.search;

import com.netimen.annotations.EBeanCustomScope;

import org.androidannotations.annotations.Bean;

@EBeanCustomScope
public class SearchApi {
    @Bean
    QueryWatcherApi queryWatcherApi;

    @Bean
    PerformSearchApi performSearchApi;
}
