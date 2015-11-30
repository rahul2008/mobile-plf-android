package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ImageDrawable extends ImageView{
    private ColorStateList tint;

    public ImageDrawable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public ImageDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (tint != null && tint.isStateful())
            updateTintColor();

    }

    public void setColorFilter(ColorStateList tint) {
        this.tint = tint;
        super.setColorFilter(tint.getColorForState(getDrawableState(), 0));
    }

    private void updateTintColor() {
        int color = tint.getColorForState(getDrawableState(), 0);
        setColorFilter(color);
    }


    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TintableImageView, defStyle, 0);
        tint = a.getColorStateList(R.styleable.TintableImageView_tint);
        a.recycle();
    }

    public ImageDrawable(final Context context) {
        super(context);
    }
}
