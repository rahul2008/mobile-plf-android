package com.philips.cdp.uikit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
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

        Drawable switchOff = VectorDrawable.create(context, R.drawable.uikit_switch_off);
        Drawable switchOn = VectorDrawable.create(context, R.drawable.uikit_switch_on_vector);

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_checked}, switchOn);
        states.addState(new int[]{}, switchOff);

        setTextOff("");
        setTextOn("");
        setTrackDrawable(states);
        setThumbResource(R.drawable.uikit_thumb);

    }

    public PuiSwitch(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
