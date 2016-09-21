/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.matcher;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.philips.platform.catalogapp.drawableutils.GradientDrawableUtils;

import org.hamcrest.Matcher;

public class DrawableMatcher {

    public static Matcher<Drawable> isSameHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                int actualHeight = bounds.bottom - bounds.top;
                return expectedValue == actualHeight;
            }
        };
    }

    public static Matcher<Drawable> isSameWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                int actualWidth = bounds.right - bounds.left;
                return expectedValue == actualWidth;
            }
        };
    }

    public static Matcher<Drawable> isSameRadius(final int index, final float expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                return Float.compare(stateColors.getCornerRadius()[index], expectedValue) == 0;
            }
        };
    }

    public static Matcher<Drawable> isSameColor(final int state, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                return stateColors.getStateColor(state) == expectedValue;
            }
        };
    }
}
