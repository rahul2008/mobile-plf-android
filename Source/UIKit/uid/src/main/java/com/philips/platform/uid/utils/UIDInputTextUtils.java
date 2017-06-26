/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.StrokeCompat;
import com.philips.platform.uid.thememanager.ThemeUtils;

import static android.support.v7.content.res.AppCompatResources.getDrawable;

public final class UIDInputTextUtils {


    private final static int DRAWABLE_FILL_INDEX = 0;
    private final static int DRAWABLE_STROKE_INDEX = 1;

    private UIDInputTextUtils(){
    }

    public static Drawable getLayeredBackgroundDrawable(@NonNull TypedArray typedArray, @NonNull final Resources.Theme theme, Context context) {
        Drawable fillDrawable = getFillBackgroundDrawable(typedArray, theme, context);
        Drawable borderDrawable = getBorderBackground(typedArray, theme, context);
        Drawable backgroundDrawable = fillDrawable;
        if (fillDrawable != null && borderDrawable != null) {
            backgroundDrawable = new LayerDrawable(new Drawable[]{fillDrawable, borderDrawable});
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_FILL_INDEX, R.id.uid_texteditbox_fill_drawable);
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_STROKE_INDEX, R.id.uid_texteditbox_stroke_drawable);
        }
        return backgroundDrawable;
    }

    public static Drawable getBorderBackground(final @NonNull TypedArray typedArray,
                                         final Resources.Theme theme, Context context) {
        int borderDrawableID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextBorderBackground, -1);
        Drawable strokeDrawable = null;
        if (borderDrawableID != -1) {
            strokeDrawable = getDrawable(context, borderDrawableID);
            int borderColorStateListID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextBorderBackgroundColorList, -1);
            int borderWidth = (int) typedArray.getDimension(R.styleable.UIDTextEditBox_uidInputTextBorderWidth, 0f);
            if (borderColorStateListID != -1) {
                ColorStateList strokeColorStateList = ThemeUtils.buildColorStateList(context.getResources(), theme, borderColorStateListID);
                strokeDrawable = StrokeCompat.setStroke(strokeDrawable, borderWidth, strokeColorStateList);
            }
        }
        return strokeDrawable;
    }

    public static Drawable getFillBackgroundDrawable(final @NonNull TypedArray typedArray,
                                               final Resources.Theme theme, Context context) {
        int fillDrawableID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextFillBackground, -1);
        Drawable fillDrawable = null;
        if (fillDrawableID != -1) {
            fillDrawable = DrawableCompat.wrap(getDrawable(context, fillDrawableID));
            int fillColorStateListID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextFillBackgroundColorList, -1);
            if (fillColorStateListID != -1) {
                ColorStateList fillColorStateList = ThemeUtils.buildColorStateList(context.getResources(), theme, fillColorStateListID);
                DrawableCompat.setTintList(fillDrawable, fillColorStateList);
            }
        }
        return fillDrawable;
    }

}
