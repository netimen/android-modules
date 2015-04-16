/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.androidmodules.api;

import com.netimen.androidmodules.annotations.Inject;
import com.netimen.androidmodules.helpers.Bus;

import org.androidannotations.annotations.EBean;

@EBean
public abstract class BaseApi {

    @Inject
    protected Bus bus;
}
