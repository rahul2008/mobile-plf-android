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
	
	public static int animDuration				= 1000;
	
	
	public static float getIndoorBGTranslationY() {
		float translationY = 0;
		translationY = MainActivity.getScreenHeight() * 0.36f * -1;
//		Log.i(TAG, "getIndoorBGTranslationY " + translationY);
		return translationY;
	}
	
	public static float getIndoorGaugeTranslationY() {
		float translationY = 0;
		translationY = MainActivity.getScreenHeight() * 0.16f * -1;
//		Log.i(TAG, "getIndoorGaugeTranslationY " + translationY);
		return translationY;
	}
	
	public static float getOutdoorGaugeTranslationY() {
		float translationY = 0;
		translationY = MainActivity.getScreenHeight() * 0.21f;
//		Log.i(TAG, "getOutdoorGaugeTranslationY " + translationY);
		return translationY;
	}
	
	public static float getOutdoorCityInfoTranslationY() {
		float translationY = 0;
		translationY = MainActivity.getScreenHeight() * 0.47f * -1;
//		Log.i(TAG, "getOutdoorCityInfoTranslationY " + translationY);
		return translationY;
	}
	
	public static float getCityInfoScaleUpTransformY() {
		float translationY = 0;
		translationY = MainActivity.getScreenHeight() * 0.115f * -1;
//		Log.i(TAG, "getCityInfoScaleUpTransformY " + translationY);
		return translationY;
	}
	
	public static float rotationPivot() {
		float pivot = 0;
		pivot = MainActivity.getScreenHeight() * 0.018f;
//		Log.i(TAG, "rotationPivot " + pivot);
		return pivot;
	}
}
