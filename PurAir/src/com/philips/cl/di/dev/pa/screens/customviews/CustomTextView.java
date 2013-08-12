package com.philips.cl.di.dev.pa.screens.customviews;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CustomTextView extends TextView {
	private static final String TAG = CustomTextView.class.getSimpleName();

	public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyAttributes(context, attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(context, attrs);
	}

	private void applyAttributes(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CustomTextView);
		try {
			Typeface font = Typeface.createFromAsset(
					getResources().getAssets(), AppConstants.FONT);
			if (font != null) {
				this.setTypeface(font);
			}
		} catch (RuntimeException e) {
			Log.e(TAG, e.getMessage());

		}
	}
}