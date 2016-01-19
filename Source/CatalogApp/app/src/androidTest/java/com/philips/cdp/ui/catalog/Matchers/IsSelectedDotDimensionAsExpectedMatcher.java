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
public class IsSelectedDotDimensionAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsCircleAsExpected";
    private float expectedSelectedDotHeight;
    private float expectedSelectedDotWidth;

    public IsSelectedDotDimensionAsExpectedMatcher(final Class<? extends View> expectedType, float expectedSelectedDotHeight, float expectedSelectedDotWidth) {
        super(expectedType);
        this.expectedSelectedDotHeight = expectedSelectedDotHeight;
        this.expectedSelectedDotWidth = expectedSelectedDotWidth;

    }

    public static Matcher<View> isSelectedDotDimenSimilar(final float expectedSelectedDotHeight, final float expectedSelectedDotWidth) {
        return new IsSelectedDotDimensionAsExpectedMatcher(View.class, expectedSelectedDotHeight, expectedSelectedDotWidth);
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
        float actualHeight = circle.getSelectedCircleHeight();
        float actualWidth = circle.getSelectedCircleWidth();

        return (actualHeight == expectedSelectedDotHeight) || (actualWidth == expectedSelectedDotWidth);
    }

}
