/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * UID Slider.
 * <p>
 * <P>UID Slider is an extension of AppCompatSeekBar with added styles.
 * <p>
 * <P> Usage of Slider is exactly as the AppCompatSeekBar.
 */
public class Slider extends AppCompatSeekBar {
    public Slider(Context context) {
        super(context);
    }

    public Slider(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uidSliderStyle);
    }

    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyRippleTint(context);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyRippleTint(Context themedContext) {
        ColorStateList borderColorStateID = ThemeUtils.buildColorStateList(themedContext, R.color.uid_slider_ripple_selector);

        if (UIDUtils.isMinLollipop() && (borderColorStateID != null) && (getBackground() instanceof RippleDrawable)) {
            ((RippleDrawable) getBackground()).setColor(borderColorStateID);
            int radius = getResources().getDimensionPixelSize(R.dimen.uid_slider_border_ripple_radius);
            UIDUtils.setRippleMaxRadius(getBackground(), radius);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handle = super.onTouchEvent(event);
        if (handle && event.getAction() == MotionEvent.ACTION_DOWN) {
            setPressed(true);
        }
        if (handle && event.getAction() == MotionEvent.ACTION_UP) {
            setPressed(false);
        }
        return handle;
    }
}
