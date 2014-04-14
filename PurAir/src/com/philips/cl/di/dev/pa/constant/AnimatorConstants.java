package com.philips.cl.di.dev.pa.constant;

import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.util.ALog;

public class AnimatorConstants {
	
	public static final String ANIM_TRANSLATION_Y 		= "translationY";
	public static final String ANIM_ROTATION 			= "rotation";
	public static final String ANIM_SCALE_X 			= "scaleX";
	public static final String ANIM_SCALE_Y 			= "scaleY";
	public static final String ANIM_ALPHA 				= "alpha";
	
	public static final int ANIM_DURATION				= 1000;
	
	
	
	public static float rotationPivot() {
		float pivot = 0;
		pivot = MainActivity.getScreenHeight() * 0.018f;
//		Log.i(TAG, "rotationPivot " + pivot);
		return pivot;
	}
	
	//New animation constants which use layout height instead of Activity height.
	public static float indoorBackgroundTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = layoutHeight * -0.5575f;
		ALog.i(ALog.ANIMATOR_CONST, "indoorBackgroundTranslationY " + translationY);
		return translationY;
	}
	
	public static float outdoorTextScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = layoutHeight * -0.17f;
		ALog.i(ALog.ANIMATOR_CONST, "outdoorTextScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	public static float outdoorTextScaleUpTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = layoutHeight * -0.725f;
		ALog.i(ALog.ANIMATOR_CONST, "outdoorTextScaleUpTranslationY " + translationY);
		return translationY;
	}
	
	public static float indoorTextScaleUpTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = layoutHeight * -0.7664f;
		ALog.i(ALog.ANIMATOR_CONST, "indoorTextScaleUpTranslationY " + translationY);
		return translationY;
	}
	
	public static float indoorTextScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = layoutHeight * -0.2104f;
		ALog.i(ALog.ANIMATOR_CONST, "indoorTextScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	public static float indoorCircleScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = layoutHeight * 0.2705f;
		ALog.i(ALog.ANIMATOR_CONST, "indoorCircleScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	public static float outdoorCircleScaleDownTranslationY(int layoutHeight) {
		float translationY = 0;
		
		translationY = layoutHeight * 0.335f;
		ALog.i(ALog.ANIMATOR_CONST, "indoorCircleScaleDownTranslationY " + translationY);
		return translationY;
	}
	
	
}
