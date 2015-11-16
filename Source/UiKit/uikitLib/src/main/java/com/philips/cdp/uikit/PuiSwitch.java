package com.philips.cdp.uikit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiSwitch extends SwitchCompat {
    public PuiSwitch(final Context context) {
        super(context);
    }

    public PuiSwitch(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        Drawable switchOff = VectorDrawable.create(context, R.drawable.uikit_switch_off);
        Drawable switchOn = VectorDrawable.create(context, R.drawable.uikit_switch_on_vector);
        setThumbResource(R.drawable.uikit_thumb);

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_checked}, switchOn);
        states.addState(new int[]{}, switchOff);

        setTextOff("");
        setTextOn("");
        setTrackDrawable(states);



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        }else {
            setBackgroundDrawable(null);
        }
    }

    public PuiSwitch(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
