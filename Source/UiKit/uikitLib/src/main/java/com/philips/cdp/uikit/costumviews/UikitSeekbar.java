package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.wnafee.vector.compat.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UikitSeekbar extends LinearLayout {

    ImageView indicatorImage;
    TextView indicatorText;
    SeekBar seekbar;
    FrameLayout sliderFramelayout;

    public UikitSeekbar(final Context context) {
        this(context, null);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uikit_slider, null, false);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        seekbar = (SeekBar) findViewById(R.id.seekbar);
        indicatorImage = (ImageView) findViewById(R.id.indicator_image);
        sliderFramelayout = (FrameLayout) findViewById(R.id.slider_framelayout);
        indicatorText = (TextView) findViewById(R.id.indicator_text);
        //indicatorImage.setImageDrawable(VectorDrawable.create(getResources(), R.drawable.uikit_bubble));





        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                int padding = getResources().getDimensionPixelSize(R.dimen.slider_padding);

                int pos = padding + (seekbar.getThumb().getBounds().left + (seekbar.getThumb().getBounds().right - seekbar.getThumb().getBounds().left) / 2 - (indicatorImage.getWidth() / 2));
                LayoutParams mparams = (LayoutParams) sliderFramelayout.getLayoutParams();
                mparams.leftMargin = pos;
                indicatorText.setText(String.valueOf(progress));
                sliderFramelayout.setLayoutParams(mparams);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });
    }
    public UikitSeekbar(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public UikitSeekbar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
