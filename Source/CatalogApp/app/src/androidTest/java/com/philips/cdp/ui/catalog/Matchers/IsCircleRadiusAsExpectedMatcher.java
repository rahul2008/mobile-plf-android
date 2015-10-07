package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsCircleRadiusAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsCircleAsExpected";
    private int expectedRadiusFocused;
    private int expectedRadiusUnFocused;

    public IsCircleRadiusAsExpectedMatcher(final Class<? extends View> expectedType, int expectedRadiusFocused, int expectedRadiusUnFocused) {
        super(expectedType);
        this.expectedRadiusFocused = expectedRadiusFocused;
        this.expectedRadiusUnFocused = expectedRadiusUnFocused;

        }


    @Override
    public void describeTo(Description description) {
        description.appendText("");
    }

    @Override
    public boolean matchesSafely(View view) {
        CirclePageIndicator circle = (CirclePageIndicator)view;

        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        float actualRadius = circle.getRadius();
        float actualRadiusUnfocused = circle.getUnSelectedRadius();

        if ((actualRadius == expectedRadiusFocused)||(actualRadiusUnfocused == expectedRadiusUnFocused)) {
            return true;
        }
        return false;
    }


/*
        int actualFocusedRadius = (int) circle.getRadius();
        circle.getUnSelectedRadius();
        circle.getFillColor();
        circle.getColorUnselected();
*/

    public static Matcher<View> isCircleRadiusSimilar(final int expectedRadiusFocused, final int expectedRadiusUnFocused) {
        return new IsCircleRadiusAsExpectedMatcher(View.class, expectedRadiusFocused, expectedRadiusUnFocused);
    }

}
