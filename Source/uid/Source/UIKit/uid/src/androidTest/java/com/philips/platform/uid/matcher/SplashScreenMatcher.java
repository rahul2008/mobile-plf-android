/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;


import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.View;

import com.philips.platform.uid.drawableutils.GradientDrawableUtils;

import org.hamcrest.Matcher;

public class SplashScreenMatcher {

    public final static int GRADIENT_START_COLOR = 0;
    public final static int GRADIENT_END_COLOR = 1;

    public static Matcher<View> isSameBackgroundColor(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                LayerDrawable layerDrawable = (LayerDrawable) view.getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) layerDrawable.getDrawable(0);
                setValues(colorDrawable.getColor(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameGradientColor(final int expectedValue, final int index) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                LayerDrawable layerDrawable = (LayerDrawable) view.getBackground();
                GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.getDrawable(1);
                int colors[];
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    colors = gradientDrawable.getColors();
                } else {
                    colors = GradientDrawableUtils.getStateColors(gradientDrawable).getColors();
                }
                setValues(colors[index], expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameGradientRadius(final float expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                LayerDrawable layerDrawable = (LayerDrawable) view.getBackground();
                GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.getDrawable(1);
                setValues(gradientDrawable.getGradientRadius(), expectedValue);
                return areEqual();
            }
        };
    }
}
