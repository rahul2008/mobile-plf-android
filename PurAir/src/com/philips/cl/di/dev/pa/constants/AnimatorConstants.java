package com.philips.cl.di.dev.pa.constants;

import android.util.Log;

import com.philips.cl.di.dev.pa.activity.MainActivity;

public class AnimatorConstants {
	
	private static String TAG = "AnimatorConstants";
	
	public static final String ANIM_TRANSLATION_X 		= "translationX";
	public static final String ANIM_TRANSLATION_Y 		= "translationY";
	public static final String ANIM_ROTATION 			= "rotation";
	public static final String ANIM_ROTATION_X 			= "rotationX";
	public static final String ANIM_ROTATION_Y 			= "rotationY";
	public static final String ANIM_SCALE_X 			= "scaleX";
	public static final String ANIM_SCALE_Y 			= "scaleY";
	public static final String ANIM_PIVOT_X 			= "pivotX";
	public static final String ANIM_PIVOT_Y 			= "pivotY"; 
	public static final String ANIM_X 					= "x";
	public static final String ANIM_Y 					= "y";
	public static final String ANIM_ALPHA 				= "alpha";
	
	public static final int ANIM_DURATION				= 1000;
	
	
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
		translationY = (MainActivity.getScreenHeight() * 0.47f * -1) + 25;
//		Log.i(TAG, "getOutdoorCityInfoTranslationY " + translationY);
		return translationY;
	}
	
	public static float getCityInfoScaleUpTransformY() {
		float translationY = 0;
		translationY = (MainActivity.getScreenHeight() * 0.115f * -1) + 0;
//		Log.i(TAG, "getCityInfoScaleUpTransformY " + translationY);
		return translationY;
	}
	
	public static float rotationPivot() {
		float pivot = 0;
		pivot = MainActivity.getScreenHeight() * 0.018f;
//		Log.i(TAG, "rotationPivot " + pivot);
		return pivot;
	}
	
	//New animation constants which use layout height instead of Activity height.
	public static float indoorBackgroundTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = (layoutHeight * -0.5575f);
		Log.i(TAG, "indoorBackgroundTranslationY " + translationY);
		return translationY;
	}
	
	public static float outdoorTextScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = (layoutHeight * -0.17f);
		Log.i(TAG, "outdoorTextScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	public static float outdoorTextScaleUpTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = (layoutHeight * -0.725f);
		Log.i(TAG, "outdoorTextScaleUpTranslationY " + translationY);
		return translationY;
	}
	
	public static float indoorTextScaleUpTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = (layoutHeight * -0.7664f);
		Log.i(TAG, "indoorTextScaleUpTranslationY " + translationY);
		return translationY;
	}
	
	public static float indoorTextScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = (layoutHeight * -0.2104f);
		Log.i(TAG, "indoorTextScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	public static float indoorCircleScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = (layoutHeight * 0.2705f);
		Log.i(TAG, "indoorCircleScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	public static float outdoorCircleScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = (layoutHeight * 0.335f);
		Log.i(TAG, "indoorCircleScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	
}
