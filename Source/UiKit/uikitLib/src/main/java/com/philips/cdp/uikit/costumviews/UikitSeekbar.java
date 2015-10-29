package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UikitSeekbar extends LinearLayout {

    ImageView indicatorImage;
    TextView indicatorText;
    SeekBar seekbar;
    FrameLayout sliderFramelayout;
    Drawable thumb;
    Context mcontext;

    public UikitSeekbar(final Context context) {
        this(context, null);
        mcontext = context;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uikit_slider, null, false);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        init(context);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {

                int padding = getResources().getDimensionPixelSize(R.dimen.slider_padding);
                int pos = padding + (thumb.getBounds().left + (thumb.getBounds().right - thumb.getBounds().left) / 2 - (indicatorImage.getWidth() / 2));
                LayoutParams mparams = (LayoutParams) sliderFramelayout.getLayoutParams();
                mparams.leftMargin = pos;

                sliderFramelayout.setLayoutParams(mparams);
                indicatorText.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });
    }

    private void setIndicatorPosition() {

        int padding = getResources().getDimensionPixelSize(R.dimen.slider_padding);
        int pos = padding + (thumb.getBounds().left + (thumb.getBounds().right - thumb.getBounds().left) / 2 - (indicatorImage.getWidth() / 2));
        LayoutParams mparams = (LayoutParams) sliderFramelayout.getLayoutParams();
        mparams.leftMargin = pos;
        sliderFramelayout.setLayoutParams(mparams);
    }

    public UikitSeekbar(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public UikitSeekbar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setIndicatorPosition();
    }

    public void init(Context context) {
        seekbar = (SeekBar) findViewById(R.id.slider);
        indicatorImage = (ImageView) findViewById(R.id.slider_indicator);
        sliderFramelayout = (FrameLayout) findViewById(R.id.slider_framelayout);
        indicatorText = (TextView) findViewById(R.id.indicator_text);
        thumb = thumShape();
        seekbar.setThumb(thumb);
        seekbar.setProgressDrawable(sliderBar());
        seekbar.setThumbOffset(0);
    }


    public ShapeDrawable thumShape() {
        ShapeDrawable thumbOval = new ShapeDrawable(new OvalShape());
        thumbOval.getPaint().setColor(ContextCompat.getColor(mcontext, R.color.uikit_white));

        thumbOval.getPaint().setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.slider_thumb_stroke_width));
        thumbOval.setIntrinsicWidth(getResources().getDimensionPixelSize(R.dimen.slider_thumb_width));
        thumbOval.setIntrinsicHeight(getResources().getDimensionPixelSize(R.dimen.slider_thumb_height));
        return thumbOval;

    }

    public LayerDrawable sliderBar() {
        LayerDrawable slidebar = (LayerDrawable) ContextCompat.getDrawable(mcontext,R.drawable.uikit_slider_bar_brightorange);
        ClipDrawable progressbar = (ClipDrawable) slidebar.findDrawableByLayerId(android.R.id.progress);
        ColorFilter progressFilter = new PorterDuffColorFilter(ContextCompat.getColor(mcontext, R.color.uikit_philips_bright_green), PorterDuff.Mode.SRC_ATOP);
        progressbar.setColorFilter(progressFilter);

        return slidebar;
    }
}
