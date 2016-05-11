
package com.philips.cdp.uikit.customviews;
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;


public class CircularProgressbar extends ProgressBar {
    Context context;
    private boolean isGrayProgress;
    private boolean isWhiteProgress;
    private boolean isSmallProgress;
    private boolean isThemeable;
    private int baseColor;


    public CircularProgressbar(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_brightColor ,R.attr.uikit_LightColor});
        baseColor = ar.getInt(0, R.attr.uikit_brightColor);
        ar.recycle();
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UIKitProgressBarCircular, 0, 0);
        isGrayProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikit_grayprogress, false);
        isWhiteProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikit_transparentprogress, false);
        isSmallProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikit_circularprogresssmall, false);
        isThemeable = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikit_themeable, false);
        a.recycle();
        setProgressDrawable(getCircularProgressDrawable());
        setRotation((-getProgress() / 100f * 360f) - 90f); // to start rotation from 90 degree from the top
    }

    private Drawable getCircularProgressDrawable() {
        LayerDrawable circularProgressDrawable;
        if (isSmallProgress) {
            circularProgressDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.uikit_circular_progress_small);
        } else {
            circularProgressDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.uikit_circular_progress);
        }

        if(isSmallProgress && !isThemeable){
            circularProgressDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.uikit_circular_progress_pb_small);
        }
        circularProgressDrawable = (LayerDrawable) circularProgressDrawable.getConstantState().newDrawable().mutate();
        Drawable progressbar = circularProgressDrawable.findDrawableByLayerId(android.R.id.progress);
        Drawable background = circularProgressDrawable.findDrawableByLayerId(android.R.id.background);
        ColorFilter baseColorProgressFilter;
        if(isThemeable) {
            baseColorProgressFilter = new PorterDuffColorFilter(baseColor, PorterDuff.Mode.SRC_ATOP);
        }else {
            baseColorProgressFilter = new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.uikit_white), PorterDuff.Mode.SRC_ATOP);
        }
        ColorFilter white = new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.uikit_white), PorterDuff.Mode.SRC_ATOP);
        ColorFilter enricher6 = new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.uikit_enricher6), PorterDuff.Mode.SRC_ATOP);
        ColorFilter enricher4 = new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.uikit_enricher4), PorterDuff.Mode.SRC_ATOP);
        if (isWhiteProgress) {
            progressbar.setColorFilter(baseColorProgressFilter);
            background.setColorFilter(white);
        } else if (isSmallProgress) {
            progressbar.setColorFilter(baseColorProgressFilter);
            background.setColorFilter(enricher4);
        } else {
            progressbar.setColorFilter(baseColorProgressFilter);
            background.setColorFilter(enricher6);
        }
        return circularProgressDrawable;
    }
}