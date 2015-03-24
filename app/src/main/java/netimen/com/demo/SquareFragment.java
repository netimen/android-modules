/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package netimen.com.demo;

import android.app.Fragment;
import android.widget.TextView;

import com.netimen.annotations.Event;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import netimen.com.demo.api.Api;
import netimen.com.demo.api.events.Calc;

@EFragment(R.layout.fragment_square)
public class SquareFragment extends Fragment {
    @ViewById
    TextView result;

//    @Module("")
    @Bean
    Api api;

    @Event
    void calc(Calc calc) {
        result.setText(" " + (calc.number * calc.number));
    }
}
