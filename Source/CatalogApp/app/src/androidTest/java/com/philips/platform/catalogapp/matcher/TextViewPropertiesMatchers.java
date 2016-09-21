/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.matcher;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

public class TextViewPropertiesMatchers {

    public static Matcher<View> isSameTextColor(final int stateAttr, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    return ((TextView) view).getTextColors().getColorForState(new int[]{stateAttr}, Color.MAGENTA) == expectedValue;
                }
                throw new RuntimeException("Expected TextView got " +view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameFontSize(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    return ((TextView) view).getTextSize() == expectedValue;
                }
                throw new RuntimeException("Expected TextView got " +view.getClass().getName());
            }
        };
    }
}