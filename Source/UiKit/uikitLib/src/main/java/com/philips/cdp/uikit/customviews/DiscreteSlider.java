package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DiscreteSlider extends BaseSlider implements SeekBar.OnSeekBarChangeListener {

    private int radius;
    private int baseSliderPadding;
    private int noDiscretePoint;

    public DiscreteSlider(final Context context) {
        super(context);
    }

    public DiscreteSlider(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnSeekBarChangeListener(this);
        init();

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.slider, 0, 0);
        noDiscretePoint = a.getInteger(0, 5);
    }

    public DiscreteSlider(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnSeekBarChangeListener(this);
        init();
    }

    public DiscreteSlider(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (noDiscretePoint == 1) {
            return;
        }
        int length = getMeasuredWidth() - baseSliderPadding;
        int scale = (length - (2 * radius * noDiscretePoint)) / (noDiscretePoint - 1);

        canvas.save();
        int y = (getThumbDrawable().getBounds().bottom + getThumbDrawable().getBounds().top) / 2;
        int x = getScrollX() + baseSliderPadding;

        Paint P = new Paint();
        P.setColor(Color.WHITE);
        P.setFlags(Paint.ANTI_ALIAS_FLAG);

        x = x + radius;
        canvas.drawCircle(x, y, radius, P);
        int i = 1;
        x = x + radius;

        for (; i < noDiscretePoint - 1; i++) {

            x = x + scale + radius;
            canvas.drawCircle(x, y, radius, P);
            x = x + radius;
        }

        canvas.drawCircle(length, y, radius, P);
        canvas.restore();
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {

        if (noDiscretePoint == 1) {
            return;
        }

        double scalefactor = 100.00 / noDiscretePoint;
        double setfactor = (double) (100.00 / (noDiscretePoint - 1));
        int discretePoint = (int) (progress / scalefactor);
        int setValue = (int) Math.round(discretePoint * setfactor);

        if (setValue > 100) {
            setValue = 100;
        }

        setProgress(setValue);
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    private void init() {
        radius = getResources().getDimensionPixelSize(R.dimen.discreteslider_circle_radius);
        baseSliderPadding = getResources().getDimensionPixelSize(R.dimen.slider_indicator_margin);
    }
}
