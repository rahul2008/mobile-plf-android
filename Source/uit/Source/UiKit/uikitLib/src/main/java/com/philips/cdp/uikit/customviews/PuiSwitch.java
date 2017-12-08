/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * Switch as per Philips guidelines.
 * <p>
 *     Example:
 *     <pre>
 *                     &lt;com.philips.cdp.uikit.customviews.PuiSwitch
 *                          android:layout_width="wrap_content"
 *                          android:layout_height="wrap_content"/&gt;
 *     </pre>
 *
 *   <H3>UI Appearance</H3>
 *      <img src="../../../../../img/puiswitch_on_off.png"
 *      alt="PuiSwitch." border="0" /></p>
 * </p>
 *
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
