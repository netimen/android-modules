package com.netimen.androidmodules;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.netimen.androidmodules.apidemos.MainActivity_;
import com.netimen.androidmodules.apidemos.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;
import static com.android.support.test.deps.guava.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;

/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 * <p/>
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   09.04.15
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity_> {

    public MainActivityTest() {
        super(MainActivity_.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testButtonState() {
        performTestButtonState(editNumber(), calcButton());
        performTestButtonState(editSearch(), searchButton());
    }


    private void performTestButtonState(ViewInteraction editText, ViewInteraction button) {
        button.check(matches(not(isEnabled())));

        editText.perform(typeText("12"));
        button.check(matches(isEnabled()));

        editText.perform(clearText());
        button.check(matches(not(isEnabled())));
    }

    public void testWorkingWith() {
        performTestWorkingWith(editNumber(), "CalcFragment", "calc");
        performTestWorkingWith(editSearch(), "SearchFragment", "search");
    }

    private void performTestWorkingWith(ViewInteraction editText, String className, String name) {
        editText.perform(typeText("12"));
        onView(withId(R.id.comment)).check(matches(withText(endsWith(className + "_.class)")))).check(matches(withText(startsWith("user is working with " + name))));
    }

    public void testWorkDone() {
        performTestWorkDone(editNumber(), calcButton(), R.id.result, "12", "144", "calculated");
        performTestWorkDone(editSearch(), searchButton(), R.id.result_search, "something", "something", "found");
    }

    private void performTestWorkDone(ViewInteraction editText, ViewInteraction button, int resultViewId, String input, String result, String action) {
        editText.perform(typeText(input));
        button.perform(click());
        onView(withId(resultViewId)).check(matches(withText(containsString(result))));
        onView(withId(R.id.comment)).check(matches(withText(containsString(action))));
    }

    ///

    private ViewInteraction editNumber() {
        return onView(withHint("Enter number"));
    }

    private ViewInteraction calcButton() {
        return onView(withText("Calc"));
    }

    private ViewInteraction editSearch() {
        return onView(MainActivityTest.withHint("Search…"));
    }

    private ViewInteraction searchButton() {
        return onView(ViewMatchers.withText("Search"));
    }

    ///

    public static Matcher<View> withHint(String hintText) {
        checkArgument(!TextUtils.isEmpty(hintText));
        return withHint(is(hintText));
    }

    public static Matcher<View> withHint(final Matcher<String> matcherText) {
        checkNotNull(matcherText);
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with item hint: " + matcherText);
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                return matcherText.matches(view.getHint().toString());
            }
        };
    }
}