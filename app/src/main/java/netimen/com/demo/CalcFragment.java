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
import com.netimen.annotations.ModuleBean;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.calc.CalcApi;
import netimen.com.demo.api.events.Calc;

@EFragment(R.layout.fragment_calc)
public class CalcFragment extends WorkFragment {
    public static final String MODULE_NAME = "calc";

    @ViewById
    TextView result;

    @ModuleBean(moduleName = MODULE_NAME)
    CalcApi calcApi;

    @Event
    void calc(Calc calc) {
        result.setText(" " + (calc.number * calc.number));
        workDone();
    }
}
