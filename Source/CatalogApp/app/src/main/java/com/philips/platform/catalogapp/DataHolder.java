/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;

public class DataHolder extends BaseObservable {
    @DrawableRes
    public final int icon;
    @StringRes
    public final int text;
    final private Context context;

    public DataHolder(@DrawableRes final int icon, @StringRes final int text, final Context context) {
        this.icon = icon;
        this.text = text;
        this.context = context;
    }

    public Drawable getIcon() {
        return VectorDrawableCompat.create(context.getResources(), icon, context.getTheme());
    }
}
