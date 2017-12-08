package com.philips.cdp.uikit.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;

public class VectorDrawable {
    public static Drawable create(Context context, int rid) {
        return VectorDrawableCompat.create(context.getResources(), rid, context.getTheme());
    }
}