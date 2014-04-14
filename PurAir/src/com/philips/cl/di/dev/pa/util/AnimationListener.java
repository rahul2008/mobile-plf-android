package com.philips.cl.di.dev.pa.util;

import android.content.Context;
import android.util.Log;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

/**
 * Listens for Dashboard animations and disables gesture events when animation is in progess.
 * @author 310150437
 *
 */
public class AnimationListener implements AnimatorListener {
	private static final String TAG = AnimationListener.class.getSimpleName();
	
	private boolean ignoreGesture;
	
	public AnimationListener(Context context) {
	}

	public boolean isIgnoreGesture() {
		return ignoreGesture ;
	}
	
	@Override
	public void onAnimationCancel(Animator arg0) {
		Log.i(TAG, "onAnimationCancel ignoreGesture " + ignoreGesture);
	}

	@Override
	public void onAnimationEnd(Animator arg0) {
		/**
		 * Find bug show static field
		 */
		ignoreGesture = false;
		Log.i(TAG, "onAnimationEnd " + ignoreGesture);
//		View circle = activity.findViewById(R.id.v_indoor_circle_margin);
//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) circle.getLayoutParams();
//		params.bottomMargin -= 100;
//		
//		circle.setLayoutParams(params);
		
	}

	@Override
	public void onAnimationRepeat(Animator arg0) {
		Log.i(TAG, "onAnimationRepeat ignoreGesture " + ignoreGesture);
	}

	@Override
	public void onAnimationStart(Animator arg0) {
		/**
		 * Find bug show static field
		 */
		ignoreGesture = true;
		Log.i(TAG, "onAnimationStart " + ignoreGesture);
	}
	
}