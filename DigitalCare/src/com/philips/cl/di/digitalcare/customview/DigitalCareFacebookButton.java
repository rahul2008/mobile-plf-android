package com.philips.cl.di.digitalcare.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.facebook.widget.LoginButton;
import com.philips.cl.di.digitalcare.R;

/**
 * DigitalCareFontButton is the custom Button View.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 5 Dec 2014
 */
public class DigitalCareFacebookButton extends LoginButton {
	public DigitalCareFacebookButton(Context context) {
		super(context);
	}

	public DigitalCareFacebookButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyAttributes(this, context, attrs);
	}

	public DigitalCareFacebookButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(this, context, attrs);

	}

	private void applyAttributes(Button view, Context context,
			AttributeSet attrs) {

		if (attrs != null) {
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.DigitalCareFontTextView);
			final String typeface = a
					.getString(R.styleable.DigitalCareFontTextView_fontAssetName);
			a.recycle();

			// set the font using class DigitalCareFontLoader
			DigitalCareFontLoader.getInstance().setTypeface(view, typeface);
			setButtonText((context.getResources().getString(R.string.facebook)));
		}
	}

	public void setTypeface(String typeface) {
		DigitalCareFontLoader.getInstance().setTypeface(this, typeface);
	}

}
