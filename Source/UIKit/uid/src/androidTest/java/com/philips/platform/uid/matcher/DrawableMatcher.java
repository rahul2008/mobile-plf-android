/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.matcher;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.philips.platform.uid.drawableutils.GradientDrawableUtils;
import org.hamcrest.Matcher;

public class DrawableMatcher {

    public static Matcher<Drawable> isSameHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                int actualHeight = bounds.bottom - bounds.top;
                setValues(actualHeight, expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isMinHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                int actualHeight = bounds.bottom - bounds.top;
                setValues(actualHeight, expectedValue);
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
                setValues(actualHeight, expectedValue);
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
                setValues(actualWidth, expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameRadius(final int index, final float expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(stateColors.getCornerRadius()[index], expectedValue);
                return Float.compare(stateColors.getCornerRadius()[index], expectedValue) == 0;
            }
        };
    }

    public static Matcher<Drawable> isSameColor(final int []state, final int expectedValue, final boolean defaultColor) {
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
                setValues(Integer.toHexString(color), Integer.toHexString(expectedValue));
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameStrokeColor(final int[] state, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(stateColors.getStrokeSolidStateColor(state), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameStrokeWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(stateColors.getStrokeWidth(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameRippleRadius(final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(stateColors.getRippleRadius(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameRippleColor(final int attr, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(stateColors.getRippleColor(attr), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameThicknessRatio(final float expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(stateColors.getRingThicknessRatio(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameInnerRadiusRatio(final float expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {
            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                setValues(stateColors.getInnerRadiusRatio(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<Drawable> isSameColors(final int index, final int expectedValue) {
        return new BaseTypeSafteyMatcher<Drawable>() {
            @Override
            protected boolean matchesSafely(Drawable drawable) {
                GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(drawable);
                int actual = stateColors.getColors()[index];
                setValues(actual, expectedValue);
                return areEqual();
            }
        };
    }
}
