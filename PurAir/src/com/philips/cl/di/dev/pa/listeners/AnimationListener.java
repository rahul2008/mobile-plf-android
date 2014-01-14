package com.philips.cl.di.dev.pa.listeners;

import android.app.Activity;
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
	
	public static boolean ignoreGesture;
	private Context context;
	private Activity activity;
	
	public AnimationListener(Context context) {
		this.context = context;
		this.activity = (Activity) context;
	}

	@Override
	public void onAnimationCancel(Animator arg0) {
		Log.i(TAG, "onAnimationCancel ignoreGesture " + ignoreGesture);
	}

	@Override
	public void onAnimationEnd(Animator arg0) {
		ignoreGesture = false;
		Log.i(TAG, "onAnimationEnd " + ignoreGesture);
	}

	@Override
	public void onAnimationRepeat(Animator arg0) {
		Log.i(TAG, "onAnimationRepeat ignoreGesture " + ignoreGesture);
	}

	@Override
	public void onAnimationStart(Animator arg0) {
		ignoreGesture = true;
		Log.i(TAG, "onAnimationStart " + ignoreGesture);
	}
	
}