package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.philips.cdp.uikit.customviews.CircleIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsUnSelectedDotDimensionAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsCircleAsExpected";
    private int expectedUnSelectedDotHeight;
    private int expectedUnSelectedDotWidth;

    public IsUnSelectedDotDimensionAsExpectedMatcher(final Class<? extends View> expectedType, int expectedUnSelectedDotHeight, int expectedUnSelectedDotWidth) {
        super(expectedType);
        this.expectedUnSelectedDotHeight = expectedUnSelectedDotHeight;
        this.expectedUnSelectedDotWidth = expectedUnSelectedDotWidth;

    }

    public static Matcher<View> isUnSelectedDotDimenSimilar(final int expectedUnSelectedDotHeight, final int expectedUnSelectedDotWidth) {
        return new IsUnSelectedDotDimensionAsExpectedMatcher(View.class, expectedUnSelectedDotHeight, expectedUnSelectedDotWidth);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("");
    }


/*
        int actualFocusedRadius = (int) circle.getRadius();
        circle.getUnSelectedRadius();
        circle.getFillColor();
        circle.getColorUnselected();
*/

    @Override
    public boolean matchesSafely(View view) {
        CircleIndicator circle = (CircleIndicator) view;

        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        float actualHeight = circle.getUnSelectedCircleHeight();
        float actualWidth = circle.getUnSelectedCircleWidth();

        return (actualHeight == expectedUnSelectedDotHeight) || (actualWidth == expectedUnSelectedDotWidth);
    }

}
