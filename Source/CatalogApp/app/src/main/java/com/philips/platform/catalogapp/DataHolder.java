/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;

public class DataHolder extends BaseObservable {
    @DrawableRes
    public final int icon;
    @StringRes
    public final int text;
    @StringRes
    public final int description;

    final private Context context;
    private final ColorStateList colorList;
    public boolean isSelected;

    public DataHolder(@DrawableRes final int icon, @StringRes final int text, @StringRes final int description, final Context context) {
        this.icon = icon;
        this.text = text;
        this.description = description;
        this.context = context;
        colorList = AppCompatResources.getColorStateList(context, R.color.uid_list_item_icon_selector);
    }

    public Drawable getIcon() {
        Drawable vectorDrawable = VectorDrawableCompat.create(context.getResources(), icon, context.getTheme());
        vectorDrawable.setTintList(colorList);
        return vectorDrawable;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
