/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package netimen.com.demo;

import android.widget.TextView;

import com.netimen.annotations.Event;
import com.netimen.annotations.Module;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.calc.NumberWatcherApi;
import netimen.com.demo.api.calc.PerformCalcApi;
import netimen.com.demo.api.events.Calc;

@EFragment(R.layout.fragment_calc)
@Module(moduleName = CalcFragment.MODULE_NAME, submodules = {PerformCalcApi.class, NumberWatcherApi.class})
public class CalcFragment extends WorkFragment {
    public static final String MODULE_NAME = "calc";

    @ViewById
    TextView result;

    @Event
    void calc(Calc calc) {
        result.setText(" " + (calc.number * calc.number));
        workDone();
    }
}
