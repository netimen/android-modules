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

import com.netimen.annotations.Event;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import netimen.com.demo.api.events.WorkDone;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
    @ViewById
    TextView comment;

    @StringRes
    String commentCalc, commentSearch;

    @Event(value = WorkDone.class, moduleClass = CalcFragment.class)
    void calcDone() {
        comment.setText(commentCalc);
    }

    @Event(value = WorkDone.class, moduleClass = SearchFragment.class)
    void searchDone() {
        comment.setText(commentSearch);
    }
}
