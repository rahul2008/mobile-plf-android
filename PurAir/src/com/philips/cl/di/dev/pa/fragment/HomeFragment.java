package com.philips.cl.di.dev.pa.fragment;


import static com.philips.cl.di.dev.pa.constant.AnimatorConstants.*;
import static com.philips.cl.di.dev.pa.constant.AppConstants.*;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirTutorialActivity;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.listeners.AnimationListener;
import com.philips.cl.di.dev.pa.network.TaskGetHttp;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData.WeatherDataListener;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.Fonts;
import com.philips.cl.di.dev.pa.utils.Utils;

public class HomeFragment extends BaseFragment implements OnClickListener, OnGestureListener, WeatherDataListener, ServerResponseListener {

	/** The Constant TAG. */
	public final static String TAG = HomeFragment.class.getSimpleName();

	/** The main view. */
	private View vMain;

	/** The is indoor expanded. */
	private boolean isIndoorExpanded = true;

	/** Dashboard ImageViews */
	private ImageView ivIndoorCircle, ivIndoorMeter, ivOutdoorCircle, ivOutdoorMeter, ivOutdoorWeatherImage,
						ivFanSpeedIcon, ivFilterStatusIcon;

	/** Dashboard TextViews */
	private TextView tvIndoorAQI, tvIndoorTitle, tvIndoorComment, tvIndoorModeTitle, tvFilterStatusTitle,
	tvIndoorModeValue, tvIndoorFilterStatus, tvFilterHome,
	tvOutdoorAQI, tvOutdoorTitle, tvOutdoorComment,
	tvUpdatedTitle, tvUpdatedAtDate, tvCity, tvLocality, tvOutdoorTemperature;
	
	private String purifierName;

	private AnimatorSet scaleDownIndoorFragment, scaleUpIndoorFragment, 
	scaleDownOutdoorFragment, scaleUpOutdoorFragment, initAnimationLocations;

	private AnimationListener animationListener;

	/** Indoor and outdoor LinearLayouts which listen for gestures*/
	private LinearLayout llIndoor, llOutdoor;

	/** Indoor and outdoor layout height*/
	private int indoorHeight, outdoorHeight;

	/** Gesture listener to detect fling*/
	private GestureDetectorCompat gestureDetectorCompat;
	
	private Button takeAction;
	boolean bBtnTakeActionVisible;

	/** Dashboard values*/
	private float indoorAQIValue;
	private int indoorPSense, outdoorAQIValue, outdoorTemperature;
	private String outdoorUpdatedAt = "", outdoorWeatherDesc, isDayTime;
	private boolean rotateOutdoorCircle, rotateIndoorCircle, updateOutdoorDashboard;
	private String dergreeRotatePointer;

	private LinearLayout takeTourLayout;

	private static OutdoorAQIEventDto outdoorAQIEventDto;
	private static List<Weatherdto> weatherDtoList; 
	private static String currentCityTime;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		vMain = inflater.inflate(R.layout.rl_home_master_fragment, container, false);
		((ViewGroup) vMain).setClipChildren(false);

		gestureDetectorCompat = new GestureDetectorCompat(getActivity(), this);
		animationListener = new AnimationListener(getActivity());
		initViews();
		initAnimations();
		startOutdoorAQITask() ;
		startWeatherDataTask() ;
		
		isIndoorExpanded = true;
		rotateOutdoorCircle = true;
		rotateIndoorCircle = true;
		bBtnTakeActionVisible = false;
		
		if(SessionDto.getSessionDto() != null && SessionDto.getSessionDto().getCityDetails() != null)
			Log.i(TAG, "OutdoorLocations " + SessionDto.getSessionDto().getCityDetails().getCities());
		
