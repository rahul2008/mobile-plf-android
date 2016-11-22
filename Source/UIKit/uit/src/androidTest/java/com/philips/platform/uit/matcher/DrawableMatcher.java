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

    public static Matcher<Drawable> isMinHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                int actualHeight = bounds.bottom - bounds.top;
                setValues(String.valueOf(actualHeight), String.valueOf(expectedValue));
                return actualHeight >= expectedValue;
            }
        };
    }

    public static Matcher<Drawable> isMinWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                int actualHeight = bounds.right - bounds.left;
                setValues(String.valueOf(actualHeight), String.valueOf(expectedValue));
                return actualHeight >= expectedValue;
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

    public static Matcher<Drawable> isSameColor(final int state, final int expectedValue, final boolean defaultColor) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                int color;
                if (defaultColor) {
                    color = stateColors.getDefaultColor();
                } else {
                    color = stateColors.getStateColor(state);
                }
                setValues(String.valueOf(Integer.toHexString(color)), Integer.toHexString(expectedValue));
                return color == expectedValue;
            }
        };
    }

    public static Matcher<Drawable> isSameStrokeColor(final int state, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(String.valueOf(stateColors.getStrokeSolidStateColor(state)), String.valueOf(expectedValue));
                return stateColors.getStrokeSolidStateColor(state) == expectedValue;
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

    public static Matcher<Drawable> isSameRippleRadius(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(String.valueOf(stateColors.getRippleRadius()), String.valueOf(expectedValue));
                return stateColors.getRippleRadius() == expectedValue;
            }
        };
    }


    public static Matcher<Drawable> isSameRippleColor(final int attr, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(String.valueOf(stateColors.getRippleColor(attr)), String.valueOf(expectedValue));
                return stateColors.getRippleColor(attr) == expectedValue;
            }
        };
    }

    public static Matcher<Drawable> isSameThickness(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                int actual = stateColors.getRingThickness();
                setValues(String.valueOf(actual), String.valueOf(expectedValue));
                return actual == expectedValue;
            }
        };
    }

    public static Matcher<Drawable> isSameInnerRadius(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {
            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                int actual = stateColors.getInnerRadius();
                setValues(String.valueOf(actual), String.valueOf(expectedValue));
                return actual == expectedValue;
            }
        };
    }

    public static Matcher<Drawable> isSameColors(final int index, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {
            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                int actual = stateColors.getColors()[index];
                setValues(String.valueOf(actual), String.valueOf(expectedValue));
                return actual == expectedValue;
            }
        };
    }
}
