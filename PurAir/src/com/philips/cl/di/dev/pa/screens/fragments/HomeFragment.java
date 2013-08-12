package com.philips.cl.di.dev.pa.screens.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;

/**
 * The Class HomeFragment.
 */
public class HomeFragment extends Fragment implements OnClickListener,
		OnGestureListener {

	/** The scale down/up outdoor animator set. */
	private AnimatorSet scaleDownIndoorAnimatorSet,
			scaleDownOutdoorAnimatorSet;
	/** The scaling animation variables. */
	private ObjectAnimator scaleUpIndoor, scaleDownIndoor, scaleUpOutdoor,
			scaleDownOutdoor, scaleUpIndoorRing, scaleUpOutdoorRing,
			scaleDownIndoorRing, scaleDownOutdoorRing,translateUpOutdoorInfo,translateDownOutdoorInfo;

	/** The relative layouts outdoor/indoor section. */
	private RelativeLayout rlIndoorSection, rlOutdoorSection,rlOutdoorInfo;

	/** The framelayout outdoor ring. */
	private FrameLayout flIndoorRing, flOutdoorRing;

	/** The view id. */
	private int viewId;

	/** The is indoor expanded. */
	private boolean isIndoorExpanded = true;

	/** The Constant TAG. */
	public final static String TAG = HomeFragment.class.getSimpleName();

	/** The gesture detector. */
	private GestureDetector gestureDetector;

	/** The i outdoor compressed height. */
	private int iOutdoorCompressedHeight;

	/** The params outdoor. */
	FrameLayout.LayoutParams paramsIndoor, paramsOutdoor;

	/** The main view. */
	View vMain;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	/**
	 * On create view.
	 * 
	 * @param inflater
	 *            the inflater
	 * @param container
	 *            the container
	 * @param savedInstanceState
	 *            the saved instance state
	 * @return the view
	 */
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = inflater.inflate(R.layout.activity_home, container, false);
		gestureDetector = new GestureDetector(this);
		initialiseViews();
		initialiseAnimations();
		return vMain;
	}

	/**
	 * Initialize views.
	 */
	private void initialiseViews() {

		rlIndoorSection = (RelativeLayout) vMain
				.findViewById(R.id.rlIndoorSection);
		ViewTreeObserver vtoIndoor = rlIndoorSection.getViewTreeObserver();
		vtoIndoor.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				paramsIndoor = (android.widget.FrameLayout.LayoutParams) rlIndoorSection
						.getLayoutParams();
				rlIndoorSection.getMeasuredHeight();
				rlIndoorSection.setPivotX(0f);
				rlIndoorSection.setPivotY(0f);

			}
		});
		rlIndoorSection.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				viewId = v.getId();
				return gestureDetector.onTouchEvent(event);
			}
		});
		rlOutdoorSection = (RelativeLayout) vMain
				.findViewById(R.id.rlOutdoorSection);
		ViewTreeObserver vtoOutdoor = rlIndoorSection.getViewTreeObserver();
		vtoOutdoor.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				paramsOutdoor = (android.widget.FrameLayout.LayoutParams) rlOutdoorSection
						.getLayoutParams();
				iOutdoorCompressedHeight = rlOutdoorSection.getMeasuredHeight();
				rlOutdoorSection.setPivotX(0f);
				rlOutdoorSection.setPivotY(iOutdoorCompressedHeight);

			}
		});
		rlOutdoorSection.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				viewId = v.getId();
				return gestureDetector.onTouchEvent(event);
			}
		});

		flIndoorRing = (FrameLayout) vMain.findViewById(R.id.flIndoorRing);
		flOutdoorRing = (FrameLayout) vMain.findViewById(R.id.flOutdoorRing);
		rlOutdoorInfo = (RelativeLayout) vMain.findViewById(R.id.rlOutdoorInfo);
	}

	/**
	 * Initialize animations.
	 */
	private void initialiseAnimations() {

		scaleDownIndoor = ObjectAnimator.ofFloat(rlIndoorSection, "scaleY", 1f,
				.40f);
		scaleDownIndoor.setDuration(2000);

		scaleDownIndoorRing = ObjectAnimator.ofFloat(flIndoorRing, "scaleX",
				1f, .40f);
		scaleDownIndoorRing.setDuration(2000);

		scaleUpOutdoor = ObjectAnimator.ofFloat(rlOutdoorSection, "scaleY", 1f,
				2.5f);
		scaleUpOutdoor.setDuration(2000);

		scaleDownOutdoorRing = ObjectAnimator.ofFloat(flOutdoorRing, "scaleX",
				1f, .40f);
		scaleDownOutdoorRing.setDuration(2000);

		scaleUpIndoor = ObjectAnimator.ofFloat(rlIndoorSection, "scaleY", .40f,
				1f);
		scaleUpIndoor.setDuration(2000);

		scaleUpIndoorRing = ObjectAnimator.ofFloat(flIndoorRing, "scaleX",
				.40f, 1f);
		scaleUpIndoorRing.setDuration(2000);

		scaleUpOutdoorRing = ObjectAnimator.ofFloat(flOutdoorRing, "scaleX",
				.40f, 1f);
		scaleUpOutdoorRing.setDuration(2000);

		scaleDownOutdoor = ObjectAnimator.ofFloat(rlOutdoorSection, "scaleY",
				2.5f, 1f);
		scaleDownOutdoor.setDuration(2000);
		
		translateDownOutdoorInfo = ObjectAnimator.ofFloat(rlOutdoorInfo, "translationY",-540f , 0f);
		
		translateDownOutdoorInfo.setDuration(2000);
		
		translateUpOutdoorInfo = ObjectAnimator.ofFloat(rlOutdoorInfo, "translationY",
				0f, -540f);
		translateUpOutdoorInfo.setDuration(2000);
	
		// Animation Sets 
		scaleDownIndoorAnimatorSet = new AnimatorSet();
		scaleDownIndoorAnimatorSet.setDuration(2000);
		scaleDownIndoorAnimatorSet.playTogether(scaleDownIndoor,
				scaleDownIndoorRing, scaleUpOutdoor, scaleUpOutdoorRing,translateUpOutdoorInfo);
		
		scaleDownOutdoorAnimatorSet = new AnimatorSet();
		scaleDownOutdoorAnimatorSet.setDuration(2000);
		scaleDownOutdoorAnimatorSet.playTogether(scaleUpIndoor,
				scaleUpIndoorRing, scaleDownOutdoor, scaleDownOutdoorRing,translateDownOutdoorInfo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	/**
	 * On click.
	 * 
	 * @param v
	 *            the v
	 */
	@Override
	public void onClick(View v) {
		Log.i(TAG, "On Click");
	}

	/**
	 * On down.
	 * 
	 * @param e
	 *            the e
	 * @return true, if successful
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	/**
	 * On fling.
	 * 
	 * @param e1
	 *            the e1
	 * @param e2
	 *            the e2
	 * @param velocityX
	 *            the velocity x
	 * @param velocityY
	 *            the velocity y
	 * @return true, if successful
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.i(TAG, "On Fling");
		float differenceY = (e2.getY() - e1.getY());
		switch (viewId) {
		case R.id.rlIndoorSection:
			if (Math.abs(differenceY) > AppConstants.SWIPE_THRESHOLD
					&& Math.abs(velocityY) > AppConstants.SWIPE_VELOCITY_THRESHOLD) {
				if (differenceY > 0) {
					Log.i(TAG, "SWIPE DOWN" + viewId);
					if (!isIndoorExpanded) {
						scaleDownOutdoorAnimatorSet.start();
						isIndoorExpanded = !isIndoorExpanded;
					}
				} else {
					Log.i(TAG, "SWIPE UP" + viewId);
					if (isIndoorExpanded) {
						scaleDownIndoorAnimatorSet.start();
						isIndoorExpanded = !isIndoorExpanded;
					}

				}
			}

			return true;

		case R.id.rlOutdoorSection:
			if (Math.abs(differenceY) > AppConstants.SWIPE_THRESHOLD
					&& Math.abs(velocityY) > AppConstants.SWIPE_VELOCITY_THRESHOLD) {
				if (differenceY > 0) {
					Log.i(TAG, "SWIPE DOWN" + viewId);
					if (!isIndoorExpanded) {
						scaleDownOutdoorAnimatorSet.start();
						isIndoorExpanded = !isIndoorExpanded;
					}
				} else {
					Log.i(TAG, "SWIPE UP" + viewId);
					if (isIndoorExpanded) {
						scaleDownIndoorAnimatorSet.start();
						isIndoorExpanded = !isIndoorExpanded;
					}
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * On long press.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		Log.i(TAG, "On Long Press");

	}

	/**
	 * On scroll.
	 * 
	 * @param e1
	 *            the e1
	 * @param e2
	 *            the e2
	 * @param distanceX
	 *            the distance x
	 * @param distanceY
	 *            the distance y
	 * @return true, if successful
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	/**
	 * On show press.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		Log.i(TAG, "On Show Press");
	}

	/**
	 * On single tap up.
	 * 
	 * @param e
	 *            the e
	 * @return true, if successful
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
