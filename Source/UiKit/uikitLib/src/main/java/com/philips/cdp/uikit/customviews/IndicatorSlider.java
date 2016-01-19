package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * <br>
 1.To use IndicatorSlider in XML file  include below convention as per your required display metrics
 <br>
 &lt;com.philips.cdp.uikit.customviews.IndicatorSlider
 android:id="@+id/indicatorslider"
 android:layout_width="280dp"
 android:layout_height="wrap_content"
 android:layout_gravity="center"
 android:layout_marginTop="10dp"></com.philips.cdp.uikit.customviews.IndicatorSlider&gt;
 *
 *
 */
public class IndicatorSlider extends LinearLayout {

    private ImageView indicatorImage;
    private TextView indicatorText;
    private BaseSlider seekbar;
    private FrameLayout sliderFramelayout;
    private OnIndicatorSliderChangeListener listener;

    public IndicatorSlider(final Context context) {
        this(context, null);
    }

    public IndicatorSlider(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }


    public IndicatorSlider(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uikit_slider, null, false);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        init(context);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {

                int padding = getResources().getDimensionPixelSize(R.dimen.slider_padding);
                int pos = padding + (seekbar.getThumbDrawable().getBounds().left + (seekbar.getThumbDrawable().getBounds().right - seekbar.getThumbDrawable().getBounds().left) / 2 - (indicatorImage.getWidth() / 2));
                LayoutParams mparams = (LayoutParams) sliderFramelayout.getLayoutParams();
                mparams.leftMargin = pos;
                sliderFramelayout.setLayoutParams(mparams);
                indicatorText.setText(String.valueOf(progress));

                if (listener !=null) {
                    listener.onProgressChanged(seekBar, progress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
                if (listener !=null) {
                    listener.onStartTrackingTouch(seekBar);
                }

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if (listener !=null) {
                    listener.onStartTrackingTouch(seekBar);
                }

            }
        });
    }

    private void setIndicatorPosition() {

        int padding = getResources().getDimensionPixelSize(R.dimen.slider_padding);
        int pos = padding + (seekbar.getThumbDrawable().getBounds().left + (seekbar.getThumbDrawable().getBounds().right - seekbar.getThumbDrawable().getBounds().left) / 2 - (indicatorImage.getWidth() / 2));
        LayoutParams mparams = (LayoutParams) sliderFramelayout.getLayoutParams();
        mparams.leftMargin = pos;
        sliderFramelayout.setLayoutParams(mparams);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setIndicatorPosition();
    }

    private void init(Context context) {
        seekbar = (BaseSlider) findViewById(R.id.slider);
        indicatorImage = (ImageView) findViewById(R.id.slider_indicator);
        indicatorImage.setImageDrawable(VectorDrawable.create(context, R.drawable.uikit_slider_bubble));
        sliderFramelayout = (FrameLayout) findViewById(R.id.slider_framelayout);
        indicatorText = (TextView) findViewById(R.id.indicator_text);
    }

    public void setOnIndicatorSliderChangeListener(OnIndicatorSliderChangeListener listener) {
        this.listener = listener;
    }


    /**
     * A callback that notifies clients when the progress level has been
     * changed. This includes changes that were initiated by the user through a
     * touch gesture or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnIndicatorSliderChangeListener {

        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range 0..max where max
         *        was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar.
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStartTrackingTouch(SeekBar seekBar);

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar.
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStopTrackingTouch(SeekBar seekBar);
    }


}
