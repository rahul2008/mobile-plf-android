/**
 * This class will create only one instance of any typeface. Whenever a typeface
 * is requested, it will check if the instance of the requested typeface exists.
 * If not it will create an instance and save it to a Map. All typeface can be
 * retrieved using the font name as the key.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 5 Dec 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 *//*

package com.philips.cdp.digitalcare.customview;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Typeface;
import android.widget.TextView;


public class DigitalCareFontLoader {

	private static DigitalCareFontLoader mInstance;
	private Map<String, Typeface> mFonts;

	private DigitalCareFontLoader() {
		mFonts = new HashMap<String, Typeface>();
	}

	public static DigitalCareFontLoader getInstance() {
		if (mInstance == null) {
			mInstance = new DigitalCareFontLoader();
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
*/
