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
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.EditText;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class TextEditBox extends EditText {
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

        applyBackgroundTinting(typedArray, theme);
        typedArray.recycle();
    }

    private void applyBackgroundTinting(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        int fillColorStateListID = typedArray.getResourceId(R.styleable.UITTextEditBox_uitInputTextFillColorList, -1);
        if (fillColorStateListID != -1 && getBackground() != null) {
            ColorStateList fillColorStateList = ThemeUtils.buildColorStateList(getContext().getResources(), theme, fillColorStateListID);
            Drawable drawable = getBackground();
            if(drawable instanceof LayerDrawable) {
                Drawable fillDrawable = ((LayerDrawable) drawable).findDrawableByLayerId(R.id.uit_texteditbox_fill_drawable);
                if(fillDrawable != null) {
                    DrawableCompat.setTintList(DrawableCompat.wrap(fillDrawable), fillColorStateList);
                    drawable.invalidateSelf();
                }
            }
        }
    }
}