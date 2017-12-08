package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsWidthAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsWidthAsExpected";
    private int expectedWidth;

    public IsWidthAsExpectedMatcher(final Class<? extends View> expectedType, int expectedWidth) {
        super(expectedType);
        this.expectedWidth = expectedWidth;
    }

    public static Matcher<View> isWidthSimilar(final int expectedWidth) {
        return new IsWidthAsExpectedMatcher(View.class, expectedWidth);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual image differs in width when compared with expected image");
    }

    @Override
    public boolean matchesSafely(View view) {
        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualWidth = actualBitmap.getWidth();
        return actualWidth == expectedWidth;
    }
}
