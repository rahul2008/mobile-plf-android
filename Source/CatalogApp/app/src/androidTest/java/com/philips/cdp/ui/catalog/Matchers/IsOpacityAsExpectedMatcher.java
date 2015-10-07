package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsOpacityAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsOpacityAsExpected";
    private int expectedAlpha;

    public IsOpacityAsExpectedMatcher(final Class<? extends View> expectedType, int expectedAlpha) {
        super(expectedType);
        this.expectedAlpha = expectedAlpha;
          }


    @Override
    public void describeTo(Description description) {
        description.appendText("Alpha value of actual is not as expected");
    }

    @Override
    public boolean matchesSafely(View view) {
        CirclePageIndicator circle = (CirclePageIndicator)view;

        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        Paint paint = circle.getUnselectedPaintFill();
        int actualAlpha = paint.getAlpha();

        if ((actualAlpha == expectedAlpha)) {
            return true;
        }
        return false;
    }


    public static Matcher<View> isOpacitySimilar(final int expectedAlpha) {
        return new IsOpacityAsExpectedMatcher(View.class,expectedAlpha );
    }

}
