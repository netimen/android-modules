/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package com.netimen.androidmodules.apidemos;

import android.app.Fragment;

import com.netimen.androidmodules.annotations.Inject;
import com.netimen.androidmodules.apidemos.events.WorkDone;
import com.netimen.androidmodules.helpers.Bus;

import org.androidannotations.annotations.EFragment;

import java.util.Random;


@EFragment
public abstract class WorkFragment extends Fragment {
    @Inject
    protected Bus bus;

    public final String name;

    @SuppressWarnings("WeakerAccess")
    protected WorkFragment(String name) {
        this.name = name + "-" + (1 + new Random().nextInt(10));
    }

    @SuppressWarnings("WeakerAccess")
    protected void workDone() {
        bus.event(new WorkDone());
    }
}
