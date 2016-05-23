package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 * <p/>
 * Helper class if the compound drawables needs to be tinted.
 * Otherwise behaves as android base button.
 */
public class UIKitButton extends AppCompatButton {

    private ColorStateList mTintList;

    public UIKitButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initTintList();
        wrapAllDrawables();
    }

    @Override
    public void setCompoundDrawables(final Drawable left, final Drawable top, final Drawable right, final Drawable bottom) {
        super.setCompoundDrawables(wrap(left), wrap(top), wrap(right), wrap(bottom));
    }

    @Override
    public void setCompoundDrawablesRelative(final Drawable start, final Drawable top, final Drawable end, final Drawable bottom) {
        super.setCompoundDrawablesRelative(wrap(start), wrap(top), wrap(end), wrap(bottom));
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(final Drawable start, final Drawable top, final Drawable end, final Drawable bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(wrap(start), wrap(top), wrap(end), wrap(bottom));
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(final Drawable left, final Drawable top, final Drawable right, final Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(wrap(left), wrap(top), wrap(right), wrap(bottom));
    }

    /**
     * Provide custom tint for the compound Drawables.
     *
     * @param tint
     */
    public void setCompoundDrawableTintList(final ColorStateList tint) {
        mTintList = tint;
        wrapAllDrawables();
        invalidate();
    }

    private void initTintList() {
        TypedArray a = getContext().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor, R.attr.uikit_darkerColor});
        if (a != null) {
            int normalColor = a.getColor(0, Color.WHITE);
            int pressedColor = a.getColor(1, Color.WHITE);

            int[][] states = {{android.R.attr.state_pressed}, {}};
            int[] colors = {pressedColor, normalColor};

            mTintList = new ColorStateList(states, colors);
            a.recycle();
        }
    }

    private void wrapAllDrawables() {
        final Drawable[] drawables = getCompoundDrawables();
        if (drawables[0] != null) {
            wrap(drawables[0]);
        }
        if (drawables[1] != null) {
            wrap(drawables[1]);
        }
        if (drawables[2] != null) {
            wrap(drawables[2]);
        }
        if (drawables[3] != null) {
            wrap(drawables[3]);
        }
    }

    private Drawable wrap(Drawable d) {
        if (d == null) return null;

        Drawable wrappedDrawable = DrawableCompat.wrap(d).mutate();
        wrappedDrawable.setBounds(d.getBounds());
        if (wrappedDrawable instanceof DrawableWrapper) {
           ((DrawableWrapper) wrappedDrawable).setTintList(mTintList);
           ((DrawableWrapper) wrappedDrawable).setTintMode(PorterDuff.Mode.SRC_ATOP);
        } else {

            DrawableCompat.setTintList(wrappedDrawable, mTintList);
        }
        return wrappedDrawable;
    }
}
