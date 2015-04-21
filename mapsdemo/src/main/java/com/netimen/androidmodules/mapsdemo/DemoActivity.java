package com.netimen.androidmodules.mapsdemo;

import android.support.v4.app.FragmentActivity;

import com.netimen.androidmodules.annotations.EModule;
import com.netimen.androidmodules.mapsdemo.submodules.AnimateActionButtonSubmodule;
import com.netimen.androidmodules.mapsdemo.submodules.CalcDistanceSubmodule;
import com.netimen.androidmodules.mapsdemo.submodules.FindPlaceSubmodule;
import com.netimen.androidmodules.mapsdemo.submodules.MapInteractionSubmodule;

import org.androidannotations.annotations.EActivity;

/**
 * all the code is in the submodules
 */
@EActivity(R.layout.activity_demo)
@EModule(submodules = {FindPlaceSubmodule.class, CalcDistanceSubmodule.class, MapInteractionSubmodule.class, AnimateActionButtonSubmodule.class})
public class DemoActivity extends FragmentActivity {
}
