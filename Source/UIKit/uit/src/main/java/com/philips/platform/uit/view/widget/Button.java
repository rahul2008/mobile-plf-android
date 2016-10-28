/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

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

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class Button extends AppCompatButton {

    private ColorStateList drawableColorlist;
    private int drawableWidth;
    private int drawableHeight;
    private boolean isCenterLayoutRequested;
    private Rect compoundRect = new Rect();

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

        assignDrawableProperties(typedArray, theme, attrs);
        applyBackgroundTinting(typedArray, theme);
        applyTextColorTinting(typedArray, theme);
        applyDrawable(typedArray);
        setCenterLayoutFlag(typedArray);
        typedArray.recycle();
    }

    private void applyTextColorTinting(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UITButton_uitButtonTextColorList, -1);
        if (textColorStateID != -1) {
            setTextColor(getColorStateList(textColorStateID, theme));
        }
    }

    private void applyBackgroundTinting(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UITButton_uitButtonBackgroundColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            setSupportBackgroundTintList(getColorStateList(backGroundListID, theme));
        }
    }

    private ColorStateList getColorStateList(final int backgroundColorStateID, final @NonNull Resources.Theme theme) {
        return ThemeUtils.buildColorStateList(getContext().getResources(), theme, backgroundColorStateID);
    }

    /**
     * This method can be used to set the drawable on left inside button
     *
     * @param drawable
     */
    public void setImageDrawable(Drawable drawable) {
        Drawable wrappedDrawable = drawable;
        if (drawableColorlist != null && drawable != null) {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, drawableColorlist);
        }
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(wrappedDrawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isCenterLayoutRequested) {
            final float availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();

            float textWidth = 0f;
            final Layout layout = getLayout();
            if (layout != null) {
                for (int i = 0; i < layout.getLineCount(); i++) {
                    textWidth = Math.max(textWidth, layout.getLineRight(i));
                }
            }

            Drawable leftDrawable = getCompoundDrawables()[0];
            int drawableAdjustments = 0;

            if (leftDrawable != null) {
                leftDrawable.copyBounds(compoundRect);
                drawableAdjustments = compoundRect.width() + getCompoundDrawablePadding();
            }

            canvas.save();
            canvas.translate((availableWidth - drawableAdjustments - textWidth) / 2, 0);
        }
        super.onDraw(canvas);

        if (isCenterLayoutRequested) {
            canvas.restore();
        }
    }

    private void applyDrawable(TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.UITButton_uitButtonImageDrawableSrc, -1);
//        We allow setting drawable programmatically too, which can be case for vectors.
        if (resourceId != -1) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId).mutate();
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            final Drawable[] compoundDrawables = getCompoundDrawables();
            setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        }
    }

    private void assignDrawableProperties(TypedArray typedArray, final Resources.Theme theme, final AttributeSet attr) {
        drawableWidth = (int) typedArray.getDimension(R.styleable.UITButton_uitButtonImageDrawableWidth, 24.0f);
        drawableHeight = (int) typedArray.getDimension(R.styleable.UITButton_uitButtonImageDrawableHeight, 24.0f);
        //Store the color state list
        int resourceId = typedArray.getResourceId(R.styleable.UITButton_uitButtonDrawableColorList, -1);
        if (resourceId != -1) {
            drawableColorlist = getColorStateList(resourceId, theme);
        }
    }

    //We need to set gravity to left and center vertical so that we can translate the canvas later and get proper values.
    private void setCenterLayoutFlag(TypedArray typedArray) {
        isCenterLayoutRequested = typedArray.getBoolean(R.styleable.UITButton_uidButtonCenter, false);
        if (isCenterLayoutRequested) {
            setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
    }
}
