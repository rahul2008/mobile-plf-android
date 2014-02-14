package com.philips.cl.di.dev.pa.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Typeface;
import android.widget.TextView;

/*
 * This class will create only one instance of any typeface. Whenever a typeface is requested, 
 * it will check if the instance of the requested typeface exists. If not it will create an instance and save it to a Map.
 * All typeface can be retrieved using the font name as the key.
 */
public class FontLoader {

	private static FontLoader mInstance;
	private Map<String, Typeface> mFonts;

	private FontLoader() {
		mFonts = new HashMap<String, Typeface>();
	}

	public static FontLoader getInstance() {
		if (mInstance == null) {
			mInstance = new FontLoader();
		}

		return mInstance;
	}	

	public void setTypeface(TextView tv, String fontName) {
		if (fontName == null) return;

		if(!tv.isInEditMode())
		{
			Typeface typeface = mFonts.get(fontName);
			if (typeface == null) {
				typeface = Typeface.createFromAsset(tv.getContext().getAssets(), fontName);
				mFonts.put(fontName, typeface);
			}
			tv.setTypeface(typeface);
		}
	}

}
