/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   12.03.15
 */
package netimen.com.demo.api;

import android.widget.Button;

import com.netimen.annotations.Event;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.events.Calc;
import netimen.com.demo.api.events.GetNumber;
import netimen.com.demo.api.events.NumberChanged;

@EBean
public class CalcApi extends BaseApi {

    @ViewById
    Button calc;

    @Event
    void onNumberChanged(NumberChanged event) {
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
