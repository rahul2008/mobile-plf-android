/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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

    private Rect trackUIDPadding;

    //We need to extract the drawable from layer list to avoid recursive ondraw call if bounds are set
    private Drawable uidTrackDrawable;

    private boolean isMinLollipop;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };


    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchStyle);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
        isMinLollipop = UIDUtils.isMinLollipop();
    }

    @Override
    public void setTrackDrawable(final Drawable trackDrawable) {
        setUIDTrackDrawable();
        super.setTrackDrawable(trackDrawable);
    }

    //Trackdrawable paddings are not correctly applied in Kitkat in this draw.
    @Override
    public void draw(final Canvas c) {
        if (isMinLollipop) {
            applyTrackHorizontalPadding();
        }
        super.draw(c);
    }

    //Trackdrawable paddings are not correctly applied in Lollipop in this draw.
    //In several UI rotations, the track is not clipped.
    @Override
    public void onDraw(final Canvas c) {
        if (!isMinLollipop) {
            applyTrackHorizontalPadding();
        }
        super.onDraw(c);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final int[] state = getDrawableState();
        if (uidTrackDrawable != null) {
            boolean changed = uidTrackDrawable.setState(state);
            if (changed) {
                invalidate();
            }
        }
    }

    /**
     * Should be used only for testing.
     * @return Drawable that is tinted to support current theme
     */
    @SuppressWarnings("unused")
    public Drawable getUIDTrackDrawable() {
        return uidTrackDrawable;
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDSwitch, defStyleAttr, R.style.UIDSwitchStyle);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        trackUIDPadding = saveUIDTrackPaddings();
        setUIDTrackDrawable();
        applyThumbTint(typedArray, theme);
        applyTrackTint(typedArray, theme);
        applyRippleTint(typedArray, theme);
        typedArray.recycle();
    }

    private Rect saveUIDTrackPaddings() {
        Rect paddingRect = new Rect();
        int padding = getResources().getDimensionPixelSize(R.dimen.uid_switch_track_inset);
        paddingRect.left = padding;
        paddingRect.top = padding;
        paddingRect.right = padding;
        paddingRect.bottom = padding;
        return paddingRect;
    }

    private void applyTrackTint(final TypedArray typedArray, final Resources.Theme theme) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchTrackColorList, -1);
        if (textColorStateID != -1) {
            ColorStateList trackTintList = ThemeUtils.buildColorStateList(getResources(), theme, textColorStateID);
            if (uidTrackDrawable != null) {
                DrawableCompat.setTintList(uidTrackDrawable, trackTintList);
            } else {
                setTrackDrawable(DrawableCompat.wrap(getTrackDrawable()));
                setTrackTintList(trackTintList);
            }
        }
    }

    private void applyThumbTint(final TypedArray typedArray, final Resources.Theme theme) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchThumbColorList, -1);
        if (textColorStateID != -1) {
            setThumbDrawable(DrawableCompat.wrap(getThumbDrawable()));

            setThumbTintList(ThemeUtils.buildColorStateList(getResources(), theme, textColorStateID));
        }
    }

    private void setUIDTrackDrawable() {
        Drawable trackDrawable = getTrackDrawable();
        if (trackDrawable instanceof LayerDrawable &&
                ((LayerDrawable) trackDrawable).findDrawableByLayerId(R.id.uid_id_switch_track) != null) {
            uidTrackDrawable = DrawableCompat.wrap(((LayerDrawable) getTrackDrawable()).findDrawableByLayerId(R.id.uid_id_switch_track));
        } else {
            uidTrackDrawable = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyRippleTint(final TypedArray typedArray, final Resources.Theme theme) {
        int borderColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchBorderColorList, -1);
        if (UIDUtils.isMinLollipop() && (borderColorStateID > -1) && (getBackground() instanceof RippleDrawable)) {
            ((RippleDrawable) getBackground()).setColor(ThemeUtils.buildColorStateList(getResources(), theme, borderColorStateID));
            int radius = getResources().getDimensionPixelSize(R.dimen.uid_switch_border_ripple_radius);
            UIDUtils.setRippleMaxRadius(getBackground(), radius);
        }
    }

    private void applyTrackHorizontalPadding() {
        if (uidTrackDrawable != null) {
            Rect bounds = getTrackDrawable().getBounds();
            int left = bounds.left + trackUIDPadding.left;
            int right = bounds.right - trackUIDPadding.right;
            int top = bounds.top + trackUIDPadding.top;
            int bottom = bounds.bottom - trackUIDPadding.bottom;

            uidTrackDrawable.setBounds(left, top, right, bottom);
        }
    }
}