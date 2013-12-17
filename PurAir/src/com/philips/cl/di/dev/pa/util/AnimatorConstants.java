package com.philips.cl.di.dev.pa.util;

import com.philips.cl.di.dev.pa.pureairui.MainActivity;

import android.util.Log;

public class AnimatorConstants {
	
	private static String TAG = "AnimatorConstants";
	
	public static String animTranslationX 		= "translationX";
	public static String animTranslationY 		= "translationY";
	public static String animRotation 			= "rotation";
	public static String animRotationX 			= "rotationX";
	public static String animRotationY 			= "rotationY";
	public static String animScaleX 			= "scaleX";
	public static String animScaleY 			= "scaleY";
	public static String animPivotX 			= "pivotX";
	public static String animPivotY 			= "pivotY"; 
	public static String animX 					= "x";
	public static String animY 					= "y";
	public static String animAlpha 				= "alpha";
	
	public static int animDuration				= 3000;
	
	
	public static float getIndoorBGTranslationY() {
		float translationX = 0;
		translationX = MainActivity.getScreenHeight() * 0.36f * -1;
		Log.i(TAG, "getIndoorBGTranslationY " + translationX);
		return translationX;
	}
	
	public static float getIndoorGaugeTranslationY() {
		float translationX = 0;
		translationX = MainActivity.getScreenHeight() * 0.16f * -1;
		Log.i(TAG, "getIndoorGaugeTranslationY " + translationX);
		return translationX;
	}
	
	public static float getOutdoorGaugeTranslationY() {
		float translationX = 0;
		translationX = MainActivity.getScreenHeight() * 0.21f;
		Log.i(TAG, "getOutdoorGaugeTranslationY " + translationX);
		return translationX;
	}
	
	public static float getOutdoorCityInfoTranslationY() {
		float translationX = 0;
		translationX = MainActivity.getScreenHeight() * 0.36f;
		Log.i(TAG, "getOutdoorCityInfoTranslationY " + translationX);
		return translationX;
	}
	
}
