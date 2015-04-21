/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package com.netimen.androidmodules.apidemos.submodules.calc;

import android.widget.Button;

import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.apidemos.submodules.BaseSubmodule;
import com.netimen.androidmodules.apidemos.events.Calc;
import com.netimen.androidmodules.apidemos.events.GetNumber;
import com.netimen.androidmodules.apidemos.events.InputChanged;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EBean
public class PerformCalcSubmodule extends BaseSubmodule {

    @ViewById
    Button calc;

    @Event
    void onInputChanged(InputChanged event) {
        calc.setEnabled(!event.isEmpty);
    }

    @Click
    void calc() {
        bus.event(new Calc(bus.request(new GetNumber())));
    }

    @EditorAction
    void editNumber() {
        calc();
    }
}
