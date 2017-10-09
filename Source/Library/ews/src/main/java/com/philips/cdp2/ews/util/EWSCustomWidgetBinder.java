/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

public class EWSCustomWidgetBinder {

    @BindingAdapter("setAnimationDrawable")
    public static void setAnimationDrawable(@NonNull final ImageView imageView, @DrawableRes final Drawable drawable) {
        imageView.setImageDrawable(drawable);
        ((AnimationDrawable) imageView.getDrawable()).start();
    }
}
