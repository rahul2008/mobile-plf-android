package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsCircleBackgroundColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsCircleBackgroundBitmapColorAsExpectedMatcher";
    private String expectedFocusedCircleRGB;
    private String expectedUnFocusedCircleRGB;

    public IsCircleBackgroundColorAsExpectedMatcher(final Class<? extends View> expectedType, String expectedFocusedCircleRGB, String expectedUnFocusedCircleRGB) {
        super(expectedType);
        this.expectedFocusedCircleRGB = expectedFocusedCircleRGB;
        this.expectedUnFocusedCircleRGB = expectedUnFocusedCircleRGB;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual background color  differs  when compared with expected background color");
    }

    @Override
    public boolean matchesSafely(View view) {

        CirclePageIndicator circle = (CirclePageIndicator)view;

        Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas actualCanvas = new Canvas(actualBitmap);
        view.draw(actualCanvas);
        int actualFocusedCircleColor = circle.getFillColor() ;
        int actualUnfocusedCircleColor = circle.getColorUnselected();

        String redFocused = Integer.toString(Color.red(actualFocusedCircleColor));
        String greenFocused = Integer.toString(Color.green(actualFocusedCircleColor));
        String blueFocused = Integer.toString(Color.blue(actualFocusedCircleColor));

        String actualFocusedCircleRGB = redFocused+greenFocused+blueFocused;

        String redUnFocused = Integer.toString(Color.red(actualUnfocusedCircleColor));
        String greenUnFocused = Integer.toString(Color.green(actualUnfocusedCircleColor));
        String blueUnFocused = Integer.toString(Color.blue(actualUnfocusedCircleColor));

        String actualUnFocusedCircleRGB = redUnFocused+greenUnFocused+blueUnFocused;

        if ((actualFocusedCircleRGB.equalsIgnoreCase(expectedFocusedCircleRGB)) || (actualUnFocusedCircleRGB.equalsIgnoreCase(expectedUnFocusedCircleRGB) )){
            return true;
        }
            return false;
        }

    public static Matcher<View> isCircleColorSimilar(final String expectedFocusedCircleRGB, final String expectedUnFocusedCircleRGB){
        return new IsCircleBackgroundColorAsExpectedMatcher(View.class, expectedFocusedCircleRGB, expectedUnFocusedCircleRGB);
    }
}

