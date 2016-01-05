package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
class PUIProgressBar extends ProgressBar {

    private Context mContext;
    private int themeBaseColor;
    private boolean secondaryProgress;
    private int veryLightBaseColor;
    private int lightBaseColor;
    private boolean whiteProgress;

    public PUIProgressBar(final Context context) {
        super(context);
        mContext = context;
    }

    public PUIProgressBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        //this(context, attrs,0);
        //super(context, attrs,android.R.attr.progressBarStyleHorizontal);
        mContext = context;
        processAttributes();
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.UIKitProgressBar, 0, 0);
        secondaryProgress = a.getBoolean(R.styleable.UIKitProgressBar_uikitsecondaryprogress, false);
        whiteProgress = a.getBoolean(R.styleable.UIKitProgressBar_uikitwhiteprogress, false);
        init(context);
    }

    public PUIProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public PUIProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, android.R.attr.progressBarStyleHorizontal, defStyleRes);
        mContext = context;
    }

    private void processAttributes() {
        TypedArray a = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.veryLightColor, R.attr.LightColor});
        themeBaseColor = a.getColor(0, ContextCompat.getColor(mContext, R.color.uikit_philips_blue));
        veryLightBaseColor = a.getColor(1, ContextCompat.getColor(mContext, R.color.uikit_philips_very_light_blue));
        lightBaseColor = a.getColor(2, ContextCompat.getColor(mContext, R.color.uikit_philips_light_blue));
        a.recycle();
    }

    private LayerDrawable sliderBar() {
        LayerDrawable sliderbar = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.uikit_progress_bar);
        sliderbar.getConstantState().newDrawable().mutate();
        ClipDrawable progressbar = (ClipDrawable) sliderbar.findDrawableByLayerId(android.R.id.progress);
        GradientDrawable background = (GradientDrawable) sliderbar.findDrawableByLayerId(android.R.id.background);
        ColorFilter BaseColorProgressFilter = new PorterDuffColorFilter(themeBaseColor, PorterDuff.Mode.SRC_ATOP);
        ColorFilter White = new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.uikit_white), PorterDuff.Mode.SRC_ATOP);
        ColorFilter Enricher4 = new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.uikit_enricher4), PorterDuff.Mode.SRC_ATOP);
        if (whiteProgress) {
            progressbar.setColorFilter(White);
            background.setColorFilter(Enricher4);
        } else {
            progressbar.setColorFilter(BaseColorProgressFilter);
        }
        return sliderbar;
    }

    private LayerDrawable secondarySliderBar() {
        LayerDrawable secondarySliderbar = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.uikit_secondary_progress_bar);
        secondarySliderbar.getConstantState().newDrawable().mutate();
        ClipDrawable progressbar = (ClipDrawable) secondarySliderbar.findDrawableByLayerId(android.R.id.progress);
        GradientDrawable background = (GradientDrawable) secondarySliderbar.findDrawableByLayerId(android.R.id.background);
        ColorFilter BaseColorProgressFilter = new PorterDuffColorFilter(themeBaseColor, PorterDuff.Mode.SRC_ATOP);
        ColorFilter White = new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.uikit_white), PorterDuff.Mode.SRC_ATOP);
        ColorFilter Enricher4 = new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.uikit_enricher4), PorterDuff.Mode.SRC_ATOP);
        if (whiteProgress) {
            progressbar.setColorFilter(White);
            background.setColorFilter(Enricher4);
        } else {
            progressbar.setColorFilter(BaseColorProgressFilter);
        }

        ClipDrawable secondaryprogressbar = (ClipDrawable) secondarySliderbar.findDrawableByLayerId(android.R.id.secondaryProgress);
        ColorFilter VeryLightProgressFilter = new PorterDuffColorFilter(veryLightBaseColor, PorterDuff.Mode.SRC_ATOP);
        ColorFilter LightProgressFilter = new PorterDuffColorFilter(lightBaseColor, PorterDuff.Mode.SRC_ATOP);
        if (whiteProgress) {
            secondaryprogressbar.setColorFilter(LightProgressFilter);
        } else {
            secondaryprogressbar.setColorFilter(VeryLightProgressFilter);
        }
        return secondarySliderbar;
    }

    private void init(Context context) {

        if (secondaryProgress) {
            setProgressDrawable(secondarySliderBar());
            setSecondaryProgress(70);
        } else {
            setProgressDrawable(sliderBar());
            setIndeterminateDrawable(sliderBar());
        }
        setProgress(50);


    }
}
