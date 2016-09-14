/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class Button extends AppCompatButton {
    public Button(Context context) {
        this(context, null);
    }

    public Button(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UITButton, defStyleAttr, R.style.UITDefaultButton);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        applyBackgroundTinting(typedArray, theme);
        applyTextColorTinting(typedArray, theme);
        typedArray.recycle();
    }

    private void applyTextColorTinting(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UITButton_uitButtonTextColorList, -1);
        if (textColorStateID != -1) {
            setTextColor(getTextColorStateList(textColorStateID, theme));
        }
    }

    private void applyBackgroundTinting(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UITButton_uitButtonBackgroundColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            setSupportBackgroundTintList(getBackgroundColorStateList(backGroundListID, theme));
        }
    }

    private ColorStateList getBackgroundColorStateList(int backgroundColorStateID, @NonNull final Resources.Theme theme) {
        return ThemeUtils.buildColorStateList(getContext().getResources(), theme, backgroundColorStateID);
    }

    private ColorStateList getTextColorStateList(int textColorStateID, @NonNull final Resources.Theme theme) {
        return ThemeUtils.buildColorStateList(getContext().getResources(), theme, textColorStateID);
    }
}
