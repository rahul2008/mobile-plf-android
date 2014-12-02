package com.philips.cl.di.dev.pa.digitalcare.customview;

import com.philips.cl.di.dev.pa.digitalcare.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

public class FontButton extends Button {
	public FontButton(Context context) {
		super(context);
	}

	public FontButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyAttributes(this, context, attrs);
	}

	public FontButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(this, context, attrs);
	}

	private void applyAttributes(Button view, Context context,
			AttributeSet attrs) {

		if (attrs != null) {
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.FontTextView);
			final String typeface = a
					.getString(R.styleable.FontTextView_fontAssetName);
			a.recycle();

			// set the font using class FontLoader
			FontLoader.getInstance().setTypeface(view, typeface);
		}
	}

	public void setTypeface(String typeface) {
		FontLoader.getInstance().setTypeface(this, typeface);
	}
}
