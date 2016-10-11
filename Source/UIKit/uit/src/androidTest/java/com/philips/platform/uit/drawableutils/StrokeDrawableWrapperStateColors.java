package com.philips.platform.uit.drawableutils;

import android.graphics.drawable.Drawable;

import com.philips.platform.uit.compat.StrokeDrawableWrapper;

/**
 * Created by 310213764 on 10/7/2016.
 */
public class StrokeDrawableWrapperStateColors extends KitKatStateColorsColors {
    StrokeDrawableWrapper strokeDrawable;

    public StrokeDrawableWrapperStateColors(final Drawable d) {
        super(d);
        strokeDrawable = (StrokeDrawableWrapper) d;
    }

    @Override
    public int getStateColor(final int attr) {
        return strokeDrawable.getstateColor(attr);
    }
/*
    @Override
    public float[] getCornerRadius() {
        return new float[0];
    }

    @Override
    public int getStrokeWidth() {
        return 0;
    }

    @Override
    public int getGradientSolidColor() {
        return 0;
    }

    @Override
    public int getStrokeSolidColor() {
        return 0;
    }*/

    @Override
    public int getStrokeSolidStateColor(final int attr) {
        return strokeDrawable.getstateColor(attr);
    }
}
