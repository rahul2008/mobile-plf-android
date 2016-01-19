package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.philips.cdp.uikit.R;

/**
 * <br>
 1.To use DiscreteSlider in XML file  include below convention as per your required display metrics
 <br>
 &lt;com.philips.cdp.uikit.customviews.DiscreteSlider
 android:id="@+id/discreteslider"
 android:layout_width="269dp"
 android:layout_height="wrap_content"
 android:layout_gravity="center"
 android:layout_marginTop="10dp"
 app:discreteValue="7"&gt;
 &lt;/com.philips.cdp.uikit.customviews.DiscreteSlider&gt;
 *
 *
 */
public class DiscreteSlider extends BaseSlider implements SeekBar.OnSeekBarChangeListener {

    private int radius;
    private int baseSliderPadding;
    private int noDiscretePoint;
    private OnDiscreteSliderChangeListener listener;

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
        if (listener !=null) {
            listener.onProgressChanged(seekBar, setValue, fromUser);
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
            listener.onStopTrackingTouch(seekBar);
        }
    }

    private void init() {
        radius = getResources().getDimensionPixelSize(R.dimen.discreteslider_circle_radius);
        baseSliderPadding = getResources().getDimensionPixelSize(R.dimen.slider_indicator_margin);
    }

    /**
     * A callback that notifies clients when the progress level has been
     * changed. This includes changes that were initiated by the user through a
     * touch gesture or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnDiscreteSliderChangeListener {

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


    public void setOnDiscreteSliderChangeListener(OnDiscreteSliderChangeListener listener) {
        this.listener = listener;
    }
}
