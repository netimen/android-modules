/**
 * Copyright (c) 2014 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   07.12.14
 */
package com.netimen.androidmodules.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.TextView;

import com.netimen.androidmodules.annotations.Event;
import com.netimen.androidmodules.api.events.InputChanged;
import com.netimen.androidmodules.helpers.ModuleObjectsShare;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;


@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
    @ViewById
    TextView comment;

    @StringRes
    String commentCalcDone, commentSearchDone, commentWorkingWith;

    @Event(moduleClass = {CalcFragment.class, SearchFragment.class})
    void workingWith(InputChanged inputChanged, ModuleObjectsShare.IModule module) {
        comment.setText(String.format(commentWorkingWith, inputChanged.moduleName, module.getClass().getSimpleName()));
    }

    @Event(moduleName = Event.ANY_MODULE)
    void workDone(ModuleObjectsShare.IModule module) {
        comment.setText(module instanceof CalcFragment ? commentCalcDone : commentSearchDone);
    }
}
