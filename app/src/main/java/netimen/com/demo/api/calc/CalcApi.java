/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api.calc;

import com.netimen.annotations.EBeanCustomScope;

import org.androidannotations.annotations.Bean;

@EBeanCustomScope
public class CalcApi {

    @Bean
    PerformCalcApi performCalcApi;

    @Bean
    NumberWatcherApi numberWatcherApi;
}
