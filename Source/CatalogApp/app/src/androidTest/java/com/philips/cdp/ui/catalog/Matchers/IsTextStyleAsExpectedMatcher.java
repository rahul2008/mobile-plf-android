package com.philips.cdp.ui.catalog.Matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsTextStyleAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsTextStyleAsExpectedMatcher";
    private int expectedStyle;

    public IsTextStyleAsExpectedMatcher(final Class<? extends View> expectedType, int expectedStyle) {
        super(expectedType);
        this.expectedStyle = expectedStyle;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual Text Type differs when compared with expected Text Type");
    }

    @Override
    public boolean matchesSafely(View view) {

        if (view instanceof TextView) {
            TextView actualTextview = (TextView) view;
            return (actualTextview.getTypeface().getStyle() == expectedStyle);
        }
        return false;
    }

    public static Matcher<View> isTextStyleSimilar(final int expectedStyle) {
        return new IsTextStyleAsExpectedMatcher(View.class, expectedStyle);
    }
}











