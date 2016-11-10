/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
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
        setBackgroundColor();

        if (isSecondaryProgressBarEnabled) {
            setProgressDrawable(initSecondaryProgressDrawable());
            setSecondaryProgress(70);
        } else if (isIndeterminate()) {
            setIndeterminateDrawable(initIndeterminateProgressDrawable());
        } else {
            setProgressDrawable(initProgressDrawable());
        }
        setProgress(50);
    }

    private LayerDrawable initProgressDrawable() {
        LayerDrawable progressBar = (LayerDrawable) getResources().getDrawable(R.drawable.uit_progress_bar);
        progressBar.getConstantState().newDrawable().mutate();

        setProgressColor();

        return progressBar;
    }

    private LayerDrawable initIndeterminateProgressDrawable() {
        LayerDrawable progressBar = (LayerDrawable) getResources().getDrawable(R.drawable.uit_progress_bar);
        progressBar.getConstantState().newDrawable().mutate();

        setIndeterminateProgressColor();

        return progressBar;
    }

    private LayerDrawable initSecondaryProgressDrawable() {
        LayerDrawable secondaryProgressbar = (LayerDrawable) getResources().getDrawable(R.drawable.uit_secondary_progress_bar);
        secondaryProgressbar.getConstantState().newDrawable().mutate();

        setProgressColor();
        setSecondaryProgressColor();

        return secondaryProgressbar;
    }

    private void setBackgroundColor() {
        int backgroundTint = R.color.uit_progress_bar_background_selector;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundTintList(ThemeUtils.buildColorStateList(getResources(), theme, backgroundTint));
        }
    }

    private void setProgressColor() {
        int progressTint = R.color.uit_progress_bar_progress_selector;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setProgressTintList(ThemeUtils.buildColorStateList(getResources(), theme, progressTint));
        }
    }

    private void setIndeterminateProgressColor() {
        int progressTint = R.color.uit_progress_bar_progress_selector;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setIndeterminateTintList(ThemeUtils.buildColorStateList(getResources(), theme, progressTint));
        }
    }

    private void setSecondaryProgressColor() {
        int secondaryProgressTint = R.color.uit_progress_bar_secondary_progress_selector;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSecondaryProgressTintList(ThemeUtils.buildColorStateList(getResources(), theme, secondaryProgressTint));
        }
    }
}
