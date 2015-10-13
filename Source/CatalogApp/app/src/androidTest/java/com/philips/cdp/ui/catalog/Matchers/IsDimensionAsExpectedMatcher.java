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
public class IsDimensionAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsDimensionAsExpected";
    private int expectedHeight;
    private int expectedWidth;

    public IsDimensionAsExpectedMatcher(final Class<? extends View> expectedType, int expectedHeight, int expectedWidth) {
        super(expectedType);
        this.expectedHeight = expectedHeight;
        this.expectedWidth = expectedWidth;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual image differs in height and width when compared with expected image");
    }

    @Override
    public boolean matchesSafely(View view) {
        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualHeight = actualBitmap.getHeight();
        int actualWidth = actualBitmap.getWidth();
        if ((actualHeight == expectedHeight) || (actualWidth == expectedWidth)) {
            return true;
        }
        return false;
    }

    public static Matcher<View> isDimensionSimilar(final int expectedHeight, final int expectedWidth) {
        return new IsDimensionAsExpectedMatcher(View.class, expectedHeight, expectedWidth);
    }
};

