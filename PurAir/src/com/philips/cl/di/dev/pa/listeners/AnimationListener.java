package com.philips.cl.di.dev.pa.listeners;

import android.util.Log;

import com.nineoldandroids.animation.Animator.AnimatorListener;

/**
 * Listens for Dashboard animations and disables gesture events when animation is in progess.
 * @author 310150437
 *
 */
public class AnimationListener implements AnimatorListener {
	private static final String TAG = AnimationListener.class.getSimpleName();
	
	public static boolean ignoreGesture;

	@Override
	public void onAnimationCancel(com.nineoldandroids.animation.Animator arg0) {
		Log.i(TAG, "onAnimationCancel ignoreGesture " + ignoreGesture);
	}

	@Override
	public void onAnimationEnd(com.nineoldandroids.animation.Animator arg0) {
		ignoreGesture = false;
		Log.i(TAG, "onAnimationEnd " + ignoreGesture);
	}

	@Override
	public void onAnimationRepeat(com.nineoldandroids.animation.Animator arg0) {
		Log.i(TAG, "onAnimationRepeat ignoreGesture " + ignoreGesture);
	}

	@Override
	public void onAnimationStart(com.nineoldandroids.animation.Animator arg0) {
		ignoreGesture = true;
		Log.i(TAG, "onAnimationStart " + ignoreGesture);
	}
	
}