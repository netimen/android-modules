package com.netimen.androidmodules.apidemos;

import android.support.v4.app.FragmentActivity;

import com.netimen.androidmodules.annotations.EModule;
import com.netimen.androidmodules.apidemos.submodules.AnimateActionButtonSubmodule;
import com.netimen.androidmodules.apidemos.submodules.CalcDistanceSubmodule;
import com.netimen.androidmodules.apidemos.submodules.FindPlaceSubmodule;
import com.netimen.androidmodules.apidemos.submodules.MapInteractionSubmodule;

import org.androidannotations.annotations.EActivity;

/**
 * all the code
 */
@EActivity(R.layout.activity_demo)
@EModule(submodules = {FindPlaceSubmodule.class, CalcDistanceSubmodule.class, MapInteractionSubmodule.class, AnimateActionButtonSubmodule.class})
public class DemoActivity extends FragmentActivity {
}
