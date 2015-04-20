package com.netimen.androidmodules.demo;

import android.support.v4.app.FragmentActivity;

import com.netimen.androidmodules.annotations.EModule;
import com.netimen.androidmodules.demo.submodules.AnimateActionButtonSubmodule;
import com.netimen.androidmodules.demo.submodules.CalcDistanceSubmodule;
import com.netimen.androidmodules.demo.submodules.FindPlaceSubmodule;
import com.netimen.androidmodules.demo.submodules.MapInteractionSubmodule;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_demo)
@EModule(submodules = {FindPlaceSubmodule.class, CalcDistanceSubmodule.class, MapInteractionSubmodule.class, AnimateActionButtonSubmodule.class})
public class DemoActivity extends FragmentActivity {
}
