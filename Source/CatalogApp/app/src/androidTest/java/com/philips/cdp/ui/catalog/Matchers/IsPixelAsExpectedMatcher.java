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
public class IsPixelAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsPixelAsExpected";
    private static final int RELAXED_BOUNDARY_PIXELS = 2;
    private static final int PASS_PERCENTAGE = 11;
    private Bitmap expectedBitmap;

    public IsPixelAsExpectedMatcher(final Class<? extends View> expectedType, Bitmap expectedBitmap) {
        super(expectedType);
        this.expectedBitmap = expectedBitmap;
    }

    public static Matcher<View> isImageSimilar(final Bitmap expectedBitmap) {
        return new IsPixelAsExpectedMatcher(View.class, expectedBitmap);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Image differs too much from expected image");
    }

    @Override
    public boolean matchesSafely(View view) {
        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualWidth = actualBitmap.getWidth();
        int actualHeight = actualBitmap.getHeight();

        long diff = 0;
        //Relaxing the comparison by ignoring 2px from each boundary.
        int rowCount = actualHeight - RELAXED_BOUNDARY_PIXELS;
        int columnCount = actualWidth - RELAXED_BOUNDARY_PIXELS;
        //Start comparing by relaxing 2px from starting points
        for (int row = RELAXED_BOUNDARY_PIXELS; row < rowCount; row++) {
            for (int column = RELAXED_BOUNDARY_PIXELS; column < columnCount; column++) {
                if (actualBitmap.getPixel(row, column) != expectedBitmap.getPixel(row, column)) {
                    diff++;
                }
            }
        }
        float mismatchPercentage = (diff / ((float) actualBitmap.getHeight() * actualBitmap.getWidth())) * 100;

        Log.d(TAG, "mismatchPercentage:" + mismatchPercentage + "% count=" + diff);

        return mismatchPercentage <= PASS_PERCENTAGE;
    }
}
