/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.matcher;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

public class TextViewPropertiesMatchers {

    public static Matcher<View> isSameTextColor(final int stateAttr, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    int actual = ((TextView) view).getTextColors().getColorForState(new int[]{stateAttr}, Color.MAGENTA);
                    setValues(Integer.toHexString(actual), Integer.toHexString(expectedValue));
                    return actual == expectedValue;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameHintTextColor(final int stateAttr, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    int actual = ((TextView) view).getHintTextColors().getColorForState(new int[]{stateAttr}, Color.MAGENTA);
                    setValues(Integer.toHexString(actual), Integer.toHexString(expectedValue));
                    return actual == expectedValue;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameFontSize(final float expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    setValues(String.valueOf(((TextView) view).getTextSize()), String.valueOf(expectedValue));
                    return ((TextView) view).getTextSize() == expectedValue;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameLineSpacing(final float expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    setValues(String.valueOf(((TextView) view).getLineSpacingExtra()), String.valueOf(expectedValue));
                    return ((TextView) view).getLineSpacingExtra() == expectedValue;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameCompoundDrawablePadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    setValues(String.valueOf(((TextView) view).getCompoundDrawablePadding()), String.valueOf(expectedValue));
                    return ((TextView) view).getCompoundDrawablePadding() == expectedValue;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameCompoundDrawableHeight(final int index, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {

                if (view instanceof TextView) {
                    Drawable[] drawables = ((TextView) view).getCompoundDrawables();
                    if (drawables != null && drawables[index] != null) {
                        final BaseTypeSafteyMatcher<Drawable> heightMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameHeight(expectedValue);
                        boolean result = heightMatcher.matches(drawables[index]);
                        setValues(heightMatcher.actual, heightMatcher.expected);
                        return result;
                    }
                    return false;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameCompoundDrawableWidth(final int index, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {

                if (view instanceof TextView) {
                    Drawable[] drawables = ((TextView) view).getCompoundDrawables();
                    if (drawables != null && drawables[index] != null) {
                        final BaseTypeSafteyMatcher<Drawable> widthMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedValue);
                        boolean result = widthMatcher.matches(drawables[index]);
                        setValues(widthMatcher.actual, widthMatcher.expected);
                        return result;
                    }
                    return false;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameCompoundDrawableColor(final int index, final int state, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    Drawable[] drawables = ((TextView) view).getCompoundDrawables();
                    if (drawables != null && drawables[index] != null) {
                        final BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColor(state, expectedValue, false);
                        boolean result = colorMatcher.matches(drawables[index]);

                        setValues(colorMatcher.actual, colorMatcher.expected);
                        return result;
                    }
                    return false;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<? super View> isPasswordType() {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof TextView) {
                    setValues(String.valueOf(((TextView) view).getInputType()), String.valueOf(InputType.TYPE_TEXT_VARIATION_PASSWORD));
                    return actual == expected;
                }
                return false;
            }
        };
    }
}