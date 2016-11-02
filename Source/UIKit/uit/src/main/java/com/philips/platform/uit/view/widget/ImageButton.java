/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
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
            drawableColorlist = ThemeUtils.buildColorStateList(getContext().getResources(), getContext().getTheme(), resourceId);
        }
    }

    private void applyBackgroundTinting(TypedArray typedArray) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UITImageButton_uitImageButtonColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            ViewCompat.setBackgroundTintList(this, ThemeUtils.buildColorStateList(getContext().getResources(), getContext().getTheme(), backGroundListID));
        }
    }

    private void applyDrawable(TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.UITImageButton_uitImageButtonDrawableSrc, -1);
        //We allow setting drawable programmatically too, which can be case for vectors.
        if (resourceId != -1) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId).mutate();
            setImageDrawable(drawable);
        }
    }

    /**
     * sets icon on button
     *
     * @param resourceId Non-vector resource for drawable, it will crash if vector resource id is passed
     *                   if you have vector then you can use framework api to retrieve drawable as below
     *                   VectorDrawableCompat.create(getResources(), R.drawable.share, getContext().getTheme());
     *                   and call setImageDrawable()
     */
    public void setImageResource(int resourceId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId);
        setImageDrawable(drawable);
    }

    /**
     * sets icon on button
     *
     * @param resourceId vector resource to be set as icon on button
     */
    public void setVectorResource(int resourceId) {
        Drawable drawable = VectorDrawableCompat.create(getResources(), resourceId, getContext().getTheme());
        setImageDrawable(drawable);
    }

    /**
     * Sets icon on button with given drawable
     *
     * @param drawable drawable to be set as a icon on button
     */
    public void setImageDrawable(Drawable drawable) {
        Drawable wrappedDrawable = drawable.mutate();
        if (drawableColorlist != null && drawable != null) {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, drawableColorlist);
        }
        setCompoundDrawables(wrappedDrawable, null, null, null);
        invalidate();
    }
}