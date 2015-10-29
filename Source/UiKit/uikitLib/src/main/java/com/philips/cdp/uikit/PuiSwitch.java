package com.philips.cdp.uikit;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Switch;

import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiSwitch extends Switch{
    public PuiSwitch(final Context context) {
        super(context);
    }

    public PuiSwitch(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PuiSwitch(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable switchOff = VectorDrawable.create(context, R.drawable.uikit_switch_off);
        switchOff.setColorFilter(getResources().getColor(R.color.uikit_philips_dark_blue), PorterDuff.Mode.SRC_IN);

    }
}
