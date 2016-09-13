/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class MarshmallowStateColors extends LollipopStateColors {
    private static final String COLOR_STATE_LIST = "mTint";
    private static final String SOLID_COLOR_STATE_LIST = "mSolidColors";
    private static final String STROKE_COLOR_STATE_LIST = "mStrokeColors";

    public MarshmallowStateColors(Drawable d) {
        super(d);
    }

    @Override
    public int getStateColor(int attr) {
        ColorStateList mSolidColorStateList = (ColorStateList) GradientDrawableCompat.getField(constantState, COLOR_STATE_LIST);
        return mSolidColorStateList.getColorForState(new int[]{attr}, Color.WHITE);
    }

    @Override
    protected String getSolidColorStateListFiledName() {
        return SOLID_COLOR_STATE_LIST;
    }

    @Override
    protected String getStrokeSolidColorStateListFiledName() {
        return STROKE_COLOR_STATE_LIST;
    }
}
