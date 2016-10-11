/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.drawableutils;

import android.graphics.drawable.Drawable;

public class KitKatStateColorsColors extends BaseStateColorsImpl {
    private final static String GRADIENT_SOLID_COLOR = "mSolidColor";
    private final static String STROKE_SOLID_COLOR = "mStrokeColor";

    public KitKatStateColorsColors(Drawable d) {
        super(d);
    }

    @Override
    public int getStateColor(int attr) {
        return getGradientSolidColor();
    }

    @Override
    public int getGradientSolidColor() {
        return (int) GradientDrawableUtils.getField(constantState, GRADIENT_SOLID_COLOR);
    }

    @Override
    public int getStrokeSolidColor() {
        return (int) GradientDrawableUtils.getField(constantState, STROKE_SOLID_COLOR);
    }

    @Override
    public int getStrokeSolidStateColor(int attr) {
        return getStrokeSolidColor();
    }
}