package com.philips.cl.di.dev.pa.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.utils.FontLoader;

public class CustomTextView extends TextView {

	public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyAttributes(this, context, attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(this, context, attrs);
	}

	private void applyAttributes(TextView view, Context context, AttributeSet attrs) {

		if (attrs != null) {
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CustomTextView);
			final String typeface =
					a.getString(R.styleable.CustomTextView_fontAssetName);
			a.recycle();

			//set the font using class FontLoader
			FontLoader.getInstance().setTypeface(view, typeface);			
		}
	}


	public void setTypeface(String typeface) {
		FontLoader.getInstance().setTypeface(this, typeface);
	}
}