/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.Gravity;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * <p>Provides an implementation for a customized CheckBox.
 * </p>
 * <p>In order to customize the checkbox to your own needs, it is recommended to create a new style and use UIDCheckBox style as your parrent style. <br>
 * The provided background is used for the ripple effect from lollipop onwards.</p>
 * <p>The attributes mapping follows below table.</p>
 * <table border="2" width="85%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>ResourceID</th> <th>Configuration</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">uidCheckBoxPaddingStart</td>
 * <td rowspan="1">Padding used in front of the checkbox</td>
 * </tr>
 * <tr>
 * <td rowspan="1">paddingStart</td>
 * <td rowspan="1">Padding used between the checkbox and label</td>
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
public class CheckBox extends AppCompatCheckBox {
    public CheckBox(final Context context) {
        this(context, null);
    }

    public CheckBox(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidCheckBoxStyle);
    }

    public CheckBox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Context themedContext = UIDContextWrapper.getThemedContext(context, theme);
        applyCheckBoxStyling(themedContext, theme);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);
        applyRippleTint(themedContext);
    }

    private void applyCheckBoxStyling(Context themedContext, Resources.Theme theme) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(themedContext, R.color.uid_checkbox_text_selector);
        setTextColor(colorStateList);

        VectorDrawableCompat checkedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_enabled, theme);
        VectorDrawableCompat checkedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_disabled, theme);
        VectorDrawableCompat uncheckedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_disabled, theme);
        VectorDrawableCompat uncheckedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_enabled, theme);

        setCheckBoxDrawables(checkedEnabled, checkedDisabled, uncheckedDisabled, uncheckedEnabled);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyRippleTint(Context themedContext) {
        ColorStateList borderColorStateID = ThemeUtils.buildColorStateList(themedContext, R.color.uid_checkbox_ripple_selector);

        if (UIDUtils.isMinLollipop() && (borderColorStateID != null) && (getBackground() instanceof RippleDrawable)) {
            ((RippleDrawable) getBackground()).setColor(borderColorStateID);
            int radius = getResources().getDimensionPixelSize(R.dimen.uid_checkbox_border_ripple_radius);
            UIDUtils.setRippleMaxRadius(getBackground(), radius);
        }
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

    /**
     * Customize your checkbox by providing drawables of the four possible states
     *
     * @param checkedEnabled Drawable for the checked enabled state
     * @param checkedDisabled Drawable for the checked disabled state
     * @param uncheckedDisabled Drawable for the unchecked disabled state
     * @param uncheckedEnabled Drawable for the unchecked enabled state
     */
    public void setCheckBoxDrawables(final Drawable checkedEnabled,
                                     final Drawable checkedDisabled,
                                     final Drawable uncheckedDisabled,
                                     final Drawable uncheckedEnabled) {
        setButtonDrawable(getStateListDrawable(checkedEnabled, checkedDisabled, uncheckedDisabled, uncheckedEnabled));
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), ViewCompat.getMeasuredHeightAndState(this));
        final int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        if(gravity == Gravity.CENTER_VERTICAL && getLineCount() >1) {
            setGravity(Gravity.TOP);
        }
    }
}
