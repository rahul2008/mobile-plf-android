/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class ProgressBar extends android.widget.ProgressBar {

    public enum CircularProgressBarSize {SMALL, MIDDLE, BIG};

    private Resources.Theme theme;
    private boolean isLinearProgressBarEnabled = false;
    private CircularProgressBarSize circularProgressBarSize = CircularProgressBarSize.SMALL;
    private LayerDrawable progressBar;

    public ProgressBar(final Context context) {
        this(context, null);
    }

    public ProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.progressBarStyle);
    }

    public ProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        theme = ThemeUtils.getTheme(context, attrs);

        obtainStyleAttributes(context, attrs, defStyleAttr);

        initProgressBar();
    }

    private void obtainStyleAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressBar, defStyleAttr, R.style.UIDProgressBarHorizontalDeterminate);
        isLinearProgressBarEnabled = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressBar_uid_linear_progress_bar, false);
        int sizeIndex = obtainStyledAttributes.getInt(R.styleable.UIDProgressBar_circularProgressBarSize, 0);
        circularProgressBarSize = CircularProgressBarSize.values()[sizeIndex];
        obtainStyledAttributes.recycle();
    }

    private void initProgressBar() {
        if (isLinearProgressBarEnabled) {
            initLinearProgressBar();
        } else {
            initCircularProgressBar();
        }
    }

    private void initCircularProgressBar() {
        progressBar = getCircularProgressDrawableFromSize();

        final Drawable background = progressBar.findDrawableByLayerId(android.R.id.background);
        final Drawable progress = progressBar.findDrawableByLayerId(android.R.id.progress);

        final Drawable backgroundDrawable = setTintOnDrawable(background, R.color.uit_progress_bar_background_selector);
        final Drawable progressDrawable = setTintOnDrawable(progress, R.color.uit_progress_bar_progress_selector);

        final LayerDrawable layer = new LayerDrawable(new Drawable[]{backgroundDrawable, progressDrawable});
        layer.setId(0, android.R.id.background);
        layer.setId(1, android.R.id.progress);

        setProgressDrawable(layer);

        if (isIndeterminate()) {
            setIndeterminateDrawable(layer);
        }

        getCircularProgressBarSize(circularProgressBarSize);
    }

    private LayerDrawable getCircularProgressDrawableFromSize() {
        switch (circularProgressBarSize) {
            case SMALL:
                return (LayerDrawable) getResources().getDrawable(R.drawable.uid_circular_progress_bar_small);
            case MIDDLE:
                return (LayerDrawable) getResources().getDrawable(R.drawable.uid_circular_progress_bar_middle);
            case BIG:
                return (LayerDrawable) getResources().getDrawable(R.drawable.uid_circular_progress_bar_big);
        }
        return (LayerDrawable) getResources().getDrawable(R.drawable.uid_circular_progress_bar_small);
    }

    private void initLinearProgressBar() {
        progressBar = (LayerDrawable) getResources().getDrawable(R.drawable.uid_secondary_progress_bar);

        final Drawable background = progressBar.findDrawableByLayerId(android.R.id.background);
        final Drawable progress = progressBar.findDrawableByLayerId(android.R.id.progress);
        final Drawable secondaryProgress = progressBar.findDrawableByLayerId(android.R.id.secondaryProgress);

        final Drawable backgroundDrawable = setTintOnDrawable(background, R.color.uit_progress_bar_background_selector);
        final Drawable progressDrawable = setTintOnDrawable(progress, R.color.uit_progress_bar_progress_selector);
        final Drawable secondaryProgressDrawable = setTintOnDrawable(secondaryProgress, R.color.uit_progress_bar_secondary_progress_selector);

        final LayerDrawable layer = new LayerDrawable(new Drawable[]{backgroundDrawable, secondaryProgressDrawable, progressDrawable});
        layer.setId(0, android.R.id.background);
        layer.setId(1, android.R.id.secondaryProgress);
        layer.setId(2, android.R.id.progress);

        setProgressDrawable(layer);

        if (isIndeterminate()) {
            setIndeterminateDrawable(layer);
        }
    }

    private Drawable setTintOnDrawable(Drawable drawable, int tintId) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(getResources(), theme, tintId);
        Drawable compatDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(compatDrawable, colorStateList);
        return compatDrawable;
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

    @Override
    protected synchronized void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (!isLinearProgressBarEnabled) {
            int size = getCircularProgressBarSize(circularProgressBarSize);
            setMeasuredDimension(size, size);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
