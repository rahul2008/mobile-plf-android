package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.philips.cdp.uikit.customviews.CircleIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsSelectedDotBackgroundColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsCircleBackgroundBitmapColorAsExpectedMatcher";
    private String expectedSelectedDotRGB;


    public IsSelectedDotBackgroundColorAsExpectedMatcher(final Class<? extends View> expectedType, String expectedSelectedDotRGB) {
        super(expectedType);
        this.expectedSelectedDotRGB = expectedSelectedDotRGB;
    }

    public static Matcher<View> isSelectedDotColorSimilar(final String expectedSelectedDotRGB) {
        return new IsSelectedDotBackgroundColorAsExpectedMatcher(View.class, expectedSelectedDotRGB);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual background color  differs  when compared with expected background color");
    }

    @Override
    public boolean matchesSafely(View view) {

        CircleIndicator circle = (CircleIndicator) view;

        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualSelectedDotColor = circle.getFilledColor();

/*        String redFocused = Integer.toString(Color.red(actualSelectedDotColor));
        String greenFocused = Integer.toString(Color.green(actualSelectedDotColor));
        String blueFocused = Integer.toString(Color.blue(actualSelectedDotColor));*/

        String actualSelectedDotRGB = "#" +
                Integer.toString(Color.red(actualSelectedDotColor), 16) +
                Integer.toString(Color.green(actualSelectedDotColor), 16) +
                Integer.toString(Color.blue(actualSelectedDotColor), 16);


        return actualSelectedDotRGB.equalsIgnoreCase(expectedSelectedDotRGB);
    }
}

