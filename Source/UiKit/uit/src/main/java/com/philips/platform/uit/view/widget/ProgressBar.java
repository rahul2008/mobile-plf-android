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

    private Resources.Theme theme;
    private boolean isSecondaryProgressBarEnabled;

    public ProgressBar(final Context context) {
        super(context);
    }

    public ProgressBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        theme = ThemeUtils.getTheme(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UIKitProgressBar, 0, 0);
        isSecondaryProgressBarEnabled = a.getBoolean(R.styleable.UIKitProgressBar_uit_secondary_progress, false);
        a.recycle();

        init();
    }

    public ProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        LayerDrawable progressBar = initSecondaryProgressBarDrawable();

        Drawable background = progressBar.findDrawableByLayerId(android.R.id.background);
        Drawable progress = progressBar.findDrawableByLayerId(android.R.id.progress);
        Drawable secondaryProgress = progressBar.findDrawableByLayerId(android.R.id.secondaryProgress);

        final Drawable backgroundDrawable = setTintOnDrawable(background, R.color.uit_progress_bar_background_selector);
        final Drawable progressDrawable = setTintOnDrawable(progress, R.color.uit_progress_bar_progress_selector);
        final Drawable secondaryProgressDrawable = setTintOnDrawable(secondaryProgress, R.color.uit_progress_bar_secondary_progress_selector);

        LayerDrawable layer = new LayerDrawable(new Drawable[]{background, secondaryProgress, progressDrawable});
        layer.setId(0, android.R.id.background);
        layer.setId(1, android.R.id.secondaryProgress);
        layer.setId(2, android.R.id.progress);

        setProgressDrawable(layer);
        setBackground(backgroundDrawable);

        setProgress(50);

        if (isSecondaryProgressBarEnabled) {
            setSecondaryProgress(70);
        } else if (isIndeterminate()) {
            setIndeterminateDrawable(progressDrawable);
        }
    }

    private LayerDrawable initSecondaryProgressBarDrawable() {
        LayerDrawable secondaryProgressbar = (LayerDrawable) getResources().getDrawable(R.drawable.uit_secondary_progress_bar);
        secondaryProgressbar.getConstantState().newDrawable().mutate();

        return secondaryProgressbar;
    }

    private Drawable setTintOnDrawable(Drawable drawable, int tintId) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(getResources(), theme, tintId);
        Drawable compatDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(compatDrawable, colorStateList);
        return compatDrawable;
    }
}
