/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package com.netimen.androidmodules.apidemos;

import android.widget.TextView;

import com.netimen.androidmodules.annotations.EModule;
import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.apidemos.events.Calc;
import com.netimen.androidmodules.apidemos.submodules.calc.NumberWatcherSubmodule;
import com.netimen.androidmodules.apidemos.submodules.calc.PerformCalcSubmodule;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_calc)
@EModule(submodules = {PerformCalcSubmodule.class, NumberWatcherSubmodule.class})
public class CalcFragment extends WorkFragment {

    @ViewById
    TextView result;

    public CalcFragment() {
        super("calc");
    }

    @UiThread // just to make sure such a combination compiles OK
    @Event
    void calc(Calc calc) {
        result.setText(" " + (calc.number * calc.number));
        workDone();
    }
}
