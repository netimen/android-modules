/**
 * Copyright (c) 2014 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.12.14
 */
package netimen.com.simplebrowser;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup
public class Container extends LinearLayout {
    private final GestureDetector gestureDetector;

    @ViewById
    FrameLayout webContainer;

    public Container(Context context, AttributeSet attrs) {
        super(context, attrs);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.e("AAAAA", "tapup");
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.w("AAAAA", "confirmed");
                return super.onSingleTapConfirmed(e);
            }
        });

    }

    @AfterViews
    void ready() {
        webContainer.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
//        webContainer.addOnGestureListener(new GestureDetector.SimpleOnGestureListener());
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return gestureDetector.onTouchEvent(ev);
//    }

//    @Override
//    public boolean onTouchEvent(@NonNull MotionEvent ev) { // without this, the gestures aren't detected when tapping ReaderView itself.
//        gestureDetector.onTouchEvent(ev);
//        return true;
//    }
}
