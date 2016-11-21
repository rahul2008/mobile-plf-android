/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class ProgressBar extends android.widget.ProgressBar {

    public enum CircularProgressBarSize {SMALL, MIDDLE, BIG}

    @NonNull private boolean isLinearProgressBarEnabled = false;
    @Nullable private CircularProgressBarSize circularProgressBarSize;

    public ProgressBar(final Context context) {
        this(context, null);
    }

    public ProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.progressBarStyle);
    }

    public ProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyleAttributes(context, attrs, defStyleAttr);

        Theme theme = ThemeUtils.getTheme(context, attrs);
        initProgressBar(theme);
    }

    private void obtainStyleAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressBar, defStyleAttr, R.style.UIDProgressBarHorizontalDeterminate);
        isLinearProgressBarEnabled = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressBar_uidLinearProgressBar, false);

        if(!isLinearProgressBarEnabled) {
            int sizeIndex = obtainStyledAttributes.getInt(R.styleable.UIDProgressBar_circularProgressBarSize, 0);
            circularProgressBarSize = CircularProgressBarSize.values()[sizeIndex];
        }

        obtainStyledAttributes.recycle();
    }

    private void initProgressBar(final Theme theme) {
        if (isLinearProgressBarEnabled) {
            initLinearProgressBar(theme);
        } else {
            initCircularProgressBar(theme);
        }
    }

    private void initLinearProgressBar(final Theme theme) {
        LayerDrawable progressBar = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.uid_secondary_progress_bar);

        final Drawable background = progressBar.findDrawableByLayerId(android.R.id.background);
        final Drawable progress = progressBar.findDrawableByLayerId(android.R.id.progress);
        final Drawable secondaryProgress = progressBar.findDrawableByLayerId(android.R.id.secondaryProgress);

        final Drawable backgroundDrawable = setTintOnDrawable(background, R.color.uit_progress_bar_background_selector, theme);
        final Drawable progressDrawable = setTintOnDrawable(progress, R.color.uit_progress_bar_progress_selector, theme);
        final Drawable secondaryProgressDrawable = setTintOnDrawable(secondaryProgress, R.color.uit_progress_bar_secondary_progress_selector, theme);

        final LayerDrawable layer = new LayerDrawable(new Drawable[]{backgroundDrawable, secondaryProgressDrawable, progressDrawable});
        layer.setId(0, android.R.id.background);
        layer.setId(1, android.R.id.secondaryProgress);
        layer.setId(2, android.R.id.progress);

        setProgressDrawable(layer);

        if (isIndeterminate()) {
            setIndeterminateDrawable(layer);
        }
    }

    private void initCircularProgressBar(final Theme theme) {
        LayerDrawable progressBar = getCircularProgressDrawableFromSize();

        final Drawable background = progressBar.findDrawableByLayerId(android.R.id.background);
        final Drawable progress = progressBar.findDrawableByLayerId(android.R.id.progress);

        final Drawable backgroundDrawable = setTintOnDrawable(background, R.color.uit_progress_bar_background_selector, theme);
        final Drawable progressDrawable = setTintOnDrawable(progress, R.color.uit_progress_bar_progress_selector, theme);

        final LayerDrawable layer = new LayerDrawable(new Drawable[]{backgroundDrawable, progressDrawable});
        layer.setId(0, android.R.id.background);
        layer.setId(1, android.R.id.progress);

        setProgressDrawable(layer);

        if (isIndeterminate()) {
            setIndeterminateDrawable(layer);
        }
    }

    private LayerDrawable getCircularProgressDrawableFromSize() {
        switch (circularProgressBarSize) {
            case SMALL:
                return (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.uid_circular_progress_bar_small);
            case MIDDLE:
                return (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.uid_circular_progress_bar_middle);
            case BIG:
                return (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.uid_circular_progress_bar_big);
        }
        return (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.uid_circular_progress_bar_small);
    }

    private int getCircularProgressBarSize(CircularProgressBarSize size) {
        int circularProgressSize = 0;
        switch (size) {
            case SMALL:
                circularProgressSize = getResources().getDimensionPixelSize(R.dimen.uid_progress_bar_circular_small);
                break;
            case MIDDLE:
                circularProgressSize = getResources().getDimensionPixelSize(R.dimen.uid_progress_bar_circular_middle);
                break;
            case BIG:
                circularProgressSize = getResources().getDimensionPixelSize(R.dimen.uid_progress_bar_circular_big);
                break;
        }

        return circularProgressSize;
    }

    private Drawable setTintOnDrawable(Drawable drawable, int tintId, Theme theme) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(getResources(), theme, tintId);
        Drawable compatDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(compatDrawable, colorStateList);
        return compatDrawable;
    }

    @Override
    protected synchronized void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isLinearProgressBarEnabled && circularProgressBarSize != null) {
            int size = getCircularProgressBarSize(circularProgressBarSize);
            setMeasuredDimension(size, size);
        }
    }
}
