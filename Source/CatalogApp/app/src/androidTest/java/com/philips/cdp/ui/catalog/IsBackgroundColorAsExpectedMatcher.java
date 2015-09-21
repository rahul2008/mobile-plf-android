package com.philips.cdp.ui.catalog;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.util.Log;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsBackgroundColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsBackgroundColorAsExpectedMatcher";
    private Bitmap expectedBitmap;

    public IsBackgroundColorAsExpectedMatcher(final Class<? extends View> expectedType, Bitmap expectedBitmap) {
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
        int actualRGB = actualBitmap.getPixel(3,15);
        int expectedRGB = expectedBitmap.getPixel(3,15);
        if (actualRGB != expectedRGB) {
            return false;
        }
        return true;
    }


   /* private int bitmaptoRGB (Bitmap bitmap) {

        int pixel = bitmap.getPixel(15,15);
        int redValue = Color.red(pixel);
        int greenValue = Color.green(pixel);
        int blueValue = Color.blue(pixel);
        int RGB = android.graphics.Color.rgb(redValue, greenValue, blueValue);
        return RGB;
    }*/
    public static Matcher<View> isBackgroundColorSimilar(final Bitmap expectedBitmap){
        return new IsBackgroundColorAsExpectedMatcher(View.class, expectedBitmap);
    }
}








