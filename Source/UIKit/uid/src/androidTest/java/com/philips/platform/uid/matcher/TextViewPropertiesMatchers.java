/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.matcher;

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
                    setValues(actual, expectedValue);
                    return areEqual();
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
                    setValues(actual, expectedValue);
                    return areEqual();
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
                    setValues(((TextView) view).getTextSize(), expectedValue);
                    return areEqual();
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
                    setValues(((TextView) view).getLineSpacingExtra(), expectedValue);
                    return areEqual();
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
                    setValues(((TextView) view).getCompoundDrawablePadding(), expectedValue);
                    return areEqual();
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

    public static Matcher<? super View> isSameLineHeight(final float expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof TextView) {
                    setValues(((TextView) view).getLineHeight(), (int) expectedValue);
                    return areEqual();
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<? super View> hasTransformationMethod() {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof TextView) {
                    setValues(((TextView) view).getTransformationMethod(), null);
                    return actual != null;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<? super View> hasNoTransformationMethod() {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof TextView) {
                    setValues(((TextView) view).getTransformationMethod(), null);
                    return actual == null;
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }
}