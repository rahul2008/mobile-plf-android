package com.philips.cdp.appframework.customview;

import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class CustomFontLoader {

	private static CustomFontLoader mInstance;
	private Map<String, Typeface> mFonts;

	private CustomFontLoader() {
		mFonts = new HashMap<String, Typeface>();
	}

	public static CustomFontLoader getInstance() {
		if (mInstance == null) {
			mInstance = new CustomFontLoader();
		}

		return mInstance;
	}

	public void setTypeface(TextView tv, String fontName) {
		if (fontName == null)
			return;

		if (!tv.isInEditMode()) {
			Typeface typeface = mFonts.get(fontName);
			if (typeface == null) {
				typeface = Typeface.createFromAsset(
						tv.getContext().getAssets(), fontName);
				mFonts.put(fontName, typeface);
			}
			 tv.setTypeface(typeface);
		}
	}
}
