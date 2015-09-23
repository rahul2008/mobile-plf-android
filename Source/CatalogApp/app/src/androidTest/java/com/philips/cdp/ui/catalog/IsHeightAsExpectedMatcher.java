package com.philips.cdp.ui.catalog;

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
public class IsHeightAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsHeightAsExpected";
    private int expectedHeight;

    public IsHeightAsExpectedMatcher(final Class<? extends View> expectedType, int expectedHeight) {
        super(expectedType);
        this.expectedHeight = expectedHeight;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual image differs in height when compared with expected image");
    }

    @Override
    public boolean matchesSafely(View view) {
        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualHeight = actualBitmap.getHeight();
        if (actualHeight == expectedHeight) {
            return true;
        }
           return false;
    }
    public static Matcher<View> isHeightSimilar(final int expectedHeight){
        return new IsHeightAsExpectedMatcher(View.class, expectedHeight);
    }

};
