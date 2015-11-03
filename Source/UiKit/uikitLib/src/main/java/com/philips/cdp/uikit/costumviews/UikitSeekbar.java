package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
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

    private ImageView indicatorImage;
    private TextView indicatorText;
    private SeekBar seekbar;
    private FrameLayout sliderFramelayout;
    private Drawable thumb;
    private Context mcontext;
    private int themeBaseColor;
    private int thumbStrokeWidth;
    private Boolean enableBubbleSlider;

    public UikitSeekbar(final Context context) {
        this(context, null);
    }

    public UikitSeekbar(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }



    public UikitSeekbar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext = context;

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.slider, 0, 0);
        enableBubbleSlider = a.getBoolean(R.styleable.slider_enableBubbleIndictor, false);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uikit_slider, null, false);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        processAttributes();
        init(context);

        if (!enableBubbleSlider) {
            indicatorImage.setVisibility(View.GONE);
            indicatorText.setVisibility(View.GONE);
        }
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
    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void processAttributes() {
        TypedArray a = mcontext.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        themeBaseColor = a.getColor(0, getResources().getColor(R.color.uikit_philips_blue));
        a.recycle();
        thumbStrokeWidth = (int) getResources().getDimension(R.dimen.slider_thumb_stroke_width);
    }

    private void setIndicatorPosition() {

        int padding = getResources().getDimensionPixelSize(R.dimen.slider_padding);
        int pos = padding + (thumb.getBounds().left + (thumb.getBounds().right - thumb.getBounds().left) / 2 - (indicatorImage.getWidth() / 2));
        LayoutParams mparams = (LayoutParams) sliderFramelayout.getLayoutParams();
        mparams.leftMargin = pos;
        sliderFramelayout.setLayoutParams(mparams);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (enableBubbleSlider) {
            setIndicatorPosition();
        }
    }

    private void init(Context context) {
        seekbar = (SeekBar) findViewById(R.id.slider);
        indicatorImage = (ImageView) findViewById(R.id.slider_indicator);
        sliderFramelayout = (FrameLayout) findViewById(R.id.slider_framelayout);
        indicatorText = (TextView) findViewById(R.id.indicator_text);
        thumb = getShapeDrawable();
        seekbar.setThumb(thumb);
        seekbar.setProgressDrawable(sliderBar());
        seekbar.setThumbOffset(0);
    }

    private LayerDrawable sliderBar() {
        LayerDrawable slidebar = (LayerDrawable) ContextCompat.getDrawable(mcontext, R.drawable.uikit_slider_bar);
        slidebar.getConstantState().newDrawable().mutate();
        ClipDrawable progressbar = (ClipDrawable) slidebar.findDrawableByLayerId(android.R.id.progress);
        ColorFilter progressFilter = new PorterDuffColorFilter(themeBaseColor, PorterDuff.Mode.SRC_ATOP);
        progressbar.setColorFilter(progressFilter);

        return slidebar;
    }
    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private GradientDrawable getShapeDrawable() {
        Resources resources = getResources();
        final GradientDrawable gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_slider_circle);
        GradientDrawable d = (GradientDrawable) gradientDrawable.getConstantState().newDrawable().mutate();
        int strokeColor = Color.argb(128, Color.red(themeBaseColor), Color.green(themeBaseColor), Color.blue(themeBaseColor));
        d.setStroke(thumbStrokeWidth, strokeColor);
        return d;
    }
}
