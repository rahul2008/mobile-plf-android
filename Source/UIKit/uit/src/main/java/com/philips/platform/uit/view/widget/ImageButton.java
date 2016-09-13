/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.philips.platform.uit.R;

public class ImageButton extends AppCompatImageButton {
    public ImageButton(Context context) {
        this(context, null);
    }

    public ImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.imageButtonStyle);
    }

    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UITImageButton, defStyleAttr, R.style.UITImageButton);
        applyBackgroundTinting(typedArray);
        typedArray.recycle();
    }

    private void applyBackgroundTinting(TypedArray typedArray) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UITButton_uitButtonBackgroundColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            setSupportBackgroundTintList(getBackgroundColorStateList(backGroundListID));
        }
    }

    private ColorStateList getBackgroundColorStateList(int backgroundColorStateID) {
        return AppCompatResources.getColorStateList(getContext(), backgroundColorStateID);
    }
}