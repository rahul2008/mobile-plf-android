package com.philips.cl.di.reg.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.FontLoader;

public class XTextview extends TextView {

	public XTextview(Context context) {
		super(context);
	}

	public XTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyAttributes(this, context, attrs);
	}

	public XTextview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(this, context, attrs);
	}

	private void applyAttributes(TextView view, Context context,
			AttributeSet attrs) {

		if (attrs != null) {
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.RegistrationFontTextView);
			final String typeface = a
					.getString(R.styleable.RegistrationFontTextView_fontAssetName);
			a.recycle();

			// set the font using class DigitalCareFontLoader
			FontLoader.getInstance().setTypeface(view, typeface);
		}
	}

	public void setTypeface(String typeface) {
		FontLoader.getInstance().setTypeface(this, typeface);
	}
}
