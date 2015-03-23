/**
 * Copyright (c) 2014 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   07.12.14
 */
package netimen.com.demo;

import android.app.Activity;
import android.widget.TextView;

import com.netimen.annotations.BeanInitScope;
import com.netimen.annotations.Event;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.Api;
import netimen.com.demo.api.events.Calc;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById
    TextView result;

    @BeanInitScope
    Api api;

    @Event
    void calc(Calc calc) {
        result.setText(" " + (calc.number * calc.number));
    }
}
