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

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
    @ViewById
    TextView comment;

    @StringRes
    String commentCalc, commentSearch;

    @Event
    void workDone() {
        comment.setText(commentCalc);
    }
}
