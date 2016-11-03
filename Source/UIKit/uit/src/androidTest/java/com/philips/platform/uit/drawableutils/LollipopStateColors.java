/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.drawableutils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;

import com.philips.platform.uit.utils.UITTestUtils;

public class LollipopStateColors extends KitKatStateColors {
    private static final String COLOR_STATE_LIST = "mColorStateList";
    private static final String STROKE_COLOR_STATE_LIST = "mStrokeColorStateList";

    public LollipopStateColors(Drawable d) {
        super(d);
    }

    @Override
    public int getGradientSolidColor() {
        ColorStateList solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, getSolidColorStateListFiledName());
        return getColorBasedOnAttribute(solidColors, android.R.attr.state_enabled);
    }

    @Override
    public int getStrokeSolidColor() {
        ColorStateList solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, getStrokeSolidColorStateListFiledName());
        return getColorBasedOnAttribute(solidColors, android.R.attr.state_enabled);
    }

    @Override
    public int getStrokeSolidStateColor(int attr) {
        ColorStateList solidColors = (ColorStateList) GradientDrawableUtils.getField(constantState, getStrokeSolidColorStateListFiledName());
        return getColorBasedOnAttribute(solidColors, attr);
    }

    @Override
    public int getRippleRadius() {
        return UITTestUtils.getMaxRippleRadius((RippleDrawable) drawable);
    }

    @Override
    public int getRippleColor(final int attr) {
        return getColorBasedOnAttribute(UITTestUtils.getRippleColor(constantState), attr);
    }

    protected String getSolidColorStateListFiledName() {
        return COLOR_STATE_LIST;
    }

    protected String getStrokeSolidColorStateListFiledName() {
        return STROKE_COLOR_STATE_LIST;
    }

    private int getColorBasedOnAttribute(ColorStateList colorList, int attr) {
        return colorList.getColorForState(new int[]{attr}, Color.WHITE);
    }
}
