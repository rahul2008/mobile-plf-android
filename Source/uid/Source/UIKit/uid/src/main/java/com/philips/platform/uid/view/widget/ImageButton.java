/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.utils.UIDLocaleHelper;

public class ImageButton extends AppCompatButton {
    private ColorStateList drawableColorlist;
    private int drawableWidth;
    private int drawableHeight;

    public ImageButton(@NonNull Context context) {
        this(context, null);
    }

    public ImageButton(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uidImageButtonStyle);
    }

    public ImageButton(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDImageButton, defStyleAttr, R.style.UIDImageButton);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Context themedContext = UIDContextWrapper.getThemedContext(context, theme);

        assignDrawableProperties(themedContext, typedArray);
        applyBackgroundTinting(themedContext, typedArray);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);

        //Apply drawable should be called after getting height and width to set bounds to drawable.
        applyDrawable(typedArray);
        typedArray.recycle();
    }

    private void assignDrawableProperties(Context themedContext, @NonNull TypedArray typedArray) {
        drawableWidth = (int) typedArray.getDimension(R.styleable.UIDImageButton_uidImageButtonDrawableWidth, 0.0f);
        drawableHeight = (int) typedArray.getDimension(R.styleable.UIDImageButton_uidImageButtonDrawableHeight, 0.0f);

        //Store the color state list
        int resourceId = typedArray.getResourceId(R.styleable.UIDImageButton_uidImageButtonDrawableColorList, -1);
        if (resourceId != -1) {
            drawableColorlist = ThemeUtils.buildColorStateList(themedContext, resourceId);
        }
    }

    private void applyBackgroundTinting(Context themedContext, @NonNull TypedArray typedArray) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UIDImageButton_uidImageButtonColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            ViewCompat.setBackgroundTintList(this, ThemeUtils.buildColorStateList(themedContext, backGroundListID));
        }
    }

    private void applyDrawable(@NonNull TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.UIDImageButton_uidImageButtonDrawableSrc, -1);
        //We allow setting drawable programmatically too, which can be case for vectors.
        if (resourceId != -1) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId).mutate();
            setImageDrawable(drawable);
        }
    }

    /**
     * sets icon on button with given drawable
     *
     * @param resourceId Non-vector resource for drawable. <br> Tt will crash if vector resource id is passed
     *                   if you have vector then you can use support library api to retrieve drawable as below<br>
     *                   VectorDrawableCompat.create(getResources(), R.drawable.share, getContext().getTheme());
     *                   and call setImageDrawable()<br>
     *                   otherwise you can call setVectorResource(@resourceId - for vector drawable)
     *                   @since 3.0.0
     */
    public void setImageResource(int resourceId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId);
        setImageDrawable(drawable);
    }

    /**
     * sets icon on button with given drawable
     *
     * @param resourceId vector resource to be set as icon on button
     *                   @since 3.0.0
     */
    public void setVectorResource(int resourceId) {
        Drawable drawable = VectorDrawableCompat.create(getResources(), resourceId, getContext().getTheme());
        if (drawable != null)
            setImageDrawable(drawable);
    }

    /**
     * Sets icon on button with given drawable
     *
     * @param drawable drawable to be set as a icon on button
     *                 @since 3.0.0
     */
    public void setImageDrawable(@NonNull Drawable drawable) {
        Drawable wrappedDrawable = drawable.mutate();
        if (drawableColorlist != null) {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, drawableColorlist);
        }
        setCompoundDrawables(wrappedDrawable, null, null, null);
        invalidate();
    }
}