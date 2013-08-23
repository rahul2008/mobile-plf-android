package com.philips.cl.di.dev.pa.screens.fragments;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.interfaces.OutdoorAQIListener;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.network.TaskGetHttp;

import com.philips.cl.di.dev.pa.screens.TrendsActivity;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;

import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class HomeFragment.
 */
public class HomeFragment extends Fragment implements OnClickListener,
		OnGestureListener, SensorEventListener,OutdoorAQIListener {

	/** The scale down/up outdoor animator set. */
	private AnimatorSet scaleDownIndoorAnimatorSet,
			scaleDownOutdoorAnimatorSet, fadeInAnimatorSetIndoor,
			fadeOutAnimatorSetIndoor, fadeInAnimatorSetOutdoor,
			fadeOutAnimatorSetOutdoor;
	/** The scaling animation variables. */
	private ObjectAnimator scaleUpIndoor, scaleDownIndoor, scaleUpOutdoor,
			scaleDownOutdoor, scaleUpIndoorRing, scaleUpOutdoorRing,
			scaleDownIndoorRing, scaleDownOutdoorRing, translateUpOutdoorInfo,
			translateDownOutdoorInfo, fadeInIndoorRingQuad1,
			fadeOutIndoorRingQuad1, fadeInIndoorRingQuad2,
			fadeOutIndoorRingQuad2, fadeInIndoorRingQuad3,
			fadeOutIndoorRingQuad3, fadeInIndoorRingQuad4,
			fadeOutIndoorRingQuad4, fadeInOutdoorRingQuad1,
			fadeOutOutdoorRingQuad1, fadeInOutdoorRingQuad2,
			fadeOutOutdoorRingQuad2, fadeInOutdoorRingQuad3,
			fadeOutOutdoorRingQuad3, fadeInOutdoorRingQuad4,
			fadeOutOutdoorRingQuad4;

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

	private CustomTextView tvDay, tvTime, tvCityName, tvDayOutdoor,
			tvTimeOutdoor;
	
	private CustomTextView tvOutdoorDay,tvOutdoorTime ;

	private TextView tvIndoorAQI, tvOutdoorAQI;
	
	private TextView outdoorAQI ;

	/** The params outdoor. */
	FrameLayout.LayoutParams paramsIndoor, paramsOutdoor;

	/** The main view. */
	View vMain;

	private DatabaseAdapter dbAdapter;

	private ImageView ivIndoorQuad1, ivIndoorQuad2, ivIndoorQuad3,
			ivIndoorQuad4, ivOutdoorQuad1, ivOutdoorQuad2, ivOutdoorQuad3,
			ivOutdoorQuad4;

	private ImageView ivLeftMenu, ivCenterLabel, ivRightDeviceIcon, ivMap,
			ivTrend,ivFanIndicator;

	OnIndoorRingClick mCallback;

	// Container Activity must implement this interface
	public interface OnIndoorRingClick {
		public void onRingClicked(int aqi);
	}

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
		initialiseNavigationBar();
		initialiseViews();
		initialiseAnimations();
		startOutdoorAQITask() ;
		return vMain;
	}
	
	/**
	 * Starts the Outdoor AQI task.
	 * This method calls a webservice and fetches the Outdoor AQI from the same
	 */
	private void startOutdoorAQITask() {
		TaskGetHttp chinaAQI = new TaskGetHttp("http://www.stateair.net/web/rss/1/4.xml", 2,getActivity(),this) ;
		chinaAQI.start() ;
	}

	@Override
	public void onResume() {

		super.onResume();
		SensorDataController.getInstance(getActivity()).registerListener(this);
		if (isIndoorExpanded)
			startAnimationsIndoor();
		else
			startAnimationsOutdoor();
		
		//updateOutdoorAQIFields() ;
	}
	
	/**
	 * Updates the outdoor AQI index
	 */
	private void updateOutdoorAQIFields() {
		OutdoorAQIEventDto outdoorAQIDto = dbAdapter.getLastOutdoorAQI() ;
		if( outdoorAQIDto != null ) {
			Log.i(TAG, ""+outdoorAQIDto.getOutdoorAQI()) ;
			outdoorAQI.setText(String.valueOf(outdoorAQIDto.getOutdoorAQI())) ;
			Log.i(TAG, outdoorAQIDto.getSyncDateTime()) ;
			tvOutdoorDay.setText(outdoorAQIDto.getSyncDateTime().substring(0, 10));
			tvOutdoorTime.setText(outdoorAQIDto.getSyncDateTime().substring(11, 16));
		}
	}

	/**
	 * Initialize views.
	 */
	private void initialiseViews() {

		rlIndoorSection = (RelativeLayout) vMain
				.findViewById(R.id.rlIndoorSection);
		rlIndoorSection.setPivotX(0f);
		rlIndoorSection.setPivotY(0f);
		
		
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
		flIndoorRing.setOnClickListener(this);
		flOutdoorRing = (FrameLayout) vMain.findViewById(R.id.flOutdoorRing);
		flOutdoorRing.setOnClickListener(this);
		rlOutdoorInfo = (RelativeLayout) vMain.findViewById(R.id.rlOutdoorInfo);

		tvIndoorAQI = (TextView) vMain.findViewById(R.id.tvIndoorAQI);
		
		outdoorAQI = (TextView) vMain.findViewById(R.id.tvOutdoorAQI) ;

		ivIndoorQuad1 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad1);
		ivIndoorQuad2 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad2);
		ivIndoorQuad3 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad3);
		ivIndoorQuad4 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad4);

		ivOutdoorQuad1 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad1);
		ivOutdoorQuad2 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad2);
		ivOutdoorQuad3 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad3);
		ivOutdoorQuad4 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad4);

		tvDay = (CustomTextView) vMain.findViewById(R.id.tvDay);
		
		tvOutdoorDay = (CustomTextView) vMain.findViewById(R.id.tvDayOutdoor) ;
		tvOutdoorTime = (CustomTextView) vMain.findViewById(R.id.tvTimeOutdoor) ;

		tvTime = (CustomTextView) vMain.findViewById(R.id.tvTime);

		ivMap = (ImageView) vMain.findViewById(R.id.ivMap);
		ivMap.setOnClickListener(this);
		ivTrend = (ImageView) vMain.findViewById(R.id.ivTrend);
		ivTrend.setOnClickListener(this);

		tvCityName = (CustomTextView) vMain.findViewById(R.id.tvCityName);
		tvDayOutdoor = (CustomTextView) vMain.findViewById(R.id.tvDayOutdoor);
		tvTimeOutdoor = (CustomTextView) vMain.findViewById(R.id.tvTimeOutdoor);
		tvOutdoorAQI = (TextView) vMain.findViewById(R.id.tvOutdoorAQI);
		
		ivFanIndicator = (ImageView) vMain.findViewById(R.id.ivIndicator);

		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		AirPurifierEventDto dto = dbAdapter.getLastUpdatedEvent();

		if (dto != null) {
			tvIndoorAQI.setText(String.valueOf(dto.getIndoorAQI()));
			tvDay.setText(dto.getTimeStamp().substring(0, 10));
			tvTime.setText(dto.getTimeStamp().substring(11, 16));
			updateIndoorAQIRing(dto.getIndoorAQI());
			updateIndoorBackground(dto.getIndoorAQI());
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
		scaleDownIndoorAnimatorSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				stopAnimationsIndoor();
				stopAnimationsOutdoor();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				startAnimationsOutdoor();

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});

		scaleDownOutdoorAnimatorSet = new AnimatorSet();
		scaleDownOutdoorAnimatorSet.setDuration(AppConstants.DURATION);
		scaleDownOutdoorAnimatorSet.playTogether(scaleUpIndoor,
				scaleUpIndoorRing, scaleDownOutdoor, scaleDownOutdoorRing,
				translateDownOutdoorInfo);
		scaleDownOutdoorAnimatorSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				stopAnimationsIndoor();
				stopAnimationsOutdoor();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				startAnimationsIndoor();

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});

		// Indoor Ring Fade in Fade out
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
		fadeOutIndoorRingQuad3.setStartDelay(AppConstants.FADEDELAY * 2);

		fadeOutIndoorRingQuad4 = ObjectAnimator.ofFloat(ivIndoorQuad4, "alpha",
				0.1f, 1f);
		fadeOutIndoorRingQuad4.setDuration(AppConstants.DURATION);
		fadeOutIndoorRingQuad4.setStartDelay(AppConstants.FADEDELAY * 3);

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
		fadeInIndoorRingQuad3.setStartDelay(AppConstants.FADEDELAY * 2);

		fadeInIndoorRingQuad4 = ObjectAnimator.ofFloat(ivIndoorQuad4, "alpha",
				1f, 0.1f);
		fadeInIndoorRingQuad4.setDuration(AppConstants.FADEDURATION);
		fadeInIndoorRingQuad4.setStartDelay(AppConstants.FADEDELAY * 3);

		// Outdoor Ring fade in fade out
		fadeOutOutdoorRingQuad1 = ObjectAnimator.ofFloat(ivOutdoorQuad1,
				"alpha", 0.1f, 1f);
		fadeOutOutdoorRingQuad1.setDuration(AppConstants.FADEDURATION);

		fadeOutOutdoorRingQuad2 = ObjectAnimator.ofFloat(ivOutdoorQuad2,
				"alpha", 0.1f, 1f);
		fadeOutOutdoorRingQuad2.setDuration(AppConstants.FADEDURATION);
		fadeOutOutdoorRingQuad2.setStartDelay(AppConstants.FADEDELAY);

		fadeOutOutdoorRingQuad3 = ObjectAnimator.ofFloat(ivOutdoorQuad3,
				"alpha", 0.1f, 1f);
		fadeOutOutdoorRingQuad3.setDuration(AppConstants.FADEDURATION);
		fadeOutOutdoorRingQuad3.setStartDelay(AppConstants.FADEDELAY * 2);

		fadeOutOutdoorRingQuad4 = ObjectAnimator.ofFloat(ivOutdoorQuad4,
				"alpha", 0.1f, 1f);
		fadeOutOutdoorRingQuad4.setDuration(AppConstants.DURATION);
		fadeOutOutdoorRingQuad4.setStartDelay(AppConstants.FADEDELAY * 3);

		fadeInOutdoorRingQuad1 = ObjectAnimator.ofFloat(ivOutdoorQuad1,
				"alpha", 1f, 0.1f);
		fadeInOutdoorRingQuad1.setDuration(AppConstants.FADEDURATION);

		fadeInOutdoorRingQuad2 = ObjectAnimator.ofFloat(ivOutdoorQuad2,
				"alpha", 1f, 0.1f);
		fadeInOutdoorRingQuad2.setDuration(AppConstants.FADEDURATION);
		fadeInOutdoorRingQuad2.setStartDelay(AppConstants.FADEDELAY);

		fadeInOutdoorRingQuad3 = ObjectAnimator.ofFloat(ivOutdoorQuad3,
				"alpha", 1f, 0.1f);
		fadeInOutdoorRingQuad3.setDuration(AppConstants.FADEDURATION);
		fadeInOutdoorRingQuad3.setStartDelay(AppConstants.FADEDELAY * 2);

		fadeInOutdoorRingQuad4 = ObjectAnimator.ofFloat(ivOutdoorQuad4,
				"alpha", 1f, 0.1f);
		fadeInOutdoorRingQuad4.setDuration(AppConstants.FADEDURATION);
		fadeInOutdoorRingQuad4.setStartDelay(AppConstants.FADEDELAY * 3);

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
		switch (v.getId()) {
		case R.id.flIndoorRing:
			Log.i(TAG, "On Indoor Ring click!!!");
			int iAQI = Integer.parseInt(tvIndoorAQI.getText().toString());
			mCallback.onRingClicked(iAQI);
			/*
			 * getActivity() .getSupportFragmentManager() .beginTransaction()
			 * .replace(R.id.llContainer, new IndoorDetailsFragment(),
			 * IndoorDetailsFragment.TAG).commit();
			 */
			break;

		case R.id.flOutdoorRing:
			Log.i(TAG, "On Outdoor Ring click!!!");
			getActivity()
					.getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.llContainer, new OutdoorDetailsFragment(),
							OutdoorDetailsFragment.TAG).addToBackStack(null)
					.commit();
			break;

		case R.id.ivTrend:
			Log.i(TAG, "On Trend click!!!");
			getActivity().startActivity(
					new Intent(getActivity(), TrendsActivity.class));
			break;

		default:
			break;
		}
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
		int iOutdoorAQI = (int) (Math.random() * 500);
		updateOutdoorBackground(iOutdoorAQI);
		updateOutdoorAQIRing(iOutdoorAQI);
		updateOutdoorInfo(iOutdoorAQI);
		if (airPurifierEventDto != null) {
			int iIndoorAQI = airPurifierEventDto.getIndoorAQI();
			//int iIndoorAQI = (int) (Math.random() * 500);
			updateIndoorBackground(iIndoorAQI);
			updateIndoorAQIRing(iIndoorAQI);
			updateIndoorInfo(iIndoorAQI);
			
		}
	}

	private void updateIndoorInfo(int indoorAQI) {
		String currentDateTime = Utils.getCurrentDateTime();
		tvIndoorAQI.setText(String.valueOf(indoorAQI));
		tvDay.setText(currentDateTime.substring(0, 10));
		tvTime.setText(currentDateTime.substring(11, 16));
	}

	private void updateOutdoorInfo(int outdoorAQI) {
		String currentDateTime = Utils.getCurrentDateTime();
		tvOutdoorAQI.setText(String.valueOf(outdoorAQI));
		tvCityName.setText("Shanghai");
		tvDayOutdoor.setText(currentDateTime.substring(0, 10));
		tvTimeOutdoor.setText(currentDateTime.substring(11, 16));

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
		
		ivFanIndicator.setImageResource(Utils.getResourceID(Utils.getFanIndicator(iAQI), getActivity()));

	}

	private void updateOutdoorAQIRing(int iAQI) {
		String[] INDOOR_RING = Utils.getIndoorRing(iAQI);
		ivOutdoorQuad1.setImageResource(Utils.getResourceID(INDOOR_RING[0],
				getActivity()));
		ivOutdoorQuad2.setImageResource(Utils.getResourceID(INDOOR_RING[1],
				getActivity()));
		ivOutdoorQuad3.setImageResource(Utils.getResourceID(INDOOR_RING[2],
				getActivity()));
		ivOutdoorQuad4.setImageResource(Utils.getResourceID(INDOOR_RING[3],
				getActivity()));

	}

	private void updateIndoorBackground(int iIndoorAqi) {
		rlIndoorSection.setBackgroundResource(Utils.getResourceID(
				Utils.getIndoorBG(iIndoorAqi), getActivity()));
	}

	private void updateOutdoorBackground(int iOutdoorAQI) {
		rlOutdoorSection.setBackgroundResource(Utils.getResourceID(
				Utils.getOutdoorBG(iOutdoorAQI), getActivity()));
	}

	@Override
	public void onPause() {
		Log.i(TAG, "OnPause");
		SensorDataController.getInstance(getActivity())
				.unRegisterListener(this);
		stopAnimationsIndoor();
		stopAnimationsOutdoor();
		super.onPause();
	}

	private void startAnimationsIndoor() {
		fadeInAnimatorSetIndoor = new AnimatorSet();
		fadeInAnimatorSetIndoor.playTogether(fadeInIndoorRingQuad1,
				fadeInIndoorRingQuad2, fadeInIndoorRingQuad3,
				fadeInIndoorRingQuad4);
		// fadeInAnimatorSet.start();
		fadeInAnimatorSetIndoor.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG, "Animation started : Fade IN");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				Log.i(TAG, "Animation repeated : Fade IN");
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (fadeOutAnimatorSetIndoor != null)
					fadeOutAnimatorSetIndoor.start();

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i(TAG, "Animation canceled : Fade IN");
			}
		});

		fadeOutAnimatorSetIndoor = new AnimatorSet();
		fadeOutAnimatorSetIndoor.playTogether(fadeOutIndoorRingQuad1,
				fadeOutIndoorRingQuad2, fadeOutIndoorRingQuad3,
				fadeOutIndoorRingQuad4);
		fadeOutAnimatorSetIndoor.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				//Log.i(TAG, "Animation started : Fade OUT");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				//Log.i(TAG, "Animation Repat : Fade OUT");
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (fadeInAnimatorSetIndoor != null)
					fadeInAnimatorSetIndoor.start();

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i(TAG, "Animation Canceled : Fade OUT");

			}
		});

		if (fadeInAnimatorSetIndoor != null
				&& !fadeInAnimatorSetIndoor.isRunning()) {
			fadeInAnimatorSetIndoor.start();
		}
	}

	private void startAnimationsOutdoor() {
		fadeInAnimatorSetOutdoor = new AnimatorSet();
		fadeInAnimatorSetOutdoor.playTogether(fadeInOutdoorRingQuad1,
				fadeInOutdoorRingQuad2, fadeInOutdoorRingQuad3,
				fadeInOutdoorRingQuad4);
		// fadeInAnimatorSet.start();
		fadeInAnimatorSetOutdoor.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG, "Animation started : Fade IN");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				Log.i(TAG, "Animation repeated : Fade IN");
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (fadeOutAnimatorSetOutdoor != null)
					fadeOutAnimatorSetOutdoor.start();

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i(TAG, "Animation canceled : Fade IN");
			}
		});

		fadeOutAnimatorSetOutdoor = new AnimatorSet();
		fadeOutAnimatorSetOutdoor.playTogether(fadeOutOutdoorRingQuad1,
				fadeOutOutdoorRingQuad2, fadeOutOutdoorRingQuad3,
				fadeOutOutdoorRingQuad4);
		fadeOutAnimatorSetOutdoor.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG, "Animation started : Fade OUT");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				Log.i(TAG, "Animation Repat : Fade OUT");
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (fadeInAnimatorSetOutdoor != null)
					fadeInAnimatorSetOutdoor.start();

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i(TAG, "Animation Canceled : Fade OUT");

			}
		});

		if (fadeInAnimatorSetOutdoor != null
				&& !fadeInAnimatorSetOutdoor.isRunning()) {
			fadeInAnimatorSetOutdoor.start();
		}
	}

	private void stopAnimationsIndoor() {
		if (fadeInAnimatorSetIndoor != null
				&& fadeInAnimatorSetIndoor.isRunning()) {
			fadeInAnimatorSetIndoor.end();
			fadeInAnimatorSetIndoor = null;
		}
		if (fadeOutAnimatorSetIndoor != null
				&& fadeOutAnimatorSetIndoor.isRunning()) {
			fadeOutAnimatorSetIndoor.end();
			fadeInAnimatorSetIndoor = null;
		}
	}

	private void stopAnimationsOutdoor() {
		if (fadeInAnimatorSetOutdoor != null
				&& fadeInAnimatorSetOutdoor.isRunning()) {
			fadeInAnimatorSetOutdoor.end();
			fadeInAnimatorSetOutdoor = null;
		}
		if (fadeOutAnimatorSetOutdoor != null
				&& fadeOutAnimatorSetOutdoor.isRunning()) {
			fadeOutAnimatorSetOutdoor.end();
			fadeOutAnimatorSetOutdoor = null;
		}
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "OnDestroy");
		storeLastEvent();
		super.onDestroy();
	}

	/**
	 * This method stores the latest air purifier event on application exit.
	 */
	private void storeLastEvent() {
		dbAdapter.insertAirPurifierEvent(Integer.parseInt(tvIndoorAQI.getText()
				.toString()));
		dbAdapter.close();
	}

	private void initialiseNavigationBar() {
		ivLeftMenu = (ImageView) getActivity().findViewById(R.id.ivLeftMenu);
		ivRightDeviceIcon = (ImageView) getActivity().findViewById(
				R.id.ivRightDeviceIcon);
		ivCenterLabel = (ImageView) getActivity().findViewById(
				R.id.ivCenterLabel);

		ivLeftMenu.setBackgroundResource(R.drawable.menu_icon);
		ivRightDeviceIcon.setBackgroundResource(R.drawable.device_icon);
		ivCenterLabel.setBackgroundResource(R.drawable.label_my_iaq);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnIndoorRingClick) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}
	
	/**
	 * This runnable will be called by the handler after every one hour to get the latest outdoor AQI
	 */
	private final Runnable getOutdoorAQIRunnable = new Runnable() {
		@Override
		public void run() {
			startOutdoorAQITask() ;
		}
	};
	
	/**
	 * This will start a timer for 1 hour to fetch the latest data
	 */
	private void startOutdoorAQITimer() {
		outdoorAQIHandler.postDelayed(getOutdoorAQIRunnable, AppConstants.OUTDOOR_AQI_UPDATE_DURATION) ;
	}

	
	private Handler outdoorAQIHandler = new Handler() ;
	/**
	 * Handler to update the User Interface
	 */
	 private final Handler handler = new Handler() {
         public void handleMessage(Message msg) {
              //updateOutdoorAQIFields();
         };
	  } ;
	  
	/**
	 * Callback from outdoorAQI class
	 */
	@Override
	public void updateOutdoorAQI() {
		handler.sendEmptyMessage(0) ;
		startOutdoorAQITimer() ;
		
	}

}
