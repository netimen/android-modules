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
import com.netimen.annotations.helpers.ModuleProvider;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import netimen.com.demo.api.events.InputChanged;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
    @ViewById
    TextView comment;

    @StringRes
    String commentCalcDone, commentSearchDone, commentWorkingWith;

    @Event(moduleClass = {CalcFragment.class, SearchFragment.class})
    void workingWith(InputChanged inputChanged) {
        comment.setText(String.format(commentWorkingWith, inputChanged.moduleName));
    }

    @Event(moduleName = Event.ANY_MODULE)
    void workDone(ModuleProvider.IModule module) {
        comment.setText(module instanceof CalcFragment ? commentCalcDone : commentSearchDone);
    }
//    @Event(value = WorkDone.class, moduleClass = CalcFragment.class)
//    void calcDone() {
//        comment.setText(commentCalcDone);
//    }
//
//    @Event(value = WorkDone.class, moduleClass = SearchFragment.class)
//    void searchDone() {
//        comment.setText(commentSearchDone);
//    }
}
