package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;

/**
 * <br>
 1.To use RangeSlider in XML file  include below convention as per your required display metrics
 <br>
 &lt;com.philips.cdp.uikit.customviews.RangeSlider
 android:id="@+id/rangeslider"
 android:layout_width="269dp"
 android:layout_height="wrap_content"
 android:layout_gravity="center"
 android:layout_marginTop="10dp"
 app:absoluteMaxValue="300"
 app:absoluteMinValue="90" /&gt;
 *
 *
 */
public class RangeSlider<T extends Number> extends RelativeLayout implements RangeSeekBar.OnRangeSeekBarChangeListener {

    private TextView textviewLeft;
    private TextView textviewRight;
    private RangeSeekBar baserangebar;
    private OnRangeSliderChangeListener<T> listener;
    public RangeSlider(final Context context) {
        super(context);
    }

    public RangeSlider(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uikit_range_slider, null, false);
        addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        init();
        baserangebar.setOnRangeSeekBarChangeListener(this);
        baserangebar.setNotifyWhileDragging(true);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);

        Number minValue = extractNumericValueFromAttributes(a,R.styleable.RangeSeekBar_uikit_absoluteMinValue,0);
        Number maxValue = extractNumericValueFromAttributes(a,R.styleable.RangeSeekBar_uikit_absoluteMaxValue,100);
        setRangeValues(minValue, maxValue);
    }

    public RangeSlider(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uikit_range_slider, null, false);
        addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        init();
    }

    public RangeSlider(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private T extractNumericValueFromAttributes(TypedArray a, int attribute, int defaultValue) {
        TypedValue tv = a.peekValue(attribute);
        if (tv == null) {
            return (T) Integer.valueOf(defaultValue);
        }

        int type = tv.type;
        if (type == TypedValue.TYPE_FLOAT) {
            return (T) Float.valueOf(a.getFloat(attribute, defaultValue));
        } else {
            return (T) Integer.valueOf(a.getInteger(attribute, defaultValue));
        }
    }

    @Override
    public void onRangeSeekBarValuesChanged(final RangeSeekBar bar, final Object minValue, final Object maxValue) {
        if (listener !=null) {
            listener.onRangeSliderValuesChanged(bar, (T) bar.getSelectedMinValue(), (T) bar.getSelectedMaxValue());
        }
    }

    /**
     * setText for minimum and maximum value
     * @param minvalue string minimum value
     * @param maxvalue string maximum value
     */
    public void setText(String minvalue, String maxvalue) {
        textviewLeft.setText(minvalue);
        textviewRight.setText(maxvalue);
    }

    private void init() {

        textviewLeft = (TextView) findViewById(R.id.textviewleft);
        textviewRight = (TextView) findViewById(R.id.textviewright);
        baserangebar = (RangeSeekBar) findViewById(R.id.baserangebar);

    }

    public void setRangeValues(Number minValue, Number maxValue) {
        baserangebar.setRangeValues(minValue, maxValue);

    }

    /**
     * Registers given listener callback to notify about changed selected values.
     *
     * @param listener The listener to notify about changed selected values.
     */
    public void setOnRangeSliderChangeListener(OnRangeSliderChangeListener<T> listener) {
        this.listener = listener;
    }


    /**
     * Callback listener interface to notify about changed range values.
     *
     * @param <T> The Number type the RangeSeekBar has been declared with.
     */

    public interface OnRangeSliderChangeListener<T> {

        void onRangeSliderValuesChanged(RangeSeekBar<?> bar, T minValue, T maxValue);
    }
}
