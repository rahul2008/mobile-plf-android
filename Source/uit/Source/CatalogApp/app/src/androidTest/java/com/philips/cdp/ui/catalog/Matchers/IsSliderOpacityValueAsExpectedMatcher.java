package com.philips.cdp.ui.catalog.Matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class IsSliderOpacityValueAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsOpacityAsExpected";
    private int expectedAlpha;


    public IsSliderOpacityValueAsExpectedMatcher(final Class<? extends View> expectedType, int expectedAlpha) {
        super(expectedType);
        this.expectedAlpha = expectedAlpha;
    }

    public static Matcher<View> isSliderOpacityValueSimilar(final int expectedAlpha) {
        return new IsSliderOpacityValueAsExpectedMatcher(View.class, expectedAlpha);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Alpha value of actual is not as expected");
    }

    @Override
    public boolean matchesSafely(View view) {
        if (view instanceof SeekBar) {
            SeekBar seekbar = (SeekBar) view;
            int actualAlpha = seekbar.getThumb().getAlpha();

            return (actualAlpha == expectedAlpha);
        }
        return false;
    }
}


