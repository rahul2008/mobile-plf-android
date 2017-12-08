/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;


public class CircularLineProgressBar extends ProgressBar {
    Context context;


    public CircularLineProgressBar(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.UiKit_ProgressSpinner, 0, 0);
       int updateFrequency  = a.getInteger(R.styleable.UiKit_ProgressSpinner_uikit_duration, 250);
        a.recycle();
        startAnimation(updateFrequency);
    }

    public void startAnimation(int updateFrequency) {

        int duration = updateFrequency > 0 ? updateFrequency : 250;

        AnimationDrawable ad = (AnimationDrawable) ContextCompat.getDrawable(context, (R.drawable.uikit_circular_line_progress)).getConstantState().newDrawable();

        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner1)), duration);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner2)), duration);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner3)), duration);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner4)), duration);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner5)), duration);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner6)), duration);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner7)), duration);
        ad.addFrame((VectorDrawable.create(context, R.drawable.uikit_progressbar_spinner8)), duration);
        setIndeterminateDrawable(ad);
        setIndeterminate(true);
        ad.start();
    }
}
