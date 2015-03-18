/**
 * Copyright (c) 2014 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   07.12.14
 */
package netimen.com.demo;

import android.app.Activity;
import android.view.Window;

import com.netimen.annotations.BeanInitScope;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.WindowFeature;

import netimen.com.demo.api.Api;

@EActivity(R.layout.activity_main)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class MainActivity extends Activity {

    @BeanInitScope
    Api api;

//    @Inject
//    Bus bus;
//
//    @AfterInject
//    void ready(){
//        bus.event(new AddressChanged());
//    }


}
