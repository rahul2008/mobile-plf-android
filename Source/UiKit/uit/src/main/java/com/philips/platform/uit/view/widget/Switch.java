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
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;
import com.philips.platform.uit.utils.UIDUtils;

public class Switch extends SwitchCompat {

    int translate = -1;

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchStyle);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = ViewCompat.getMeasuredWidthAndState(this);
        if (widthMode == MeasureSpec.AT_MOST && getSwitchMinWidth() > 0) {
            width = Math.min(width, getSwitchMinWidth());
            final Rect inset = DrawableUtils.getOpticalBounds(getThumbDrawable());
            translate = inset.left;
        }

        setMeasuredDimension(width, ViewCompat.getMeasuredHeightAndState(this));
    }

    @Override
    public void draw(final Canvas canvas) {
        if (translate != -1) {
            canvas.save();
            canvas.translate(translate, 0);
        }
        super.draw(canvas);
        if (translate != -1) {
            canvas.restore();
        }
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
        }
    }
}