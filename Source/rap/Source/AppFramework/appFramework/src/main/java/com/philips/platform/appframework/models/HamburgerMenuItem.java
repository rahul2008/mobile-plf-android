/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.models;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;

import com.philips.platform.appframework.R;

public class HamburgerMenuItem {
    @DrawableRes
    public final int icon;

    public final String title;

    final private Context context;
    private final ColorStateList colorList;

    public HamburgerMenuItem(@DrawableRes final int icon, final String title, final Context context) {
        this.icon = icon;
        this.title = title;
        this.context = context;
        colorList = AppCompatResources.getColorStateList(context, R.color.uid_list_item_icon_selector);
    }

    public Drawable getIcon() {
        Drawable vectorDrawable = VectorDrawableCompat.create(context.getResources(), icon, context.getTheme());
        vectorDrawable.setTintList(colorList);
        return vectorDrawable;
    }

    public String getTitle() {
        return title;
    }
}
