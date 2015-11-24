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

import java.text.NumberFormat;
import java.util.Locale;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RangeSlider<T extends Number> extends RelativeLayout implements RangeSeekBar.OnRangeSeekBarChangeListener {

    private TextView textviewLeft;
    private TextView textviewRight;
    private RangeSeekBar baserangbar;
    public RangeSlider(final Context context) {
        super(context);
    }

    public RangeSlider(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uikit_range_slider, null, false);
        addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        init();
        baserangbar.setOnRangeSeekBarChangeListener(this);
        baserangbar.setNotifyWhileDragging(true);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);

        Number minValue = extractNumericValueFromAttributes(a,R.styleable.RangeSeekBar_absoluteMinValue,0);
        Number maxValue = extractNumericValueFromAttributes(a,R.styleable.RangeSeekBar_absoluteMaxValue,100);

        baserangbar.setRangeValues(minValue,maxValue);

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


        Locale netherland = new Locale("nl", "NL");
        NumberFormat netherlandFormat = NumberFormat.getCurrencyInstance(netherland);

        String minStr = netherlandFormat.format(minValue);
        String maxStr = netherlandFormat.format(maxValue);

        textviewLeft.setText(minStr);
        textviewRight.setText(maxStr);

    }

    private void init() {

        textviewLeft = (TextView) findViewById(R.id.textviewleft);
        textviewRight = (TextView) findViewById(R.id.textviewright);
        baserangbar = (RangeSeekBar) findViewById(R.id.baserangebar);

    }
}
