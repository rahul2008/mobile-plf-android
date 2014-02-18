package com.philips.cl.di.dev.pa.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
	
	public static Typeface getGillsans(Context context) {
		if(gillsans == null) {
			gillsans = Typeface.createFromAsset(context.getAssets(), "fonts/gillsans.ttf");
		}
		return gillsans;
	}
	public static Typeface getGillsansLight(Context context) {
		if(gillsansLight == null) {
			gillsansLight = Typeface.createFromAsset(context.getAssets(), "fonts/gillsanslight.ttf");
		}
		return gillsansLight;
	}
	public static Typeface getOpensansCondBold(Context context) {
		if(opensansCondBold == null) {
			opensansCondBold = Typeface.createFromAsset(context.getAssets(), "fonts/opensanscondbold.ttf");
		}
		return opensansCondBold;
	}
	public static Typeface getOpensansCondLight(Context context) {
		if(opensansCondLight == null) {
			opensansCondLight = Typeface.createFromAsset(context.getAssets(), "fonts/opensanscondlight.ttf");
		}
		return opensansCondLight;
	}
	public static Typeface getOpensansCondLightItalic(Context context) {
		if(opensansCondLightItalic == null) {
			opensansCondLightItalic = Typeface.createFromAsset(context.getAssets(), "fonts/opensanscondlightitalic.ttf");
		}
		return opensansCondLightItalic;
	}
	
	private static Typeface gillsans;
	private static Typeface gillsansLight;
	private static Typeface opensansCondBold;
	private static Typeface opensansCondLight;
	private static Typeface opensansCondLightItalic;
}
