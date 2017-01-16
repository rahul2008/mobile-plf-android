/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class CheckBox extends android.widget.CheckBox {
    private int checkBoxStartPadding = 0;

    public CheckBox(final Context context) {
        this(context, null);
    }

    public CheckBox(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.checkboxStyle);
    }

    public CheckBox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getCheckBoxPaddingStartFromAttributes(context, attrs, defStyleAttr);

        applyStyling(context, attrs, defStyleAttr);
    }

    private void getCheckBoxPaddingStartFromAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDCheckBox, defStyleAttr, R.style.UIDCheckBox);
        checkBoxStartPadding = typedArray.getDimensionPixelSize(R.styleable.UIDCheckBox_uidCheckBoxPaddingStart,
                context.getResources().getDimensionPixelSize(R.dimen.uid_checkbox_margin_left_right));
        typedArray.recycle();
    }

    private void applyStyling(Context context, AttributeSet attrs, int defStyleAttr) {
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        if (getTextColors() == null) {
            ColorStateList colorStateList = ThemeUtils.buildColorStateList(context.getResources(), theme, R.color.uid_checkbox_text_selector);
            setTextColor(colorStateList);
        }

        VectorDrawableCompat checkedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_enabled, theme);
        VectorDrawableCompat checkedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_disabled, theme);
        VectorDrawableCompat uncheckedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_disabled, theme);
        VectorDrawableCompat uncheckedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_enabled, theme);

        setCheckBoxDrawables(checkedEnabled, checkedDisabled, uncheckedDisabled, uncheckedEnabled);
    }

    @NonNull
    private StateListDrawable getStateListDrawable(final Drawable checkedEnabled,
                                                   final Drawable checkedDisabled,
                                                   final Drawable uncheckedDisabled,
                                                   final Drawable uncheckedEnabled
    ) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}, checkedEnabled);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}, checkedDisabled);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled, -android.R.attr.state_checked}, uncheckedDisabled);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked}, uncheckedEnabled);
        return stateListDrawable;
    }

    public void setCheckBoxDrawables(final Drawable checkedEnabled,
                                     final Drawable checkedDisabled,
                                     final Drawable uncheckedDisabled,
                                     final Drawable uncheckedEnabled) {
        setButtonDrawable(getStateListDrawable(checkedEnabled, checkedDisabled, uncheckedDisabled, uncheckedEnabled));
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        canvas.save();

        if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            canvas.translate(-checkBoxStartPadding, 0);
        } else {
            canvas.translate(checkBoxStartPadding, 0);
        }
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredWidth = getMeasuredWidth() + checkBoxStartPadding;
        setMeasuredDimension(measuredWidth, ViewCompat.getMeasuredHeightAndState(this));
    }
}
