/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.matcher;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.philips.platform.catalogapp.utils.UITTestUtils;

import org.hamcrest.Matcher;

public class FunctionDrawableMatchers {
    public static Matcher<View> isSameHeight(final String funcName, final int expectedHeight) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                Drawable drawable = UITTestUtils.getDrawableWithReflection(view, funcName);
                return DrawableMatcher.isSameHeight(expectedHeight).matches(drawable);
            }
        };
    }

    public static Matcher<View> isSameWidth(final String funcName, final int expectedWidth) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return DrawableMatcher.isSameWidth(expectedWidth).matches(UITTestUtils.getDrawableWithReflection(view, funcName));
            }
        };
    }

    public static Matcher<View> isSameRadius(final String funcName, final int index, final int expectedWidth) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return DrawableMatcher.isSameRadius(index, expectedWidth).matches(UITTestUtils.getDrawableWithReflection(view, funcName));
            }
        };
    }

    public static Matcher<View> isSameColor(final String funcName, final int state, final int expectedWidth) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return DrawableMatcher.isSameRadius(state, expectedWidth).matches(UITTestUtils.getDrawableWithReflection(view, funcName));
            }
        };
    }
}
