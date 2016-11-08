/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.matcher;

import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Matcher;

public class ViewPropertiesMatchers {
    public static Matcher<View> isSameLeftPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedValue));
                return view.getPaddingLeft() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameRightPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingRight()), String.valueOf(expectedValue));
                return view.getPaddingRight() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameTopPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingTop()), String.valueOf(expectedValue));
                return view.getPaddingTop() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameBottomPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingBottom()), String.valueOf(expectedValue));
                return view.getPaddingBottom() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameViewWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getWidth()), String.valueOf(expectedValue));
                return view.getWidth() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameViewHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getHeight()), String.valueOf(expectedValue));
                return view.getHeight() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameLeftMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedValue));
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                return lp.getMarginStart() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameRightMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedValue));
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                return lp.getMarginEnd() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameBottomMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedValue));
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                return lp.bottomMargin == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameTopMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedValue));
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                return lp.topMargin == expectedValue;
            }
        };
    }
}