/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.EditText;

import com.philips.platform.uit.R;
import com.philips.platform.uit.compat.StrokeCompat;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class TextEditBox extends EditText {
    private final static int DRAWABLE_FILL_INDEX = 0;
    private final static int DRAWABLE_STROKE_INDEX = 1;
    private Drawable strokeDrawable;
    private ColorStateList strokeColorStateList;
    private ColorStateList fillColorStateList;
    private Drawable fillDrawable;

    public TextEditBox(final Context context) {
        this(context, null);
    }

    public TextEditBox(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public TextEditBox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UITTextEditBox, defStyleAttr, R.style.UITTextEditBoxStyle);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        Drawable backgroundDrawable = getLayeredBackgroundDrawable(typedArray, theme);
        if (backgroundDrawable != null) {
            setBackground(getLayeredBackgroundDrawable(typedArray, theme));
        }
        typedArray.recycle();
    }

    private Drawable getLayeredBackgroundDrawable(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        Drawable fillDrawable = getFillBackgroundDrawable(typedArray, theme);
        Drawable borderDrawable = getBorderBackground(typedArray, theme);
        Drawable backgroundDrawable = fillDrawable;
        if (fillDrawable != null && borderDrawable != null) {
            backgroundDrawable = new LayerDrawable(new Drawable[]{fillDrawable, borderDrawable});
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_FILL_INDEX, R.id.uit_texteditbox_fill_drawable);
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_STROKE_INDEX, R.id.uit_texteditbox_stroke_drawable);
        }
        return backgroundDrawable;
    }

    private Drawable getBorderBackground(final @NonNull TypedArray typedArray, final Resources.Theme theme) {
        int borderDrawableID = typedArray.getResourceId(R.styleable.UITTextEditBox_uitInputTextBorderBackground, -1);
        if (borderDrawableID != -1) {
            strokeDrawable = AppCompatResources.getDrawable(getContext(), borderDrawableID);
            int borderColorStateListID = typedArray.getResourceId(R.styleable.UITTextEditBox_uitInputTextBorderBackgroundColorList, -1);
            int borderWidth = (int) typedArray.getDimension(R.styleable.UITTextEditBox_uitInputTextBorderWidth, 0f);
            if (borderColorStateListID != -1) {
                strokeColorStateList = ThemeUtils.buildColorStateList(getContext().getResources(), theme, borderColorStateListID);
                strokeDrawable = StrokeCompat.setStroke(strokeDrawable, borderWidth, strokeColorStateList);
            }
        }
        return strokeDrawable;
    }

    private Drawable getFillBackgroundDrawable(final @NonNull TypedArray typedArray, final Resources.Theme theme) {
        int fillDrawableID = typedArray.getResourceId(R.styleable.UITTextEditBox_uitInputTextFillBackground, -1);
        if (fillDrawableID != -1) {
            fillDrawable = DrawableCompat.wrap(AppCompatResources.getDrawable(getContext(), fillDrawableID));
            int fillColorStateListID = typedArray.getResourceId(R.styleable.UITTextEditBox_uitInputTextFillBackgroundColorList, -1);
            if (fillColorStateListID != -1) {
                fillColorStateList = ThemeUtils.buildColorStateList(getContext().getResources(), theme, fillColorStateListID);
                DrawableCompat.setTintList(fillDrawable, fillColorStateList);
            }
        }
        return fillDrawable;
    }
}