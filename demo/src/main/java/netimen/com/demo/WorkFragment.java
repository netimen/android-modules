/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo;

import android.app.Fragment;

import com.netimen.androidmodules.annotations.Inject;
import com.netimen.androidmodules.helpers.Bus;

import org.androidannotations.annotations.EFragment;

import java.util.Random;

import netimen.com.demo.api.events.WorkDone;

@EFragment
public abstract class WorkFragment extends Fragment {
    @Inject
    protected Bus bus;

    public final String name;

    protected WorkFragment(String name) {
        this.name = name + "-" + (1 + new Random().nextInt(10));
    }

    protected void workDone() {
        bus.event(new WorkDone());
    }
}
