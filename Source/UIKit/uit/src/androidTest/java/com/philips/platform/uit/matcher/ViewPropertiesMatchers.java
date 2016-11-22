/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.matcher;

import android.support.v7.widget.Toolbar;
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

    public static Matcher<View> isSameStartPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingRight()), String.valueOf(expectedValue));
                return view.getPaddingStart() == expectedValue;
            }
        };
    }

    public static Matcher<View> isSameEndPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedValue));
                return view.getPaddingEnd() == expectedValue;
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

    public static Matcher<? super View> isSameViewMinHeight(final int expectedIconHeight) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedIconHeight));
                return view.getMinimumHeight() == expectedIconHeight;
            }
        };
    }

    public static Matcher<? super View> isSameViewMinWidth(final int expectedIconWidth) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(String.valueOf(view.getPaddingLeft()), String.valueOf(expectedIconWidth));
                return view.getMinimumWidth() == expectedIconWidth;
            }
        };
    }

    public static Matcher<? super View> isVisible(final int gone) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(String.valueOf(view.getVisibility()), String.valueOf(gone));
                return view.getVisibility() == gone;
            }
        };
    }

    public static Matcher<? super View> isSameTitleRightMargin(final int rightMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    setValues(String.valueOf(toolbar.getTitleMarginEnd()), String.valueOf(rightMargin));
                    return actual == expected;
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isSameTittleLeftMargin(final int leftMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    setValues(String.valueOf(toolbar.getTitleMarginStart()), String.valueOf(leftMargin));
                    return actual == expected;
                }
                return false;
            }
        };
    }
}