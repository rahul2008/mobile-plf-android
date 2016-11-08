/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

import com.philips.platform.uit.R;

public class ProgressBar extends android.widget.ProgressBar {

    private boolean isSecondaryProgressBarEnabled;
    private int themeBaseColor = getResources().getColor(R.color.uit_aqua_level_30);
    private int secondaryProgressColor = getResources().getColor(R.color.uit_aqua_level_05);

    public ProgressBar(final Context context) {
        super(context);
    }

    public ProgressBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UIKitProgressBar, 0, 0);
        isSecondaryProgressBarEnabled = a.getBoolean(R.styleable.UIKitProgressBar_uit_secondary_progress, false);
        a.recycle();
        init();
    }

    public ProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        if (isSecondaryProgressBarEnabled) {
            setProgressDrawable(initSecondaryProgressDrawable());
            setSecondaryProgress(70);
        } else {
            setProgressDrawable(initProgressDrawable());
            setIndeterminateDrawable(initProgressDrawable());
        }
        setProgress(50);
    }

    private LayerDrawable initProgressDrawable() {
        LayerDrawable progressBar = (LayerDrawable) getResources().getDrawable(R.drawable.uit_progress_bar);
        progressBar.getConstantState().newDrawable().mutate();
        initProgressBarDrawable(progressBar);
        return progressBar;
    }

    private LayerDrawable initSecondaryProgressDrawable() {
        LayerDrawable secondaryProgressbar = (LayerDrawable) getResources().getDrawable(R.drawable.uit_secondary_progress_bar);
        secondaryProgressbar.getConstantState().newDrawable().mutate();

        initProgressBarDrawable(secondaryProgressbar);

        ClipDrawable secondaryProgressBar = (ClipDrawable) secondaryProgressbar.findDrawableByLayerId(android.R.id.secondaryProgress);
        ColorFilter secondaryProgressColorFilter = new PorterDuffColorFilter(secondaryProgressColor, PorterDuff.Mode.SRC_ATOP);
        secondaryProgressBar.setColorFilter(secondaryProgressColorFilter);

        return secondaryProgressbar;
    }

    private void initProgressBarDrawable(LayerDrawable sliderbar) {
        ClipDrawable progressBar = (ClipDrawable) sliderbar.findDrawableByLayerId(android.R.id.progress);
        ColorFilter BaseColorProgressFilter = new PorterDuffColorFilter(themeBaseColor, PorterDuff.Mode.SRC_ATOP);
        progressBar.setColorFilter(BaseColorProgressFilter);
    }
}
