/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.drawableutils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;

import com.philips.platform.uid.utils.UIDTestUtils;

public class LollipopStateColors extends KitKatStateColors {
    private static final String TINT_COLOR_FIELD = "mTint";
    private static final String COLOR_STATE_LIST = "mColorStateList";
    private static final String STROKE_COLOR_STATE_LIST = "mStrokeColorStateList";

    public LollipopStateColors(Drawable d) {
        super(d);
    }

    @Override
    public int getDefaultColor() {
        ColorStateList solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, getSolidColorStateListFiledName());
        if (solidColors == null) {
            solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, TINT_COLOR_FIELD);
        }
        return solidColors.getDefaultColor();
    }

    @Override
    public int getStateColor(int[] attr) {
        ColorStateList solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, getSolidColorStateListFiledName());
        return getColorBasedOnAttribute(solidColors, attr);
    }

    @Override
    public int getStrokeSolidColor() {
        ColorStateList solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, getStrokeSolidColorStateListFiledName());
        return getColorBasedOnAttribute(solidColors, new int[] {android.R.attr.state_enabled});
    }

    @Override
    public int getStrokeSolidStateColor(int attr) {
        return getStrokeSolidStateColor(new int[attr]);
    }

    @Override
    public int getRippleRadius() {
        return UIDTestUtils.getMaxRippleRadius((RippleDrawable) drawable);
    }

    @Override
    public int getRippleColor(final int attr) {
        return getColorBasedOnAttribute(UIDTestUtils.getRippleColor(constantState), new int[]{attr});
    }

    @Override
    public int getStrokeSolidStateColor(int[] attr) {
        ColorStateList solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, getStrokeSolidColorStateListFiledName());
        return getColorBasedOnAttribute(solidColors, attr);
    }

    protected String getSolidColorStateListFiledName() {
        return COLOR_STATE_LIST;
    }

    protected String getStrokeSolidColorStateListFiledName() {
        return STROKE_COLOR_STATE_LIST;
    }

    private int getColorBasedOnAttribute(ColorStateList colorList, int []attrs) {
        return colorList.getColorForState(attrs, Color.WHITE);
    }
}
