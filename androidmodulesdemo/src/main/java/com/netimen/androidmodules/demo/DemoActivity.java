package com.netimen.androidmodules.demo;

import android.support.v4.app.FragmentActivity;

import com.netimen.androidmodules.annotations.EModule;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_demo)
@EModule(submodules = {FindPlaceSubmodule.class})
public class DemoActivity extends FragmentActivity {
}
