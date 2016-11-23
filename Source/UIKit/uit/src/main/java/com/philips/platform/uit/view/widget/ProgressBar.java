/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

public class ProgressBar extends android.widget.ProgressBar {

    public enum CircularProgressBarSize {SMALL, MIDDLE, BIG}

    @NonNull
    private boolean isLinearProgressBarEnabled = false;

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
        obtainStyledAttributes.recycle();
    }

    private void initProgressBar(final Theme theme) {
        if (isLinearProgressBarEnabled) {
            initLinearProgressBar(theme);
        } else {
            if (isIndeterminate()) {
                initIndeterminateCircularProgressBar(theme);
            } else {
                initCircularProgressBar(theme);
            }
        }
    }

    private void initLinearProgressBar(final Theme theme) {
        final LayerDrawable progressBarDrawable = (LayerDrawable) getProgressDrawable();

        final Drawable background = progressBarDrawable.findDrawableByLayerId(android.R.id.background);
        final Drawable progress = progressBarDrawable.findDrawableByLayerId(android.R.id.progress);
        final Drawable secondaryProgress = progressBarDrawable.findDrawableByLayerId(android.R.id.secondaryProgress);

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
        final LayerDrawable progressBarDrawable = (LayerDrawable) getProgressDrawable();
        final Drawable background = progressBarDrawable.findDrawableByLayerId(android.R.id.background);
        final Drawable progress = progressBarDrawable.findDrawableByLayerId(android.R.id.progress);

        final Drawable backgroundDrawable = setTintOnDrawable(background, R.color.uit_progress_bar_background_selector, theme);
        final Drawable progressDrawable = setTintOnDrawable(progress, R.color.uit_progress_bar_progress_selector, theme);

        final LayerDrawable layer = createCircularProgressBarLayerDrawable(progressDrawable, backgroundDrawable);

        setProgressDrawable(layer);
    }

    private void initIndeterminateCircularProgressBar(final Theme theme) {
        final LayerDrawable progressBarDrawable = (LayerDrawable) getIndeterminateDrawable();
        final Drawable background = progressBarDrawable.findDrawableByLayerId(android.R.id.background);
        final Drawable progress = progressBarDrawable.findDrawableByLayerId(android.R.id.progress);

        final Drawable backgroundDrawable = DrawableCompat.wrap(background);
        DrawableCompat.setTint(backgroundDrawable, Color.TRANSPARENT);
        setGradientOnProvidedDrawable((RotateDrawable) progress, theme);

        final LayerDrawable layer = createCircularProgressBarLayerDrawable(progress, backgroundDrawable);
        setIndeterminateDrawable(layer);
    }

    @NonNull
    private LayerDrawable createCircularProgressBarLayerDrawable(@NonNull final Drawable progress, @NonNull final Drawable backgroundDrawable) {
        final LayerDrawable layer = new LayerDrawable(new Drawable[]{backgroundDrawable, progress});
        layer.setId(0, android.R.id.background);
        layer.setId(1, android.R.id.progress);
        return layer;
    }

    private void setGradientOnProvidedDrawable(@NonNull final RotateDrawable progress, final Theme theme) {
        GradientDrawable gradientDrawable = (GradientDrawable) progress.getDrawable();
        gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);

        int startColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int endcolor = obtainCircularEndColorFromStyle(theme);

        gradientDrawable.setColors(new int[]{startColor, endcolor});
    }

    private int obtainCircularEndColorFromStyle(final Theme theme) {
        final TypedArray typedArray = theme.obtainStyledAttributes(R.styleable.UIDProgressBar);
        int endcolor = typedArray.getColor(R.styleable.UIDProgressBar_uidProgressBarCircularEndColor, Color.BLUE);
        typedArray.recycle();
        return endcolor;
    }

    private Drawable setTintOnDrawable(Drawable drawable, int tintId, Theme theme) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(getResources(), theme, tintId);
        Drawable compatDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(compatDrawable, colorStateList);
        return compatDrawable;
    }
}
