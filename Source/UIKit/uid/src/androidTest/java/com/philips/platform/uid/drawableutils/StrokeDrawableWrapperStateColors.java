package com.philips.platform.uid.drawableutils;

import android.graphics.drawable.Drawable;

import com.philips.platform.uid.drawable.StrokeDrawableWrapper;

/**
 * Created by 310213764 on 10/7/2016.
 */
public class StrokeDrawableWrapperStateColors extends KitKatStateColors {
    StrokeDrawableWrapper strokeDrawable;

    public StrokeDrawableWrapperStateColors(final Drawable d) {
        super(d);
        strokeDrawable = (StrokeDrawableWrapper) d;
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
