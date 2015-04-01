/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package netimen.com.demo;

import android.widget.TextView;

import com.netimen.annotations.EModule;
import com.netimen.annotations.Event;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.calc.NumberWatcherApi;
import netimen.com.demo.api.calc.PerformCalcApi;
import netimen.com.demo.api.events.Calc;

@EFragment(R.layout.fragment_calc)
@EModule(submodules = {PerformCalcApi.class, NumberWatcherApi.class})
public class CalcFragment extends WorkFragment {

    @ViewById
    TextView result;

    @Event
    void calc(Calc calc) {
        result.setText(" " + (calc.number * calc.number));
        workDone();
    }
}
