/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.philips.platform.uid.R;

public class GradientContainer extends FrameLayout{
    public GradientContainer(@NonNull final Context context) {
        super(context);
    }

    public GradientContainer(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public GradientContainer(@NonNull final Context context, @NonNull final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGradientBackground(context, attrs, defStyleAttr);
    }

    void setGradientBackground(@NonNull final Context context, @NonNull final AttributeSet attr, final int defStyleAttr) {
        final TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.UIDGradientView, defStyleAttr, R.style.UIDGridGradientStyle);

        final int startsColor = typedArray.getColor(R.styleable.UIDGradientView_gradientStartColor, Color.TRANSPARENT);
        final int endsColor = typedArray.getColor(R.styleable.UIDGradientView_gradientEndColor, Color.BLACK);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{startsColor, endsColor});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        setBackground(gradientDrawable);
        typedArray.recycle();
    }
}
