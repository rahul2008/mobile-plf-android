/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdpp.catalogapp.utils;

import android.graphics.drawable.Drawable;

public class KitKatStateColors extends BaseColorStateImpl {
    private final static String GRADIENT_SOLID_COLOR = "mSolidColor";
    private final static String STROKE_SOLID_COLOR = "mStrokeColor";

    public KitKatStateColors(Drawable d) {
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