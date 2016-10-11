/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.matcher;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.philips.platform.uit.drawableutils.GradientDrawableUtils;

import org.hamcrest.Matcher;

public class DrawableMatcher {

    public static Matcher<Drawable> isSameHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                int actualHeight = bounds.bottom - bounds.top;
                setValues(String.valueOf(actualHeight), String.valueOf(expectedValue));
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
                setValues(String.valueOf(actualWidth), String.valueOf(expectedValue));
                return expectedValue == actualWidth;
            }
        };
    }

    public static Matcher<Drawable> isSameRadius(final int index, final float expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(String.valueOf(stateColors.getCornerRadius()[index]), String.valueOf(expectedValue));
                return Float.compare(stateColors.getCornerRadius()[index], expectedValue) == 0;
            }
        };
    }

    public static Matcher<Drawable> isSameColor(final int state, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(String.valueOf(stateColors.getStateColor(state)), String.valueOf(expectedValue));
                return stateColors.getStateColor(state) == expectedValue;
            }
        };
    }

    public static Matcher<Drawable> isSameStrokeWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(String.valueOf(stateColors.getStrokeWidth()), String.valueOf(expectedValue));
                return stateColors.getStrokeWidth() == expectedValue;
            }
        };
    }
}
