/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.matcher;

import android.view.View;

import org.hamcrest.Matcher;

public class ViewPropertiesMatchers {
    public static Matcher<View> isSameLeftPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return view.getPaddingLeft() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameRightPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return view.getPaddingRight() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameToptPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return view.getPaddingTop() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameBottomPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return view.getPaddingBottom() == expectedValue;
            }
        };
    }
}