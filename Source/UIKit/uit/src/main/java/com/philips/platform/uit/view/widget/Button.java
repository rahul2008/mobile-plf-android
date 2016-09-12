/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.philips.platform.uit.R;

public class Button extends AppCompatButton {
    public Button(Context context) {
        this(context, null);
    }

    public Button(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UITButton, defStyleAttr, R.style.UITDefaultButton);
        applyBackgroundTinting(typedArray);
        applyTextColorTinting(typedArray);
        typedArray.recycle();
    }

    private void applyTextColorTinting(TypedArray typedArray) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UITButton_uitButtonTextColorList, -1);
        if (textColorStateID != -1) {
            setTextColor(getTextColorStateList(textColorStateID));
        }
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

    private ColorStateList getTextColorStateList(int textColorStateID) {
        return AppCompatResources.getColorStateList(getContext(), textColorStateID);
    }
}
