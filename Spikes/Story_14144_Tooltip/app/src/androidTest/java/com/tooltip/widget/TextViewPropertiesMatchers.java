/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.tooltip.widget;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

public class TextViewPropertiesMatchers {



    public static Matcher<View> isSameFontSize(final float expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    setValues(((TextView) view).getTextSize(), expectedValue);
                    return areEqual();
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }
    public static Matcher<View> isSameTextColor(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    int actual = ((TextView) view).getCurrentTextColor();
                    setValues(actual, expectedValue);
                    return areEqual();
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

}