/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package com.netimen.androidmodules.demo;

import android.widget.TextView;

import com.netimen.androidmodules.annotations.EModule;
import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.api.calc.NumberWatcherSubmodule;
import com.netimen.androidmodules.api.calc.PerformCalcSubmodule;
import com.netimen.androidmodules.api.events.Calc;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_calc)
@EModule(submodules = {PerformCalcSubmodule.class, NumberWatcherSubmodule.class})
public class CalcFragment extends WorkFragment {

    @ViewById
    TextView result;

    public CalcFragment() {
        super("calc");
    }

    @Event
    void calc(Calc calc) {
        result.setText(" " + (calc.number * calc.number));
        workDone();
    }
}
