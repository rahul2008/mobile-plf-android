package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class IsOpacityValueAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsOpacityAsExpected";
    private int expectedAlpha;
    private int actualX;
    private int actualY;


    public IsOpacityValueAsExpectedMatcher(final Class<? extends View> expectedType, int expectedAlpha, final int actualX, final int actualY) {
        super(expectedType);
        this.expectedAlpha = expectedAlpha;
        this.actualX = actualX;
        this.actualY = actualY;
    }

    public static Matcher<View> isOpacityValueSimilar(final int expectedAlpha, final int actualX, final int actualY) {
        return new IsOpacityValueAsExpectedMatcher(View.class, expectedAlpha, actualX, actualY);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Alpha value of actual is not as expected");
    }

    @Override
    public boolean matchesSafely(View view) {

        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int pixel = actualBitmap.getPixel(actualX, actualY);
        int actualAlpha = Color.alpha(pixel);

        return (actualAlpha == expectedAlpha);
    }
}


