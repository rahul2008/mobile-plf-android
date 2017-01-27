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
    @StringRes
    public final int description;

    final private Context context;
    public boolean isSelected;

    public DataHolder(@DrawableRes final int icon, @StringRes final int text, @StringRes final int description, final Context context) {
        this.icon = icon;
        this.text = text;
        this.description = description;
        this.context = context;
    }

    public Drawable getIcon() {
        return VectorDrawableCompat.create(context.getResources(), icon, context.getTheme());
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
