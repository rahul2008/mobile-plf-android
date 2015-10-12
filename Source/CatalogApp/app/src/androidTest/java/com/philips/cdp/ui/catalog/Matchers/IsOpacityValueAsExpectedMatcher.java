package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;



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

public class IsOpacityValueAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsOpacityAsExpected";
    private int expectedAlpha;

    public IsOpacityValueAsExpectedMatcher(final Class<? extends View> expectedType, int expectedAlpha) {
        super(expectedType);
        this.expectedAlpha = expectedAlpha;
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
        int pixel = actualBitmap.getPixel(30, 30);
        int actualAlpha = Color.alpha(pixel);

        if ((actualAlpha == expectedAlpha)) {
            return true;
        }
        return false;
    }


    public static Matcher<View> isOpacityValueSimilar(final int expectedAlpha) {
        return new IsOpacityValueAsExpectedMatcher(View.class,expectedAlpha );
    }

}


