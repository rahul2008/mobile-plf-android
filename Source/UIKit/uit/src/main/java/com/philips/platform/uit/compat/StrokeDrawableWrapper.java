/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.compat;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class StrokeDrawableWrapper extends Drawable implements Drawable.Callback {
    private GradientDrawable delegateDrawable;
    private ColorStateList tintList;
    private int tintColor = Integer.MIN_VALUE;
    private int strokeWidth;

    public StrokeDrawableWrapper(final GradientDrawable delegateDrawable, int width, ColorStateList tintList) {
        this.delegateDrawable = (GradientDrawable) delegateDrawable.mutate();
        this.tintList = tintList;
        strokeWidth = width;

        delegateDrawable.setCallback(this);
        setBounds(delegateDrawable.getBounds());
        setState(delegateDrawable.getState());
        setVisible(delegateDrawable.isVisible(), true);
        setLevel(delegateDrawable.getLevel());
        invalidateSelf();

    }

    @Override
    public void draw(final Canvas canvas) {
        delegateDrawable.draw(canvas);
    }

    @Override
    public void setAlpha(final int alpha) {
        delegateDrawable.setAlpha(alpha);
    }

    @Override
    public void setFilterBitmap(final boolean filter) {
        delegateDrawable.setFilterBitmap(filter);
    }

    @Override
    public int getChangingConfigurations() {
        return delegateDrawable.getChangingConfigurations();
    }

    @Override
    public void setDither(final boolean dither) {
        delegateDrawable.setDither(dither);
    }

    @Override
    public boolean getPadding(final Rect padding) {
        return delegateDrawable.getPadding(padding);
    }

    @Nullable
    @Override
    public Region getTransparentRegion() {
        return delegateDrawable.getTransparentRegion();
    }

    @Override
    public boolean setState(final int[] stateSet) {
        delegateDrawable.setState(stateSet);
        int newColor = tintList.getColorForState(stateSet, Color.MAGENTA);
        if (tintColor != newColor) {
            tintColor = newColor;
            delegateDrawable.setStroke(strokeWidth, tintColor);
            delegateDrawable.invalidateSelf();
            return true;
        }
        return false;
    }

    @Override
    public void setColorFilter(final int color, final PorterDuff.Mode mode) {
        delegateDrawable.setColorFilter(color, mode);
    }

    @Override
    public void setChangingConfigurations(final int configs) {
        delegateDrawable.setChangingConfigurations(configs);
    }

    @Override
    public void setColorFilter(final ColorFilter colorFilter) {
        delegateDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return delegateDrawable.getOpacity();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Nullable
    @Override
    public ConstantState getConstantState() {
        return delegateDrawable.getConstantState();
    }

    public void setStroke(final int width, final ColorStateList list) {
        strokeWidth = width;
        tintList = list;
        delegateDrawable.invalidateSelf();
    }

    @Override
    public void invalidateDrawable(final Drawable who) {
        invalidateSelf();
    }

    @Override
    public void scheduleDrawable(final Drawable who, final Runnable what, final long when) {
        scheduleSelf(what, when);
    }

    @NonNull
    @Override
    public int[] getState() {
        return delegateDrawable.getState();
    }

    @Override
    protected void onBoundsChange(final Rect bounds) {
        delegateDrawable.setBounds(bounds);
    }

    @NonNull
    @Override
    public Drawable getCurrent() {
        return delegateDrawable.getCurrent();
    }

    @Override
    public boolean setVisible(final boolean visible, final boolean restart) {
        return delegateDrawable.setVisible(visible, restart);
    }

    @Override
    public int getIntrinsicWidth() {
        return delegateDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return delegateDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMinimumWidth() {
        return delegateDrawable.getMinimumWidth();
    }

    @Override
    public int getMinimumHeight() {
        return delegateDrawable.getMinimumHeight();
    }

    @Override
    public void unscheduleDrawable(final Drawable who, final Runnable what) {
        unscheduleSelf(what);
    }

    public int getstateColor(final int attr) {
        if(tintList != null) {
            return tintList.getColorForState(new int[]{attr}, Color.MAGENTA);
        }
        return Color.MAGENTA;
    }
}