/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.drawableutils;

import android.graphics.drawable.Drawable;

import com.philips.platform.uit.compat.StrokeDrawableWrapper;

public class StrokeDrawableWrapperStateColor extends KitKatStateColors {
    StrokeDrawableWrapper strokeDrawable;

    public StrokeDrawableWrapperStateColor(final Drawable d) {
        super(d);
        strokeDrawable = (StrokeDrawableWrapper) d;
    }

    @Override
    public int getStateColor(final int attr) {
        return strokeDrawable.getstateColor(attr);
    }

    @Override
    public int getStrokeSolidStateColor(final int attr) {
        return strokeDrawable.getstateColor(attr);
    }
}
