package com.philips.cdp.ui.catalog;

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
public class IsBackgroundBitmapColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsBackgroundBitmapColorAsExpectedMatcher";
    private Bitmap expectedBitmap;

    public IsBackgroundBitmapColorAsExpectedMatcher(final Class<? extends View> expectedType, Bitmap expectedBitmap) {
        super(expectedType);
        this.expectedBitmap = expectedBitmap;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual background color  differs  when compared with expected background color");
    }

    @Override
    public boolean matchesSafely(View view) {
        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualRGB = actualBitmap.getPixel(15,15);
        int expectedRGB = expectedBitmap.getPixel(15,15);

        if (actualRGB != expectedRGB) {
            return false;
        }
        return true;
    }
    public static Matcher<View> isBackgroundBitmapColorSimilar(final Bitmap expectedBitmap){
        return new IsBackgroundBitmapColorAsExpectedMatcher(View.class, expectedBitmap);
    }
}








