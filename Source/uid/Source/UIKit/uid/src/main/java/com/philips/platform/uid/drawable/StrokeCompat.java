/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.drawable;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

public class StrokeCompat {

    public static Drawable setStroke(Drawable drawable, int width, ColorStateList list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setStroke(width, list);
            return drawable;
        }
        throw new RuntimeException("Only GradientDrawable is supported");
    }
}