		if(MainActivity.getAirPurifierEventDto() != null) {
			setIndoorDashBoardValues(indoorAQIValue);
			setHomeName(purifierName);
		} else {
			ivIndoorMeter.setVisibility(View.INVISIBLE);
		}
		if(updateOutdoorDashboard)
			setOutdoorDashboardValues(outdoorAQIValue);
		
		
		return vMain;
	}

	/** Update dashboard values on resume*/
	private void setIndoorDashBoardValues(float pSense) {
		//Show Take Action button only if AQI is Very Unhealthy
		if (pSense > 3.5f )
		{
			bBtnTakeActionVisible = true;
			takeAction.setVisibility(View.VISIBLE);
		}
		else
		{
			bBtnTakeActionVisible = false;
			takeAction.setVisibility(View.INVISIBLE);
		}
		showIndoorGuage();
		tvIndoorAQI.setText(setIndoorPSenseText(pSense));
		setIndoorAQIStatusAndComment(indoorPSense);
		ivIndoorCircle.setImageDrawable(setIndoorCircleBackground(pSense));
		setMode(Utils.getMode(MainActivity.getAirPurifierEventDto().getFanSpeed(), getActivity()));
		setFilterStatus(Utils.getFilterStatusForDashboard(MainActivity.getAirPurifierEventDto()));
	}

	/** Update dashboard values on resume*/
	private void setOutdoorDashboardValues(int outdoorAQI) {
		tvOutdoorAQI.setText(String.valueOf(outdoorAQI));
		setOutdoorAQIStatusAndComment(outdoorAQI);
		ivOutdoorCircle.setImageDrawable(setAQICircleBackground(outdoorAQI));
		Log.i(TAG, "setOutdoorDashboardValues$outdoorUpdatedAt " + outdoorUpdatedAt);
		setUpdatedAtTime(outdoorUpdatedAt);
		setOutdoorTemperature(outdoorTemperature);
		setOutdoorTemperatureImage(outdoorWeatherDesc, isDayTime);
	}

	public void rotateOutdoorCircle() {
		if(rotateOutdoorCircle) {
			rotateAQICircle(outdoorAQIValue, ivOutdoorCircle);
			rotateOutdoorCircle = false;
		}
	}

	@Override
	public void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
	}


	@Override
	public void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
		stopAllAnimations();
	}

	private void stopAllAnimations() {
		scaleDownIndoorFragment.end();
		scaleDownOutdoorFragment.end();
		scaleUpIndoorFragment.end();
		scaleUpOutdoorFragment.end();
	}


	private void initViews() {

		llIndoor = (LinearLayout) vMain.findViewById(R.id.ll_home_indoor_2);
		llIndoor.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetectorCompat.onTouchEvent(event);
			}
		});

		llOutdoor = (LinearLayout) vMain.findViewById(R.id.ll_home_outdoor_2);
		llOutdoor.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetectorCompat.onTouchEvent(event);
			}
		});

		LayoutParams params = (LayoutParams) llIndoor.getLayoutParams();
		indoorHeight = params.height = (int) ((MainActivity.getScreenHeight() * 0.7) - (MainActivity.getScreenHeight() * 0.075));

		llIndoor.setLayoutParams(params);
		params = (LayoutParams) llOutdoor.getLayoutParams(); 
		outdoorHeight = params.height = (int) ((MainActivity.getScreenHeight() * 0.7) - (MainActivity.getScreenHeight() * 0.075));
		llOutdoor.setLayoutParams(params);
		Log.i(TAG, " llindoor " + params.height + " lloutdoor " + llOutdoor.getLayoutParams().height + " :: " + indoorHeight + " :: " + outdoorHeight);
		ivIndoorCircle = (ImageView) vMain.findViewById(R.id.indoor_circle_pointer);
		ivIndoorCircle.setOnClickListener(this);
		
		ivIndoorMeter = (ImageView) vMain.findViewById(R.id.indoor_circle_meter);
		tvFilterHome = (TextView) vMain.findViewById(R.id.tv_filter_home);
		ivFanSpeedIcon = (ImageView) vMain.findViewById(R.id.iv_filter_icon);
		tvIndoorModeTitle = (TextView) vMain.findViewById(R.id.tv_filter_mode_title);
		tvIndoorModeValue = (TextView) vMain.findViewById(R.id.tv_filter_mode_value);
		tvFilterStatusTitle = (TextView) vMain.findViewById(R.id.tv_filter_status_title);
		tvIndoorFilterStatus = (TextView) vMain.findViewById(R.id.tv_filter_status_value);
		ivFilterStatusIcon = (ImageView) vMain.findViewById(R.id.iv_fan_speed_icon);
		tvIndoorAQI = (TextView) vMain.findViewById(R.id.indoor_aqi_reading);
		tvIndoorAQI.setTypeface(Fonts.getGillsansLight(getActivity()));
		tvIndoorTitle = (TextView) vMain.findViewById(R.id.tv_indoor_aqi_status_title);
		tvIndoorTitle.setOnClickListener(this);
		tvIndoorComment = (TextView) vMain.findViewById(R.id.tv_indoor_aqi_status_message);
		tvIndoorComment.setOnClickListener(this);
		ivOutdoorCircle = (ImageView) vMain.findViewById(R.id.outdoor_circle_pointer);
		ivOutdoorCircle.setOnClickListener(this);

		//Take Action Button
		takeAction = (Button) vMain.findViewById(R.id.take_action); 
		takeAction.setOnClickListener(new OnClickListener() { 
		@Override
		public void onClick(View v) {
			DrawerLayout mDrawerLayout = ((MainActivity)getActivity()).getDrawerLayout();
			ScrollView mScrollViewRight = ((MainActivity)getActivity()).getScrollViewRight();
			mDrawerLayout.openDrawer(mScrollViewRight);
			}
		}); 
					
		ivOutdoorMeter = (ImageView) vMain.findViewById(R.id.outdoor_circle_meter);

		tvOutdoorAQI = (TextView) vMain.findViewById(R.id.outdoor_aqi_reading);
		tvOutdoorAQI.setTypeface(Fonts.getGillsansLight(getActivity()));
		tvOutdoorTitle = (TextView) vMain.findViewById(R.id.tv_outdoor_aqi_status_title);
		tvOutdoorComment = (TextView) vMain.findViewById(R.id.tv_outdoor_aqi_status_message);
		tvUpdatedTitle = (TextView) vMain.findViewById(R.id.tv_updated_title);
		tvUpdatedAtDate = (TextView) vMain.findViewById(R.id.tv_updated_at_date);
		//		tvUpdatedAtDate.setText("Oh my " + System.getProperty("line.separator")+ "  God");
		tvCity = (TextView) vMain.findViewById(R.id.tv_location_city);
		tvLocality = (TextView) vMain.findViewById(R.id.tv_location);
		ivOutdoorWeatherImage = (ImageView) vMain.findViewById(R.id.iv_outdoor_weather_image);
		tvOutdoorTemperature = (TextView) vMain.findViewById(R.id.tv_outdoor_weather_value);

		//if a new user show tutorial prompt, visible for only three visits
		if(((MainActivity)getActivity()).getVisits()<=3 && !((MainActivity)getActivity()).isTutorialPromptShown){
			//tutorial tour prompt			
			takeTourLayout= (LinearLayout) vMain.findViewById(R.id.take_tour_prompt_drawer);
			Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
			takeTourLayout.startAnimation(bottomUp);
			takeTourLayout.setVisibility(View.VISIBLE);			
			TextView lblTakeTour=(TextView) vMain.findViewById(R.id.lbl_take_tour);
			lblTakeTour.setOnClickListener(this);
			lblTakeTour.setTypeface(Fonts.getGillsans(getActivity()));
			ImageButton btnCloseTourLayout=(ImageButton) vMain.findViewById(R.id.btn_close_tour_layout);
			btnCloseTourLayout.setOnClickListener(this);
		}
	}

	private void initAnimations() {
		scaleDownIndoorFragment = new AnimatorSet();
		scaleDownIndoorFragment.playTogether(
				ObjectAnimator.ofFloat(llIndoor, ANIM_TRANSLATION_Y, indoorBackgroundTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeTitle, ANIM_TRANSLATION_Y, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivFanSpeedIcon, ANIM_TRANSLATION_Y, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeValue, ANIM_TRANSLATION_Y, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterStatusTitle, ANIM_TRANSLATION_Y, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterHome, ANIM_TRANSLATION_Y, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorFilterStatus, ANIM_TRANSLATION_Y, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivFilterStatusIcon, ANIM_TRANSLATION_Y, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivIndoorCircle, ANIM_SCALE_X, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorCircle, ANIM_SCALE_Y, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorCircle, ANIM_TRANSLATION_Y, indoorCircleScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorAQI, ANIM_TRANSLATION_Y, indoorCircleScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorAQI, ANIM_SCALE_X, 0.6f),
				ObjectAnimator.ofFloat(tvIndoorAQI, ANIM_SCALE_Y, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_SCALE_X, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_SCALE_Y, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_TRANSLATION_Y, indoorCircleScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_ALPHA, 0),
				ObjectAnimator.ofFloat(tvIndoorTitle, ANIM_ALPHA, 0),
				ObjectAnimator.ofFloat(tvIndoorComment, ANIM_ALPHA, 0));

		scaleDownIndoorFragment.setDuration(ANIM_DURATION);
		scaleDownIndoorFragment.addListener(animationListener);

		scaleUpIndoorFragment = new AnimatorSet();
		scaleUpIndoorFragment.playTogether(
				ObjectAnimator.ofFloat(llIndoor, ANIM_TRANSLATION_Y, 0),
				ObjectAnimator.ofFloat(tvIndoorModeTitle, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivFanSpeedIcon, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeValue, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterStatusTitle, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterHome, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorFilterStatus, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivFilterStatusIcon, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivIndoorCircle, ANIM_SCALE_X, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorCircle, ANIM_SCALE_Y, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorCircle, ANIM_TRANSLATION_Y, 0),
				ObjectAnimator.ofFloat(tvIndoorAQI, ANIM_TRANSLATION_Y, 0),
				ObjectAnimator.ofFloat(tvIndoorAQI, ANIM_SCALE_X, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(tvIndoorAQI, ANIM_SCALE_Y, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_SCALE_X, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_SCALE_Y, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_TRANSLATION_Y, 0),
				ObjectAnimator.ofFloat(ivIndoorMeter, ANIM_ALPHA, 0, 1),
				ObjectAnimator.ofFloat(tvIndoorTitle, ANIM_ALPHA, 0, 1),
				ObjectAnimator.ofFloat(tvIndoorComment, ANIM_ALPHA, 0, 1));
		scaleUpIndoorFragment.setDuration(ANIM_DURATION);
		scaleUpIndoorFragment.addListener(animationListener);

		scaleDownOutdoorFragment = new AnimatorSet();
		scaleDownOutdoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivOutdoorCircle, ANIM_SCALE_X, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, ANIM_SCALE_Y, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, ANIM_TRANSLATION_Y, outdoorCircleScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvOutdoorAQI, ANIM_TRANSLATION_Y, outdoorCircleScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvOutdoorAQI, ANIM_SCALE_X, 0.6f),
				ObjectAnimator.ofFloat(tvOutdoorAQI, ANIM_SCALE_Y, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_SCALE_X, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_SCALE_Y, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_TRANSLATION_Y, outdoorCircleScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_ALPHA, 0),
				ObjectAnimator.ofFloat(tvOutdoorTitle, ANIM_ALPHA, 0),
				ObjectAnimator.ofFloat(tvOutdoorComment, ANIM_ALPHA, 0),
				ObjectAnimator.ofFloat(tvUpdatedTitle, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvUpdatedAtDate, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvCity, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, ANIM_ALPHA, 0),
				ObjectAnimator.ofFloat(tvOutdoorTemperature, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(ivOutdoorWeatherImage, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight)));
		scaleDownOutdoorFragment.setDuration(ANIM_DURATION);

		scaleUpOutdoorFragment = new AnimatorSet();
		scaleUpOutdoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivOutdoorCircle, ANIM_SCALE_X, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, ANIM_SCALE_Y, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, ANIM_TRANSLATION_Y, outdoorCircleScaleDownTranslationY(outdoorHeight), 0),
				ObjectAnimator.ofFloat(tvOutdoorAQI, ANIM_TRANSLATION_Y, outdoorCircleScaleDownTranslationY(outdoorHeight), 0),
				ObjectAnimator.ofFloat(tvOutdoorAQI, ANIM_SCALE_X, 0.6f, 1f),
				ObjectAnimator.ofFloat(tvOutdoorAQI, ANIM_SCALE_Y, 0.6f, 1f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_SCALE_X, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_SCALE_Y, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_TRANSLATION_Y, outdoorCircleScaleDownTranslationY(outdoorHeight), 0),
				ObjectAnimator.ofFloat(ivOutdoorMeter, ANIM_ALPHA, 0, 1),
				ObjectAnimator.ofFloat(tvOutdoorTitle, ANIM_ALPHA, 0, 1),
				ObjectAnimator.ofFloat(tvOutdoorComment, ANIM_ALPHA, 0, 1),
				ObjectAnimator.ofFloat(tvUpdatedTitle, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvUpdatedAtDate, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvCity, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, ANIM_ALPHA, 0, 1),
				ObjectAnimator.ofFloat(tvLocality, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, ANIM_ALPHA, 1.0f),
				ObjectAnimator.ofFloat(tvOutdoorTemperature, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(ivOutdoorWeatherImage, ANIM_TRANSLATION_Y, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)));
		scaleUpOutdoorFragment.setDuration(ANIM_DURATION);

		//Only used once to align the outdoor fragment elements at the launch of the application.
		initAnimationLocations = new AnimatorSet();
		initAnimationLocations = scaleDownOutdoorFragment.clone();
		initAnimationLocations.playTogether(
				ObjectAnimator.ofFloat(tvIndoorModeTitle, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivFanSpeedIcon, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeValue, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterStatusTitle, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterHome, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivFilterStatusIcon, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorFilterStatus, ANIM_TRANSLATION_Y, indoorTextScaleUpTranslationY(indoorHeight)));
		initAnimationLocations.setDuration(0);
		initAnimationLocations.start();
	}
	
	public void hideIndoorGuage() {
		Log.i(TAG, "Hide indoor guage");
		ivIndoorMeter.setVisibility(View.INVISIBLE);
	}
	
	public void showIndoorGuage() {
		Log.i(TAG, "Show indoor guage");
		ivIndoorMeter.setVisibility(View.VISIBLE);
	}
	
	public void hideOutdoorGuage() {
		ivOutdoorMeter.setVisibility(View.INVISIBLE);
	}
	
	public void showOutdoorGuage() {
		ivOutdoorMeter.setVisibility(View.VISIBLE);
	}
	

	/**
	 * Starts the Outdoor AQI task. This method calls a webservice and fetches
	 * the Outdoor AQI from the same
	 */
	public void startOutdoorAQITask() {
		if ( SessionDto.getInstance().getOutdoorEventDto() == null ) {
			TaskGetHttp shanghaiAQI = new TaskGetHttp(AppConstants.SHANGHAI_OUTDOOR_AQI_URL,getActivity(),this);
			shanghaiAQI.start() ;		
		}
		else {
			//updateOutdoorAQIFields() ;
		}
	}
	

	public void startWeatherDataTask() {
		if ( SessionDto.getInstance().getWeatherDetails() == null 
				|| SessionDto.getInstance().getWeatherDetails().size() == 0) {
			TaskGetWeatherData statusUpdateTask = new TaskGetWeatherData(String.format(AppConstants.WEATHER_SERVICE_URL,"31.2000,121.5000"),this);
			statusUpdateTask.start();
		}
		else {
			//updateWeatherFields() ;
		}
	}

	@Override
	public void onClick(View v) {
		MainActivity activity = (MainActivity) getActivity() ;
		activity.isClickEvent = true ;
		Log.i(TAG, "onClick " + v.getId());
		Intent intent;
		switch (v.getId()) {
		case R.id.indoor_circle_pointer:
		case R.id.tv_indoor_aqi_status_message:
		case R.id.tv_indoor_aqi_status_title:
			if(isIndoorExpanded) {
				//Show indoor details
				intent = new Intent(getActivity(),	IndoorDetailsActivity.class);
				String indoorInfos[] = new String[7];
				indoorInfos[0] = tvIndoorModeValue.getText().toString();
				indoorInfos[1] = tvIndoorFilterStatus.getText().toString();
				indoorInfos[2] = String.valueOf(indoorAQIValue);
				indoorInfos[3] = tvIndoorTitle.getText().toString();
				indoorInfos[4] = tvIndoorComment.getText().toString();
				indoorInfos[5] = tvIndoorTitle.getText().toString();
				indoorInfos[6] = purifierName;
				intent.putExtra("indoor", indoorInfos);
				startActivity(intent);
			}
			break;
		case R.id.outdoor_circle_pointer:
			if (updateOutdoorDashboard && !isIndoorExpanded) {
				intent = new Intent(getActivity(),	OutdoorDetailsActivity.class);
				String outdoorInfos[] = new String[10];
				outdoorInfos[0] = tvUpdatedAtDate.getText().toString();
				outdoorInfos[1] = tvCity.getText().toString();
				outdoorInfos[2] = tvLocality.getText().toString();
				outdoorInfos[3] = outdoorWeatherDesc;
				outdoorInfos[4] = tvOutdoorTemperature.getText().toString();
				outdoorInfos[5] = tvOutdoorTitle.getText().toString();
				outdoorInfos[6] = tvOutdoorComment.getText().toString();
				outdoorInfos[7] = tvOutdoorAQI.getText().toString();
				outdoorInfos[8] = dergreeRotatePointer;
				outdoorInfos[9] = isDayTime;
				intent.putExtra("outdoor", outdoorInfos);
				intent.putExtra("outdoorAqi", outdoorAQIEventDto);
				startActivity(intent);
			}
			break;
		case R.id.lbl_take_tour:
			intent = new Intent(getActivity(), AirTutorialActivity.class);
			startActivity(intent);
			takeTourLayout.setVisibility(View.GONE);
			((MainActivity)getActivity()).isTutorialPromptShown=true;
			break;
		case R.id.btn_close_tour_layout:
			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
			takeTourLayout.startAnimation(bottomDown);
			takeTourLayout.setVisibility(View.GONE);
			((MainActivity)getActivity()).isTutorialPromptShown=true;
			showTutorialDialog();
			break;
		default:
			break;
		}
	}


	private void showTutorialDialog()
	{
		// Created a new Dialog
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// inflate the layout
		dialog.setContentView(R.layout.tutorial_custom_dialog);

		TextView takeTourAlertLbl=(TextView) dialog.findViewById(R.id.take_tour_alert);
		Button btnClose=(Button) dialog.findViewById(R.id.btn_close);			
		Button btnTakeTour=(Button) dialog.findViewById(R.id.btn_take_tour);

		takeTourAlertLbl.setTypeface(Fonts.getGillsans(getActivity()));
		btnClose.setTypeface(Fonts.getGillsans(getActivity()));
		btnTakeTour.setTypeface(Fonts.getGillsans(getActivity()));

		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnTakeTour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentOd = new Intent(getActivity(), AirTutorialActivity.class);
				startActivity(intentOd);
				dialog.dismiss();
			}
		});

		// Display the dialog
		dialog.show();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		//		Log.i(TAG, "onDown");
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(animationListener != null && 
				animationListener.isIgnoreGesture()) {
			Log.i(TAG, "Returning from onFling");
			return false;
		}
		Log.i(TAG, "Processing onFling isIndoorExpanded " + isIndoorExpanded + " screenWidth " + MainActivity.getScreenWidth() + " screenHeight " + MainActivity.getScreenHeight() + " layoutHeight " + indoorHeight);
		float differenceY = (e2.getY() - e1.getY());
			
		if(Math.abs(differenceY) > SWIPE_THRESHOLD
				&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
			if(differenceY > 0) {
				if(!isIndoorExpanded) {
					scaleUpIndoorFragment.start();
					scaleDownOutdoorFragment.start();
					isIndoorExpanded = true;
					if (bBtnTakeActionVisible)
						takeAction.setVisibility(View.VISIBLE);
				}
			} else if (differenceY < 0) {
				if(isIndoorExpanded) {
					scaleDownIndoorFragment.start();
					scaleUpOutdoorFragment.start();
					isIndoorExpanded = false;
					takeAction.setVisibility(View.INVISIBLE); 
				}
			}
		}
		return false;
	}

	/**Dashboard outdoor info BEGIN */

	public void setUpdatedAtTime(String time) {
		if ( time != null ) {
			String[] date = time.split(" ");
			Log.i(TAG, time) ;
			if (tvUpdatedAtDate != null && date != null && date.length > 1 ) {
				//tvUpdatedAtDate.setText(date[0] + "\n" + date[1]);
				String[] dateNew = date[0].split("-");
				tvUpdatedAtDate.setText(dateNew[2] + "-" + dateNew[1] + "-" + dateNew[0] + "\n" + date[1]);
			}
		}
	}

	public void setOutdoorAQIvalue(int outdoorAQI) {
		this.outdoorAQIValue = outdoorAQI;
		tvOutdoorAQI.setText(String.valueOf(outdoorAQIValue));
		ivOutdoorCircle.setImageDrawable(setAQICircleBackground(outdoorAQIValue));
		if(rotateOutdoorCircle) {
			rotateAQICircle(outdoorAQIValue, ivOutdoorCircle);
			rotateOutdoorCircle = false;
		}
		//		rotateAQICircle(outdoorAQIValue, ivOutdoorCircle);
		setOutdoorAQIStatusAndComment(outdoorAQIValue);
	}

	private void rotateIndoorAQICircle(float aqi, ImageView iv) {
		if(getActivity() == null || aqi < 0) {
			return;
		}

		float rotation = aqi * (300.0f/10.0f);
		ViewHelper.setPivotX(iv, iv.getWidth()/(float)2);
		ViewHelper.setPivotY(iv, iv.getHeight()/(float)2 + rotationPivot());
		ObjectAnimator.ofFloat(iv, ANIM_ROTATION, 0, rotation).setDuration(2000).start();
	}

	private void rotateAQICircle(int aqi, ImageView iv) {
		/** Apply rotation transform and switch the background image of the AQI circle 
		 *	according to the AQI value
		 *	300 degrees is the arc of the gauge
		 *	500 is the max value of AQI
		 */

		if(getActivity() == null) {
			return;
		}

		float rotation = aqi * (300.0f/500.0f);
		ViewHelper.setPivotX(iv, iv.getWidth()/(float)2);
		ViewHelper.setPivotY(iv, iv.getHeight()/(float)2 + rotationPivot());
		ObjectAnimator.ofFloat(iv, ANIM_ROTATION, 0, rotation).setDuration(2000).start();
		dergreeRotatePointer = String.valueOf(rotation);
	}

	public Drawable setIndoorCircleBackground(float aqi) {
		if(getActivity() == null) {
			return null;
		}
		if(aqi <= 1.4f) {
			return getActivity().getResources().getDrawable(R.drawable.blue_circle_with_arrow_2x);
		} else if(aqi > 1.4f && aqi <= 2.3f) {
			return getActivity().getResources().getDrawable(R.drawable.light_pink_circle_arrow1_2x);
		} else if(aqi > 2.3f && aqi <= 3.5f) {
			return getActivity().getResources().getDrawable(R.drawable.red_circle_arrow_2x);
		} else if(aqi > 3.5f) {
			return getActivity().getResources().getDrawable(R.drawable.light_red_circle_arrow_2x);
		} 
		return null;
	}

	public Drawable setAQICircleBackground(int aqi) {
		if(getActivity() == null) {
			return null;
		}
		if(aqi >= 0 && aqi <= 50) {
			return getActivity().getResources().getDrawable(R.drawable.blue_circle_with_arrow_2x);
		} else if(aqi > 50 && aqi <= 100) {
			return getActivity().getResources().getDrawable(R.drawable.pink_circle_with_arrow_2x);
		} else if(aqi > 100 && aqi <= 150) {
			return getActivity().getResources().getDrawable(R.drawable.light_pink_circle_arrow_2x);
		} else if(aqi > 150 && aqi <= 200) {
			return getActivity().getResources().getDrawable(R.drawable.light_pink_circle_arrow1_2x);
		} else if(aqi > 200 && aqi <= 300) {
			return getActivity().getResources().getDrawable(R.drawable.red_circle_arrow_2x);
		} else if(aqi > 300 /*&& aqi <= 500*/) {
			return getActivity().getResources().getDrawable(R.drawable.light_red_circle_arrow_2x);
		}
		return null;
	}

	private void setOutdoorAQIStatusAndComment(int aqi) {
		if( getActivity() != null ) {
		String title = "";
		String message = "";
		if(aqi >= 0 && aqi <= 50) {
			title = getString(R.string.very_healthy);
			message = getString(R.string.very_healthy_msg_outdoor) + "\n";
		} else if(aqi > 50 && aqi <= 100) {
			title = getString(R.string.healthy);
			message = getString(R.string.healthy_msg_outdoor) + "\n";
		} else if(aqi > 100 && aqi <= 150) {
			title = getString(R.string.slightly_polluted);
			message = getString(R.string.slightly_polluted_msg_outdoor) + "\n";
		} else if(aqi > 150 && aqi <= 200) {
			title = getString(R.string.moderately_polluted);
			message = getString(R.string.moderately_polluted_msg_outdoor) + "\n";
		} else if(aqi > 200 && aqi <= 300) {
			title = getString(R.string.unhealthy);
			message = getString(R.string.unhealthy_msg_outdoor) + "\n";
		} else if(aqi > 300 && aqi <= 500) {
			title = getString(R.string.hazardous_msg_outdoor);
			message = getString(R.string.hazardous) + "\n";
		}
		tvOutdoorTitle.setText(title);
		tvOutdoorComment.setText(message);
		}
	}

	public void setOutdoorTemperature(int temperature) {
		if(getActivity() != null)
			tvOutdoorTemperature.setText(temperature+"\u2103");
	}

	private void setOutdoorTemperatureImage(String weatherDesc, String isDayTime) {
		if(getActivity() != null) {
		Drawable weatherImage = null;
		Log.i(TAG, "setOutdoorTemperatureImage " + weatherDesc);
		if(weatherDesc == null || weatherDesc.equals("") || getActivity() == null) {
			return;
		}

		if((weatherDesc.compareToIgnoreCase(SUNNY)) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.sunny_white);
		} else if ((weatherDesc.compareToIgnoreCase(MIST)) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.mist_white);
		} else if ((weatherDesc.compareToIgnoreCase(CLOUDY)) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.cloudy_white);
		} else if ((weatherDesc.compareToIgnoreCase(PARTLY_CLOUDY) /* && daytime*/) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.partly_cloudy_white);
		} else if ((weatherDesc.compareToIgnoreCase(PARTLY_CLOUDY) /* && nighttime*/) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.partly_cloudy_night_white);
		} else if ((weatherDesc.compareToIgnoreCase(CLEAR_SKIES)) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.clear_sky_night_white);
		} else if ((weatherDesc.compareToIgnoreCase(SNOW)) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.snow_white);
		} else if ((weatherDesc.compareToIgnoreCase(LIGHT_RAIN_SHOWER) == 0) || (weatherDesc.compareToIgnoreCase(LIGHT_DRIZZLE) == 0)) {
			weatherImage = getResources().getDrawable(R.drawable.light_rain_shower_white);
		} else if ((weatherDesc.compareToIgnoreCase(PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER)) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.light_rain_with_thunder_white);
		} else if ((weatherDesc.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_SHOWER) == 0) || (weatherDesc.compareToIgnoreCase(TORRENTIAL_RAIN_SHOWER) == 0) || (weatherDesc.compareToIgnoreCase(HEAVY_RAIN) == 0)) {
			weatherImage = getResources().getDrawable(R.drawable.heavy_rain_white);
		} else if ((weatherDesc.compareToIgnoreCase(HEAVY_RAIN_AT_TIMES)) == 0) {
			//TODO : Replace with proper icon. Icon not found, replacing with heavy raind
			weatherImage = getResources().getDrawable(R.drawable.heavy_rain_white);
		} else if ((weatherDesc.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER)) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.moderate_rain_with_thunder_white);
		} else if ((weatherDesc.compareToIgnoreCase(CLEAR)) == 0) {
			if(isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = getResources().getDrawable(R.drawable.sunny_white);
			else
				weatherImage = getResources().getDrawable(R.drawable.clear_sky_night_white);
		} else {
			weatherImage = getResources().getDrawable(R.drawable.light_rain_shower_white);
		}	

		ivOutdoorWeatherImage.setImageDrawable(weatherImage);
	}
	}

	public void setCityName(String city) {
		tvCity.setText(city);
	}

	public void setLocality(String locality) {
		tvLocality.setText(locality);
	}

	/** Dashboard outdoor info END */


	/** Dashboard indoor info BEGIN */

	public void setMode(String mode) {
		tvIndoorModeValue.setText(mode);
	}

	public void setFilterStatus(String status) {
		tvIndoorFilterStatus.setText(status);
	}

	public void setIndoorAQIValue(float indoorAQI) {
		//Show Take Action button only if AQI is Very Unhealthy
		if (indoorAQI > 3.5f && isIndoorExpanded)
		{
			bBtnTakeActionVisible = true;
			takeAction.setVisibility(View.VISIBLE);
		}
		else
		{
			bBtnTakeActionVisible = false;
			takeAction.setVisibility(View.INVISIBLE);
		}
		
		Log.i(TAG, "BEGIN :: rotateIndoorCircle  " + rotateIndoorCircle + " indoorAQI " + indoorAQI + " indoorAQIValue " + indoorAQIValue);
		tvIndoorAQI.setText(setIndoorPSenseText(indoorAQI));
		ivIndoorCircle.setImageDrawable(setIndoorCircleBackground(indoorAQI));
		if(rotateIndoorCircle || indoorAQIValue != indoorAQI) {
			//			rotateAQICircle(indoorAQI, ivIndoorCircle);
			rotateIndoorAQICircle(indoorAQI, ivIndoorCircle);
			rotateIndoorCircle = false;
		}
		setIndoorAQIStatusAndComment(indoorAQI);
		this.indoorAQIValue = indoorAQI;
		Log.i(TAG, "END ::::: rotateIndoorCircle  " + rotateIndoorCircle + " indoorAQI " + indoorAQI + " indoorAQIValue " + indoorAQIValue);
	}
	
	private String setIndoorPSenseText(float aqi) {
		if(aqi < 0) {
			return "";
		}
		if(aqi <= 1.4f) {
			return (getString(R.string.good)) ;
		} else if(aqi > 1.4f && aqi <= 2.3f) {
			return (getString(R.string.moderate)) ;
		} else if(aqi > 2.3f && aqi <= 3.5f) {
			return (getString(R.string.unhealthy)) ;
		} else if(aqi > 3.5f) {
			return (getString(R.string.very_unhealthy_split)) ;
		} 
		return "";
	}
	
	private void setIndoorAQIStatusAndComment(float indoorAQI) {
		if(indoorAQI < 0) {
			tvIndoorTitle.setText("") ;
			tvIndoorComment.setText("") ;
		} else if(indoorAQI >= 0 && indoorAQI <= 1.4f) {
			tvIndoorTitle.setText(getString(R.string.good)) ;
			tvIndoorComment.setText(getString(R.string.very_healthy_msg_indoor, purifierName)) ;
		} else if(indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			tvIndoorTitle.setText(getString(R.string.moderate)) ;
			tvIndoorComment.setText(getString(R.string.healthy_msg_indoor, purifierName)) ;
		} else if(indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			tvIndoorTitle.setText(getString(R.string.unhealthy)) ;
			tvIndoorComment.setText(getString(R.string.slightly_polluted_msg_indoor, purifierName)) ;
		} else if(indoorAQI > 3.5f) {
			tvIndoorTitle.setText(getString(R.string.very_unhealthy)) ;
			tvIndoorComment.setText(getString(R.string.moderately_polluted_msg_indoor, purifierName)) ;
		}
	}

	public void setHomeName(String name) {
		//TODO : Replace hard coded string.
		purifierName = name;
		if (getActivity() != null) {
			tvFilterHome.setText(name + getString(R.string.apos_s) + "\n" +  getString(R.string.room));
		}
	}
	
	/** Dashboard indoor info END */
	
	/**Unused gestures methods.*/
	@Override
	public void onLongPress(MotionEvent e) {}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {	return true; }

	@Override
	public void onShowPress(MotionEvent e) {}

	@Override
	public boolean onSingleTapUp(MotionEvent e) { return true; }

	@Override
	public void weatherDataUpdated(String weatherData) {
		if ( getActivity() != null &&
				weatherData != null ) {
			SessionDto.getInstance().setWeatherDetails(new DataParser(weatherData).parseWeatherData()) ;
			updateWeatherDetails() ;
		}

	}

	private void updateOutdoorAQIFields() {
		Log.i(TAG, "updateOutdoorAQIFields");
		OutdoorAQIEventDto outdoorDto = SessionDto.getInstance().getOutdoorEventDto() ;
		outdoorAQIEventDto = outdoorDto;
		if(outdoorDto == null) {
			Log.i(TAG, "outdoorDTO is null");
			return;
		}
		int idx [] = outdoorDto.getIdx()  ;
		if ( idx != null && idx.length > 0 ) {

			for ( int index = 0 ; index < idx.length ; index ++ ) {
				if( idx[index] != 0) {
					setOutdoorAQIvalue(idx[index]) ;
					break;
				}
			}
			rotateOutdoorCircle = true;
			outdoorUpdatedAt = outdoorDto.getT();
			setUpdatedAtTime(outdoorUpdatedAt) ;
			updateOutdoorDashboard = true;
			
			currentCityTime = outdoorUpdatedAt;
		}
	}

	private void updateWeatherDetails() {
		handler.sendEmptyMessage(2) ;
	}

	private void updateWeatherFields() {
		List<Weatherdto> weatherDto = SessionDto.getInstance().getWeatherDetails() ;
		weatherDtoList = weatherDto;
		if ( weatherDto != null && weatherDto.size() > 0 ) {
			int weatherInC = (int) weatherDto.get(0).getTempInCentigrade() ;
			isDayTime = weatherDto.get(0).getIsdaytime();
			outdoorTemperature = weatherInC;
			setOutdoorTemperature(outdoorTemperature) ;
			outdoorWeatherDesc = weatherDto.get(0).getWeatherDesc();
			setOutdoorTemperatureImage(outdoorWeatherDesc, isDayTime);
			updateOutdoorDashboard = true;
		}
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("msg: "+msg.what+":"+msg.arg1+":"+msg.arg2);
			if ( msg.what == 1 )
				updateOutdoorAQIFields();
			else if ( msg.what == 2 ) 
				updateWeatherFields() ;
			else if (msg.what == 3 ) {
				Toast.makeText(getActivity(), "Signon Successful", Toast.LENGTH_LONG).show() ;
			}
			else if (msg.what == 4 ) {
				Toast.makeText(getActivity(), "Signon Failed", Toast.LENGTH_LONG).show() ;
			}
		};
	};

	private void updateOutdoorAQI() {
		handler.sendEmptyMessage(1);
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		//Log.i(TAG, "respCode " + responseCode + " respData " + responseData);
		if (getActivity() != null) {
			if ( responseCode == 200 ) {
				new DataParser(responseData).parseOutdoorAQIData() ;
//				CityDetails city = new GsonBuilder().create().fromJson(responseData, CityDetails.class);
//				SessionDto.getInstance().setCityDetails(city) ;
				updateOutdoorAQI() ;
			}
		}
	}
	
	public static List<Weatherdto> getWeatherDetails() {
		return weatherDtoList;
	}
	
	public static OutdoorAQIEventDto getOutdoorAQIEventDto() {
		return outdoorAQIEventDto;
	}
	
	public static int getCurrentHour() {
		int currentHr = -1;
		if (currentCityTime != null && currentCityTime.length() > 0) {
			try {
				currentHr = Integer.parseInt(currentCityTime.substring(11, 13));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return currentHr;
	}
	
}
