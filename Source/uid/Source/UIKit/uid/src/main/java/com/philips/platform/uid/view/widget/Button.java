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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDUtils;

public class Button extends AppCompatButton {

    private ColorStateList drawableColorlist;
    private int drawableWidth;
    private int drawableHeight;
    private boolean isCenterLayoutRequested;
    private Rect compoundRect = new Rect();

    public Button(@NonNull Context context) {
        this(context, null);
    }

    public Button(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uidButtonStyle);
    }

    public Button(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDButton, defStyleAttr, R.style.UIDDefaultButton);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);

        Context themedContext = UIDContextWrapper.getThemedContext(context, theme);

        assignDrawableProperties(typedArray, themedContext);
        applyBackgroundTinting(typedArray, themedContext);
        applyTextColorTinting(typedArray, themedContext);
        applyDrawable(typedArray);
        setCenterLayoutFlag(typedArray);
        typedArray.recycle();
    }

    private void assignDrawableProperties(@NonNull TypedArray typedArray, @NonNull final Context themedContext) {
        int defaultMinWidth = getResources().getDimensionPixelSize(R.dimen.uid_imagebutton_image_size);
        drawableWidth = typedArray.getDimensionPixelSize(R.styleable.UIDButton_uidButtonImageDrawableWidth, defaultMinWidth);
        drawableHeight = typedArray.getDimensionPixelSize(R.styleable.UIDButton_uidButtonImageDrawableHeight, defaultMinWidth);
        int resourceId = typedArray.getResourceId(R.styleable.UIDButton_uidButtonDrawableColorList, -1);
        if (resourceId != -1) {
            drawableColorlist = ThemeUtils.buildColorStateList(themedContext, resourceId);
        }
    }

    private void applyBackgroundTinting(@NonNull TypedArray typedArray, @NonNull final Context themedContext) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UIDButton_uidButtonBackgroundColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            setBackgroundTintList(ThemeUtils.buildColorStateList(themedContext, backGroundListID));
        }
    }

    private void applyTextColorTinting(@NonNull TypedArray typedArray, @NonNull final Context themedContext) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UIDButton_uidButtonTextColorList, -1);
        if (textColorStateID != -1) {
            setTextColor(ThemeUtils.buildColorStateList(themedContext, textColorStateID));
        }
    }

    private void applyDrawable(@NonNull TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.UIDButton_uidButtonImageDrawableSrc, -1);
        // We allow setting drawable programmatically too, which can be case for vectors.
        if (resourceId != -1) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId).mutate();
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            final Drawable[] compoundDrawables = getCompoundDrawables();
            Drawable wrappedCompatDrawable = DrawableCompat.wrap(drawable);
            if (drawableColorlist != null) {
                DrawableCompat.setTintList(wrappedCompatDrawable, drawableColorlist);
            }
            setCompoundDrawablesRelativeWithIntrinsicBounds(wrappedCompatDrawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        }
    }

    //We need to set gravity to left and center vertical so that we can translate the canvas later and get proper values.
    private void setCenterLayoutFlag(@NonNull TypedArray typedArray) {
        isCenterLayoutRequested = typedArray.getBoolean(R.styleable.UIDButton_uidButtonCenter, false);
        if (isCenterLayoutRequested) {
            setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        }
    }

    /**
     * This method can be used to set the drawable on left inside button
     *
     * @param drawable drawable to be set as a icon on button
     *                 @since 3.0.0
     */
    public void setImageDrawable(Drawable drawable) {
        Drawable wrappedDrawable = drawable;
        if (drawableColorlist != null && drawable != null) {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, drawableColorlist);
        }
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawablesRelativeWithIntrinsicBounds(wrappedDrawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (isCenterLayoutRequested) {
            final float availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();

            float textWidth = 0f;
            final Layout layout = getLayout();
            if (layout != null) {
                for (int i = 0; i < layout.getLineCount(); i++) {
                    if(UIDUtils.isLayoutRTL(this)){
                        textWidth = Math.max(textWidth, layout.getLineLeft(i));
                    } else {
                        textWidth = Math.max(textWidth, layout.getLineRight(i));
                    }
                }
            }

            Drawable leftDrawable = getCompoundDrawables()[0];
            int drawableAdjustments = 0;

            if (leftDrawable != null) {
                leftDrawable.copyBounds(compoundRect);
                drawableAdjustments = compoundRect.width() + getCompoundDrawablePadding();
            }

            canvas.save();
            if(UIDUtils.isLayoutRTL(this)){
                canvas.translate(-(textWidth) / 2, 0);
            } else {
                canvas.translate((availableWidth - drawableAdjustments - textWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);

        if (isCenterLayoutRequested) {
            canvas.restore();
        }
    }
}
