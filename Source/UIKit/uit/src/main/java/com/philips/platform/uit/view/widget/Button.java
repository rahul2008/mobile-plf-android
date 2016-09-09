/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.philips.platform.uit.R;

public class Button extends AppCompatButton {
    public Button(Context context) {
        this(context, null);
    }

    public Button(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyBackgroundTinting();
    }

    private void applyBackgroundTinting() {
        if (getBackground() != null) {
            Drawable wrappedDrawable = DrawableCompat.wrap(getBackground());
            DrawableCompat.setTintList(wrappedDrawable, getBackgroundColorStateList());
        }
    }

    private ColorStateList getBackgroundColorStateList() {
        return AppCompatResources.getColorStateList(getContext(), R.color.uit_default_button_background_selector);
    }
}
