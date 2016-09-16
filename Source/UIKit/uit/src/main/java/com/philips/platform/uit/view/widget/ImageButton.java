/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class ImageButton extends AppCompatButton {
    private ColorStateList drawableColorlist;
    private int drawableWidth;
    private int drawableHeight;

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
        assignDrawableProperties(typedArray);
        applyBackgroundTinting(typedArray);

        //Apply drawable should be called after getting height and width to set bounds to drawable.
        applyDrawable(typedArray);
        typedArray.recycle();
    }

    private void assignDrawableProperties(TypedArray typedArray) {
        drawableWidth = (int) typedArray.getDimension(R.styleable.UITImageButton_uitImageButtonDrawableWidth, 0.0f);
        drawableHeight = (int) typedArray.getDimension(R.styleable.UITImageButton_uitImageButtonDrawableHeight, 0.0f);

        //Store the color state list
        int resourceId = typedArray.getResourceId(R.styleable.UITImageButton_uitImageButtonDrawableColorList, -1);
        if (resourceId != -1) {
            drawableColorlist = getColorStateListFromResourceID(resourceId);
        }
    }

    private void applyBackgroundTinting(TypedArray typedArray) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UITImageButton_uitImageButtonColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            ViewCompat.setBackgroundTintList(this, getColorStateListFromResourceID(backGroundListID));
        }
    }

    private void applyDrawable(TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.UITImageButton_uitImageButtonDrawableSrc, -1);
        //We allow setting drawable programmatically too, which can be case for vectors.
        if (resourceId != -1) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId).mutate();
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (drawableColorlist != null && drawable != null) {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            drawable.invalidateSelf();
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, drawableColorlist);
        }
        setCompoundDrawables(drawable, null, null, null);
    }

    private ColorStateList getColorStateListFromResourceID(int backgroundColorStateID) {
        return ThemeUtils.buildColorStateList(getContext().getResources(), getContext().getTheme(), backgroundColorStateID);
    }
}