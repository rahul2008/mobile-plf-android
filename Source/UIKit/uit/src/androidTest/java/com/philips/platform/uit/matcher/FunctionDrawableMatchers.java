package com.philips.platform.uit.matcher;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;

import com.philips.platform.uit.utils.UITTestUtils;

import org.hamcrest.Matcher;

public class FunctionDrawableMatchers {
    public static Matcher<View> isSameHeight(final String funcName, final int expectedValue) {
        return isSameHeight(funcName, expectedValue, -1);
    }

    public static Matcher<View> isSameHeight(final String funcName, final int expectedValue, final int drawableID) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                Drawable drawable = UITTestUtils.getDrawableWithReflection(view, funcName);
                if(drawable instanceof LayerDrawable) {
                    drawable = ((LayerDrawable) drawable).findDrawableByLayerId(drawableID);
                }
                return DrawableMatcher.isSameHeight(expectedValue).matches(drawable);
            }
        };
    }

    public static Matcher<View> isSameWidth(final String funcName, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return DrawableMatcher.isSameWidth(expectedValue).matches(UITTestUtils.getDrawableWithReflection(view, funcName));
            }
        };
    }

    public static Matcher<View> isSameRadius(final String funcName, final int index, final float expectedValue) {
        return isSameRadius(funcName, index,expectedValue, -1);
    }

    public static Matcher<View> isSameRadius(final String funcName, final int index, final float expectedValue, final int drawableID) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                Drawable drawable = UITTestUtils.getDrawableWithReflection(view, funcName);
                if(drawable instanceof LayerDrawable) {
                    drawable = ((LayerDrawable) drawable).findDrawableByLayerId(drawableID);
                }

                return DrawableMatcher.isSameRadius(index, expectedValue).matches(drawable);
            }
        };
    }
    /**
     * Must be operated on drawables. If the target is ColorStateList, use another function instead.
     *
     * @param funcName
     * @param state
     * @param expectedValue
     * @return
     */
    public static Matcher<View> isSameColor(final String funcName, final int state, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return DrawableMatcher.isSameColor(state, expectedValue).matches(UITTestUtils.getDrawableWithReflection(view, funcName));
            }
        };
    }


    /**
     * Must be operated on ColorStateList. If the target is Drawable, use another function instead.
     *
     * @param funcName
     * @param state
     * @param expectedValue
     * @return
     */
    public static Matcher<View> isSameColorFromColorList(final String funcName, final int state, final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                ColorStateList colorStateList = UITTestUtils.getColorStateListWithReflection(view, funcName);
                return colorStateList.getColorForState(new int[]{state}, Color.MAGENTA) == expectedValue;
            }
        };
    }
}
