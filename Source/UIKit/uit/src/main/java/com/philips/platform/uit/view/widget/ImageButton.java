/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.philips.platform.uit.R;

public class ImageButton extends AppCompatImageButton {
    private ColorStateList drawableColorlist;

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

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if(drawableColorlist != null && drawable != null) {
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable,drawableColorlist);
        }
    }

    private void processAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UITImageButton, defStyleAttr, R.style.UITImageButton);
        applyBackgroundTinting(typedArray);
        applyDrawableProperties(typedArray);
        typedArray.recycle();
    }

    private void applyDrawableProperties(TypedArray typedArray) {
        applyDrawableBounds(typedArray);
        assignDrawableColorList(typedArray);
    }

    private void assignDrawableColorList(TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.UITImageButton_uitImageButtonDrawableColorList, -1);
        if (resourceId != -1) {
            drawableColorlist = getDrawableColorStateList(resourceId);
        }
    }

    private void applyDrawableBounds(TypedArray typedArray) {
        int height = (int) typedArray.getDimension(R.styleable.UITImageButton_uitImageDrawableHeight, 0.0f);
        int width = (int) typedArray.getDimension(R.styleable.UITImageButton_uitImageDrawableWidth, 0.0f);
        getDrawable().setBounds(0, 0, width, height);
    }

    private void applyBackgroundTinting(TypedArray typedArray) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UITImageButton_uitImageButtonColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            ViewCompat.setBackgroundTintList(this, getBackgroundColorStateList(backGroundListID));
        }
    }

    private ColorStateList getBackgroundColorStateList(int backgroundColorStateID) {
        return AppCompatResources.getColorStateList(getContext(), backgroundColorStateID);
    }

    private ColorStateList getDrawableColorStateList(int drawableColorStateID) {
        return AppCompatResources.getColorStateList(getContext(), drawableColorStateID);
    }
}