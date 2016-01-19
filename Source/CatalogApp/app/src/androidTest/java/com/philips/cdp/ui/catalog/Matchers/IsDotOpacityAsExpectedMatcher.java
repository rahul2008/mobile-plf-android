package com.philips.cdp.ui.catalog.Matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.philips.cdp.uikit.customviews.CircleIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */


public class IsDotOpacityAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsOpacityAsExpected";
    private int expectedAlpha;

    public IsDotOpacityAsExpectedMatcher(final Class<? extends View> expectedType, int expectedAlpha) {
        super(expectedType);
        this.expectedAlpha = expectedAlpha;
    }

    public static Matcher<View> isOpacitySimilar(final int expectedAlpha) {
        return new IsDotOpacityAsExpectedMatcher(View.class, expectedAlpha);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Alpha value of actual is not as expected");
    }

    @Override
    public boolean matchesSafely(View view) {
        CircleIndicator circle = (CircleIndicator) view;
//        PhilipsBadgeView philipsBadgeView = (PhilipsBadgeView)view;
//        philipsBadgeView.getCurrentTextColor();

/*        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        Paint paint = circle.getUnselectedPaintFill();*/


        float actualAlpha = circle.getUnSelectedDot().getAlpha();

        return (actualAlpha == expectedAlpha);
    }

}
