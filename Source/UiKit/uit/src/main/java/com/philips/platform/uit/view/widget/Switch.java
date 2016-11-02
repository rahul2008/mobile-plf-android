/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;
import com.philips.platform.uit.utils.UIDUtils;

public class Switch extends SwitchCompat {

    private Drawable trackDrawable;
    private Rect trackPadding = new Rect();

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchStyle);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
        trackDrawable = getTrackDrawable();
        saveTrackDrawablePadding(trackDrawable);
    }

    @Override
    public void setTrackDrawable(final Drawable trackDrawable) {
        super.setTrackDrawable(trackDrawable);
        this.trackDrawable = trackDrawable;
        saveTrackDrawablePadding(trackDrawable);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        applyTrackHorizontalPadding();
        super.onDraw(canvas);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDSwitch, defStyleAttr, R.style.UIDSwitchStyle);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        applyThumbTint(typedArray, theme, attrs);
        applyTrackTint(typedArray, theme, attrs);
        applyRippleTint(typedArray, theme, attrs);
        typedArray.recycle();
    }

    private void applyTrackTint(final TypedArray typedArray, final Resources.Theme theme, final AttributeSet attrs) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchTrackColorList, -1);
        if (textColorStateID != -1) {
            setTrackDrawable(DrawableCompat.wrap(getTrackDrawable()));

            setTrackTintList(ThemeUtils.buildColorStateList(getResources(), theme, textColorStateID));
        }
    }

    private void applyThumbTint(final TypedArray typedArray, final Resources.Theme theme, final AttributeSet attrs) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchThumbColorList, -1);
        if (textColorStateID != -1) {
            setThumbDrawable(DrawableCompat.wrap(getThumbDrawable()));

            setThumbTintList(ThemeUtils.buildColorStateList(getResources(), theme, textColorStateID));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyRippleTint(final TypedArray typedArray, final Resources.Theme theme, final AttributeSet attrs) {
        int borderColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchBorderColorList, -1);
        if (UIDUtils.isMinLollipop() && (borderColorStateID > -1) && (getBackground() instanceof RippleDrawable)) {
            ((RippleDrawable) getBackground()).setColor(ThemeUtils.buildColorStateList(getResources(), theme, borderColorStateID));
            int radius = getResources().getDimensionPixelSize(R.dimen.uid_switch_border_ripple_radius);
            UIDUtils.setRippleMaxRadius(getBackground(),radius);
        }
    }


    private void saveTrackDrawablePadding(final Drawable trackDrawable) {
        if (trackDrawable != null) {
            trackDrawable.getPadding(trackPadding);
        }
    }

    protected void applyTrackHorizontalPadding() {
        if (trackDrawable != null) {
            Rect bounds = trackDrawable.getBounds();
            int left = bounds.left + trackPadding.left;
            int right = bounds.right - trackPadding.right;
            trackDrawable.setBounds(left, bounds.top, right, bounds.bottom);
        }
    }
}