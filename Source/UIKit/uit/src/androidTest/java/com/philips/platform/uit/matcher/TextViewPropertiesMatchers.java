/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.matcher;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
                    setValues(String.valueOf(actual), String.valueOf(expectedValue));
                    return actual == expectedValue;
                }
                throw new RuntimeException("expected TextView got " +view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameFontSize(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    setValues(String.valueOf(((TextView) view).getTextSize()), String.valueOf(expectedValue));
                    return ((TextView) view).getTextSize() == expectedValue;
                }
                throw new RuntimeException("expected TextView got " +view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameCompoundDrwablePadding(final int expectedValue) {
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
                        boolean result =  heightMatcher.matches(drawables[index]);
                        setValues(heightMatcher.actual, heightMatcher.expected);
                        return result;
                    }
                    return false;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameCompoundDrawableColor(final int index, final int expectedValue, final int state) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    Drawable[] drawables = ((TextView) view).getCompoundDrawables();
                    final BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColor(state, expectedValue);
                    boolean result =  colorMatcher.matches(drawables[index]);

                    setValues(colorMatcher.actual, colorMatcher.expected);
                    return result;
                }
                throw new RuntimeException("expected TextView got " +view.getClass().getName());
            }
        };
    }

}