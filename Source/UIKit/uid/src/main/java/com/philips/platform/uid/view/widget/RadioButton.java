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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * <p>Provides an implementation for a customized RadioButton.
 * </p>
 * <p>In order to customize the RadioButton to your own needs, it is recommended to create a new style and use UIDRadioButton style as your parrent style. <br>
 * The provided background is used for the ripple effect from lollipop onwards.</p>
 * <p>The attributes mapping follows below table.</p>
 * <table border="2" width="85%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>ResourceID</th> <th>Configuration</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">uidRadioButtonPaddingStart</td>
 * <td rowspan="1">Padding used in front of the RadioButton</td>
 * </tr>
 * <tr>
 * <td rowspan="1">paddingStart</td>
 * <td rowspan="1">Padding used between the RadioButton and label</td>
 * </tr>
 * <tr>
 * <td rowspan="1">paddingEnd</td>
 * <td rowspan="1">Padding used after the label</td>
 * </tr>
 * <tr>
 * <td rowspan="1">fontPath</td>
 * <td rowspan="1">Path used to specify your custom font</td>
 * </tr>
 * </tbody>
 * <p>
 * </table>
 */
public class RadioButton extends AppCompatRadioButton{

    private int radioButtonStartPadding = 0;

    public RadioButton(final Context context) {
        this(context, null);
    }

    public RadioButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidRadioButtonStyle);
    }

    public RadioButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        applyRadioButtonStyling(context, theme);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDRadioButton, defStyleAttr, R.style.UIDRadioButton);
        getRadioButtonPaddingStartFromAttributes(context, typedArray);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            applyRippleTint(theme);
        }
        typedArray.recycle();
    }

    private void getRadioButtonPaddingStartFromAttributes(final Context context, TypedArray typedArray) {
        radioButtonStartPadding = typedArray.getDimensionPixelSize(R.styleable.UIDRadioButton_uidRadioButtonPaddingStart,
                context.getResources().getDimensionPixelSize(R.dimen.uid_radiobutton_margin_left_right));
    }

    private void applyRadioButtonStyling(Context context, Resources.Theme theme) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(context.getResources(), theme, R.color.uid_radiobutton_text_selector);
        setTextColor(colorStateList);

        VectorDrawableCompat checkedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_enabled, theme);
        VectorDrawableCompat checkedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_disabled, theme);
        VectorDrawableCompat uncheckedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_disabled, theme);
        VectorDrawableCompat uncheckedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_enabled, theme);

        setRadioButtonDrawables(checkedEnabled, checkedDisabled, uncheckedDisabled, uncheckedEnabled);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyRippleTint(final Resources.Theme theme) {
        ColorStateList borderColorStateID = ThemeUtils.buildColorStateList(getResources(), theme, R.color.uid_radiobutton_ripple_selector);

        if (UIDUtils.isMinLollipop() && (borderColorStateID != null) && (getBackground() instanceof RippleDrawable)) {
            ((RippleDrawable) getBackground()).setColor(borderColorStateID);
            int radius = getResources().getDimensionPixelSize(R.dimen.uid_radiobutton_border_ripple_radius);
            UIDUtils.setRippleMaxRadius(getBackground(), radius);
        }
    }

    /**
     * Customize your radiobutton by providing drawables of the four possible states
     *
     * @param checkedEnabled Drawable for the checked enabled state
     * @param checkedDisabled Drawable for the checked disabled state
     * @param uncheckedDisabled Drawable for the unchecked disabled state
     * @param uncheckedEnabled Drawable for the unchecked enabled state
     */
    public void setRadioButtonDrawables(final Drawable checkedEnabled,
                                     final Drawable checkedDisabled,
                                     final Drawable uncheckedDisabled,
                                     final Drawable uncheckedEnabled) {
        setButtonDrawable(getStateListDrawable(checkedEnabled, checkedDisabled, uncheckedDisabled, uncheckedEnabled));
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

    @Override
    public void draw(final Canvas canvas) {
        canvas.save();
        canvas.translate(getStartPaddingAsPerLayoutDirection(), 0);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredWidth = getMeasuredWidth() + radioButtonStartPadding;
        setMeasuredDimension(measuredWidth, ViewCompat.getMeasuredHeightAndState(this));
        final int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        if(gravity == Gravity.CENTER_VERTICAL && getLineCount() >1) {
            setGravity(Gravity.TOP);
        }
    }

    private int getStartPaddingAsPerLayoutDirection() {
        if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            return -radioButtonStartPadding;
        } else {
            return radioButtonStartPadding;
        }
    }
}