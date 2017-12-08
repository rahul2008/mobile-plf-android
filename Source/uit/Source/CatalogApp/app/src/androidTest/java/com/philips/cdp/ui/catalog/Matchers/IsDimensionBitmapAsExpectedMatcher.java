package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.util.Log;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsDimensionBitmapAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsDimensionAsExpected";
    private Bitmap expectedBitmap;

    public IsDimensionBitmapAsExpectedMatcher(final Class<? extends View> expectedType, Bitmap expectedBitmap) {
        super(expectedType);
        this.expectedBitmap = expectedBitmap;
    }

    public static Matcher<View> isDimensionBitmapSimilar(final Bitmap expectedBitmap) {
        return new IsDimensionBitmapAsExpectedMatcher(View.class, expectedBitmap);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual image differs in dimension when compared with expected image");
    }

    @Override
    public boolean matchesSafely(View view) {
        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualWidth = actualBitmap.getWidth();
        int expectedWidth = expectedBitmap.getWidth();
        int actualHeight = actualBitmap.getHeight();
        int expectedHeight = expectedBitmap.getHeight();
        if ((actualWidth != expectedWidth) || (actualHeight != expectedHeight)) {
            Log.d(TAG, "width or height is not as expected");
            return false;
        }
        return true;
    }
}
