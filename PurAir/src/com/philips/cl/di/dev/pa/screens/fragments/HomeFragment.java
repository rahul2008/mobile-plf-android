package com.philips.cl.di.dev.pa.screens.fragments;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;

import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;

import com.philips.cl.di.dev.pa.utils.Utils;


/**
 * The Class HomeFragment.
 */
public class HomeFragment extends Fragment implements OnClickListener,
		OnGestureListener, SensorEventListener {

	/** The scale down/up outdoor animator set. */
	private AnimatorSet scaleDownIndoorAnimatorSet,
			scaleDownOutdoorAnimatorSet,fadeInAnimatorSet,fadeOutAnimatorSet;
	/** The scaling animation variables. */
	private ObjectAnimator scaleUpIndoor, scaleDownIndoor, scaleUpOutdoor,
			scaleDownOutdoor, scaleUpIndoorRing, scaleUpOutdoorRing,
			scaleDownIndoorRing, scaleDownOutdoorRing, translateUpOutdoorInfo,
			translateDownOutdoorInfo, fadeInIndoorRingQuad1,
			fadeOutIndoorRingQuad1, fadeInIndoorRingQuad2,
			fadeOutIndoorRingQuad2, fadeInIndoorRingQuad3,
			fadeOutIndoorRingQuad3, fadeInIndoorRingQuad4,
			fadeOutIndoorRingQuad4;

	/** The relative layouts outdoor/indoor section. */
	private RelativeLayout rlIndoorSection, rlOutdoorSection, rlOutdoorInfo;

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
	
	private CustomTextView tvDay, tvTime ;

	private TextView tvIndoorAQI;

	/** The params outdoor. */
	FrameLayout.LayoutParams paramsIndoor, paramsOutdoor;

	/** The main view. */
	View vMain;
	
	private DatabaseAdapter dbAdapter ;

	private ImageView ivIndoorQuad1, ivIndoorQuad2, ivIndoorQuad3,
			ivIndoorQuad4;

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

	@Override
	public void onResume() {
		
		super.onResume();
		SensorDataController.getInstance(getActivity()).registerListener(this);
		startAnimations();
		
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
				int height = rlIndoorSection.getMeasuredHeight();
				Log.i(TAG, "Indoor height :" + height);
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
				Log.i(TAG, "Indoor height :" + iOutdoorCompressedHeight);
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

		tvIndoorAQI = (TextView) vMain.findViewById(R.id.tvIndoorAQI);

		ivIndoorQuad1 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad1);
		ivIndoorQuad2 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad2);
		ivIndoorQuad3 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad3);
		ivIndoorQuad4 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad4);

		
		tvDay = (CustomTextView) vMain.findViewById(R.id.tvDay) ;
		
		tvTime = (CustomTextView) vMain.findViewById(R.id.tvTime) ;
		
		dbAdapter = new DatabaseAdapter(getActivity()) ;
		dbAdapter.open() ;
		
		AirPurifierEventDto dto = dbAdapter.getLastUpdatedEvent() ;
		
		if ( dto != null ) {
			tvIndoorAQI.setText(String.valueOf(dto.getIndoorAQI())) ;
			tvDay.setText(dto.getTimeStamp().substring(0, 10)) ;
			tvTime.setText(dto.getTimeStamp().substring(11,16)) ;
		}
	}

	/**
	 * Initialize animations.
	 */
	private void initialiseAnimations() {

		scaleDownIndoor = ObjectAnimator.ofFloat(rlIndoorSection, "scaleY", 1f,
				.45f);
		scaleDownIndoor.setDuration(AppConstants.DURATION);

		scaleDownIndoorRing = ObjectAnimator.ofFloat(flIndoorRing, "scaleX",
				1f, .45f);
		scaleDownIndoorRing.setDuration(AppConstants.DURATION);

		scaleUpOutdoor = ObjectAnimator.ofFloat(rlOutdoorSection, "scaleY", 1f,
				2.2f);
		scaleUpOutdoor.setDuration(AppConstants.DURATION);

		scaleDownOutdoorRing = ObjectAnimator.ofFloat(flOutdoorRing, "scaleX",
				2.2f, 1f);
		scaleDownOutdoorRing.setDuration(AppConstants.DURATION);

		scaleUpIndoor = ObjectAnimator.ofFloat(rlIndoorSection, "scaleY", .45f,
				1f);
		scaleUpIndoor.setDuration(AppConstants.DURATION);

		scaleUpIndoorRing = ObjectAnimator.ofFloat(flIndoorRing, "scaleX",
				.45f, 1f);
		scaleUpIndoorRing.setDuration(AppConstants.DURATION);

		scaleUpOutdoorRing = ObjectAnimator.ofFloat(flOutdoorRing, "scaleX",
				1f, 2.2f);
		scaleUpOutdoorRing.setDuration(AppConstants.DURATION);

		scaleDownOutdoor = ObjectAnimator.ofFloat(rlOutdoorSection, "scaleY",
				2.2f, 1f);
		scaleDownOutdoor.setDuration(AppConstants.DURATION);

		translateDownOutdoorInfo = ObjectAnimator.ofFloat(rlOutdoorInfo,
				"translationY", -480f, 0f);

		translateDownOutdoorInfo.setDuration(AppConstants.DURATION);

		translateUpOutdoorInfo = ObjectAnimator.ofFloat(rlOutdoorInfo,
				"translationY", 0f, -480f);
		translateUpOutdoorInfo.setDuration(AppConstants.DURATION);

		// Animation Sets
		scaleDownIndoorAnimatorSet = new AnimatorSet();
		scaleDownIndoorAnimatorSet.setDuration(AppConstants.DURATION);
		scaleDownIndoorAnimatorSet.playTogether(scaleDownIndoor,
				scaleDownIndoorRing, scaleUpOutdoor, scaleUpOutdoorRing,
				translateUpOutdoorInfo);

		scaleDownOutdoorAnimatorSet = new AnimatorSet();
		scaleDownOutdoorAnimatorSet.setDuration(AppConstants.DURATION);
		scaleDownOutdoorAnimatorSet.playTogether(scaleUpIndoor,
				scaleUpIndoorRing, scaleDownOutdoor, scaleDownOutdoorRing,
				translateDownOutdoorInfo);
		
		fadeOutIndoorRingQuad1 = ObjectAnimator.ofFloat(ivIndoorQuad1, "alpha",
				0.1f, 1f);
		fadeOutIndoorRingQuad1.setDuration(AppConstants.FADEDURATION);
		
		fadeOutIndoorRingQuad2 = ObjectAnimator.ofFloat(ivIndoorQuad2, "alpha",
				0.1f, 1f);
		fadeOutIndoorRingQuad2.setDuration(AppConstants.FADEDURATION);
		fadeOutIndoorRingQuad2.setStartDelay(AppConstants.FADEDELAY);
		
		fadeOutIndoorRingQuad3 = ObjectAnimator.ofFloat(ivIndoorQuad3, "alpha",
				0.1f, 1f);
		fadeOutIndoorRingQuad3.setDuration(AppConstants.FADEDURATION);
		fadeOutIndoorRingQuad3.setStartDelay(AppConstants.FADEDELAY*2);
		
		fadeOutIndoorRingQuad4 = ObjectAnimator.ofFloat(ivIndoorQuad4, "alpha",
				0.1f, 1f);
		fadeOutIndoorRingQuad4.setDuration(AppConstants.DURATION);
		fadeOutIndoorRingQuad4.setStartDelay(AppConstants.FADEDELAY*3);
		
		fadeOutAnimatorSet = new AnimatorSet();
		fadeOutAnimatorSet.playTogether(fadeOutIndoorRingQuad1,fadeOutIndoorRingQuad2,fadeOutIndoorRingQuad3,fadeOutIndoorRingQuad4);
		fadeOutAnimatorSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG,"Animation started : Fade OUT");
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				Log.i(TAG,"Animation Repat : Fade OUT");				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if(fadeInAnimatorSet!=null)
				fadeInAnimatorSet.start();
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i(TAG,"Animation Canceled : Fade OUT");
				
			}
		});
		

		fadeInIndoorRingQuad1 = ObjectAnimator.ofFloat(ivIndoorQuad1, "alpha",
				1f, 0.1f);
		fadeInIndoorRingQuad1.setDuration(AppConstants.FADEDURATION);
		
		
		fadeInIndoorRingQuad2 = ObjectAnimator.ofFloat(ivIndoorQuad2, "alpha",
				1f, 0.1f);
		fadeInIndoorRingQuad2.setDuration(AppConstants.FADEDURATION);
		fadeInIndoorRingQuad2.setStartDelay(AppConstants.FADEDELAY);
		
		
		fadeInIndoorRingQuad3 = ObjectAnimator.ofFloat(ivIndoorQuad3, "alpha",
				1f, 0.1f);
		fadeInIndoorRingQuad3.setDuration(AppConstants.FADEDURATION);
		fadeInIndoorRingQuad3.setStartDelay(AppConstants.FADEDELAY*2);
		
		fadeInIndoorRingQuad4 = ObjectAnimator.ofFloat(ivIndoorQuad4, "alpha",
				1f, 0.1f);
		fadeInIndoorRingQuad4.setDuration(AppConstants.FADEDURATION);
		fadeInIndoorRingQuad4.setStartDelay(AppConstants.FADEDELAY*3);
		
		fadeInAnimatorSet = new AnimatorSet();
		fadeInAnimatorSet.playTogether(fadeInIndoorRingQuad1,fadeInIndoorRingQuad2,fadeInIndoorRingQuad3,fadeInIndoorRingQuad4);
		//fadeInAnimatorSet.start();
		fadeInAnimatorSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG,"Animation started : Fade IN");				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				Log.i(TAG,"Animation repeated : Fade IN");				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if(fadeOutAnimatorSet!=null)
				fadeOutAnimatorSet.start();
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i(TAG,"Animation canceled : Fade IN");				
			}
		});
		
		

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

	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {
		updateUI(airPurifierEventDto);
	}

	private void updateUI(AirPurifierEventDto airPurifierEventDto) {
		if (airPurifierEventDto != null) {
			int iIndoorAQI = airPurifierEventDto.getIndoorAQI();
			updateIndoorBackground(iIndoorAQI);
			updateIndoorAQIRing(iIndoorAQI);
			updateIndoorInfo(iIndoorAQI);			
		}
	}

	private void updateIndoorInfo(int indoorAQI) {
		String currentDateTime = Utils.getCurrentDateTime() ;
		
		tvIndoorAQI.setText(String.valueOf(indoorAQI));
		tvDay.setText(currentDateTime.substring(0, 10)) ;
		tvTime.setText(currentDateTime.substring(11,16)) ;
	}

	private void updateIndoorAQIRing(int iAQI) {
		String[] INDOOR_RING = Utils.getIndoorRing(iAQI);
		ivIndoorQuad1.setImageResource(Utils.getResourceID(INDOOR_RING[0],
				getActivity()));
		ivIndoorQuad2.setImageResource(Utils.getResourceID(INDOOR_RING[1],
				getActivity()));
		ivIndoorQuad3.setImageResource(Utils.getResourceID(INDOOR_RING[2],
				getActivity()));
		ivIndoorQuad4.setImageResource(Utils.getResourceID(INDOOR_RING[3],
				getActivity()));

	}
	
	private void updateIndoorBackground(int iIndoorAqi)
	{
		rlIndoorSection.setBackgroundResource(Utils.getResourceID(Utils.getIndoorBG(iIndoorAqi), getActivity()));
	}

	@Override
	public void onPause() {
		Log.i(TAG, "OnPause");
		SensorDataController.getInstance(getActivity())
				.unRegisterListener(this);
		stopAnimations();
		super.onPause();
	}
	
	private void startAnimations()
	{
		if(fadeInAnimatorSet!=null && !fadeInAnimatorSet.isRunning())
		{
			fadeInAnimatorSet.start();
		}
	}
	
	private void stopAnimations()
	{
		if(fadeInAnimatorSet!=null && fadeInAnimatorSet.isRunning())
		{
			fadeInAnimatorSet.end();
		}
		if(fadeOutAnimatorSet!=null && fadeOutAnimatorSet.isRunning())
		{
			fadeOutAnimatorSet.end();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "OnDestroy") ;
		storeLastEvent() ;
		super.onDestroy();
	}
	
	private void storeLastEvent() {
		dbAdapter.insertAirPurifierEvent(Integer.parseInt(tvIndoorAQI.getText().toString())) ;
		dbAdapter.close() ;
	}
}
