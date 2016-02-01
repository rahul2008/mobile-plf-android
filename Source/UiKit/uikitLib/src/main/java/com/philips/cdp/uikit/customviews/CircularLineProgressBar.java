/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;


public class CircularLineProgressBar extends ProgressBar {
    Context context;
    int baseColor;
    private boolean isGrayProgress;
    private boolean isWhiteProgress;
    private boolean isSmallProgress;

    public CircularLineProgressBar(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor});
        baseColor = ar.getInt(0, R.attr.brightColor);
        ar.recycle();
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UIKitProgressBarCircular, 0, 0);
        isGrayProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikitgrayprogress, false);
        isWhiteProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikittransparentprogress, false);
        isSmallProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikitcircularprogresssmall, false);
        a.recycle();
        ColorFilter BaseColorProgressFilter = new PorterDuffColorFilter(baseColor, PorterDuff.Mode.SRC_ATOP);
        AnimationDrawable ad = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.uikit_circular_line_progress);

        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner1)), 100);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner2)), 100);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner3)), 100);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner4)), 100);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner5)), 100);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner6)), 100);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner7)), 100);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner8)), 100);

        setIndeterminateDrawable(ad);
        //  setRotation((-getProgress() / 100f * 360f) - 90f);
        ad.start();


    }


}
