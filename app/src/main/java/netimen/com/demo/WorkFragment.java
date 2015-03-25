/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   25.03.15
 */
package netimen.com.demo;

import android.app.Fragment;

import com.bookmate.bus.Bus;
import com.netimen.annotations.Inject;

import org.androidannotations.annotations.EFragment;

import netimen.com.demo.api.events.WorkDone;

@EFragment
public abstract class WorkFragment extends Fragment {
    @Inject
    protected Bus bus;

    protected void workDone() {
        bus.event(new WorkDone());
    }
}
