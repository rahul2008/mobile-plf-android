package com.philips.platform.uit.compat;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

public class StrokeCompat {

    public static Drawable setStroke(Drawable drawable, int width, ColorStateList list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setStroke(width, list);
            return drawable;
        } else if (drawable instanceof StrokeDrawableWrapper) {
            ((StrokeDrawableWrapper) drawable).setStroke(width, list);
        } else if (drawable instanceof GradientDrawable) {
            return new StrokeDrawableWrapper((GradientDrawable) drawable, width, list);
        }
        throw new RuntimeException("Only GradientDrawable or StrokeDrawableWrapper is supported");
    }
}