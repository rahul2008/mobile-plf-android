package com.philips.cl.di.dev.pa.pureairui.fragments;


import static com.philips.cl.di.dev.pa.constants.AppConstants.CLEAR;
import static com.philips.cl.di.dev.pa.constants.AppConstants.CLEAR_SKIES;
import static com.philips.cl.di.dev.pa.constants.AppConstants.CLOUDY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.HEAVY_RAIN;
import static com.philips.cl.di.dev.pa.constants.AppConstants.HEAVY_RAIN_AT_TIMES;
import static com.philips.cl.di.dev.pa.constants.AppConstants.LIGHT_DRIZZLE;
import static com.philips.cl.di.dev.pa.constants.AppConstants.LIGHT_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MIST;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MODERATE_OR_HEAVY_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.PARTLY_CLOUDY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SNOW;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SUNNY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SWIPE_THRESHOLD;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SWIPE_VELOCITY_THRESHOLD;
import static com.philips.cl.di.dev.pa.constants.AppConstants.TORRENTIAL_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animAlpha;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animDuration;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animRotation;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animScaleX;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animScaleY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.indoorBackgroundTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.indoorCircleScaleDownTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.indoorTextScaleDownTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.indoorTextScaleUpTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.outdoorCircleScaleDownTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.outdoorTextScaleDownTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.outdoorTextScaleUpTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.rotationPivot;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.view.ActionMode;
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
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.CityDetails;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.listeners.AnimationListener;
import com.philips.cl.di.dev.pa.network.TaskGetHttp;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData.WeatherDataListener;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.screens.AirTutorialActivity;
import com.philips.cl.di.dev.pa.screens.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.screens.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.utils.DataParser;

public class HomeFragment extends Fragment implements OnClickListener, OnGestureListener, WeatherDataListener, ServerResponseListener {

	/** The Constant TAG. */
	public final static String TAG = HomeFragment.class.getSimpleName();

	/** The main view. */
	private View vMain;

	/** The is indoor expanded. */
	private boolean isIndoorExpanded = true;

	/** Dashboard ImageViews */
	private ImageView ivIndoorCircle, ivIndoorMeter, ivIndoorBackground, ivOutdoorCircle, ivOutdoorMeter, ivOutdoorWeatherImage;

	/** Dashboard TextViews */
	private TextView tvIndoorAQI, tvIndoorTitle, tvIndoorComment, tvIndoorModeTitle, tvFilterStatusTitle,
	tvIndoorModeValue, tvIndoorFilterStatus, tvFilterHome,
	tvOutdoorAQI, tvOutdoorTitle, tvOutdoorComment,
	tvUpdatedTitle, tvUpdatedAtDate, tvCity, tvLocality, tvOutdoorTemperature;

	private AnimatorSet scaleDownIndoorFragment, scaleUpIndoorFragment, 
	scaleDownOutdoorFragment, scaleUpOutdoorFragment, initAnimationLocations;

	private AnimationListener animationListener;

	/** Indoor and outdoor LinearLayouts which listen for gestures*/
	private LinearLayout llIndoor, llOutdoor;

	/** Indoor and outdoor layout height*/
	private int indoorHeight, outdoorHeight;

	/** Gesture listener to detect fling*/
	private GestureDetectorCompat gestureDetectorCompat;

	/** Dashboard values*/
	private int indoorAQIValue, indoorPSense, outdoorAQIValue, outdoorTemperature;
	private String outdoorUpdatedAt = "", outdoorWeatherDesc, isDayTime;
	private boolean rotateOutdoorCircle, rotateIndoorCircle, updateOutdoorDashboard;
	private String dergreeRotatePointer;

	private LinearLayout takeTourLayout;

	private RelativeLayout takeTourPopUp;
	
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
		if(SessionDto.getSessionDto() != null && SessionDto.getSessionDto().getCityDetails() != null)
			Log.i(TAG, "OutdoorLocations " + SessionDto.getSessionDto().getCityDetails().getCities());
		
		if(MainActivity.getAirPurifierEventDto() != null) {
			setIndoorDashBoardValues(indoorPSense);
		}
		if(updateOutdoorDashboard)
			setOutdoorDashboardValues(outdoorAQIValue);
		return vMain;
	}

	/** Update dashboard values on resume*/
	private void setIndoorDashBoardValues(int pSense) {
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
			rotateOutdoorCircle = !rotateOutdoorCircle;
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

		ivIndoorBackground = (ImageView) vMain.findViewById(R.id.iv_indoor_bg);

		tvFilterHome = (TextView) vMain.findViewById(R.id.tv_filter_home);
		tvIndoorModeTitle = (TextView) vMain.findViewById(R.id.tv_filter_mode_title);
		tvIndoorModeValue = (TextView) vMain.findViewById(R.id.tv_filter_mode_value);
		tvFilterStatusTitle = (TextView) vMain.findViewById(R.id.tv_filter_status_title);
		tvIndoorFilterStatus = (TextView) vMain.findViewById(R.id.tv_filter_status_value);
		tvIndoorAQI = (TextView) vMain.findViewById(R.id.indoor_aqi_reading);
		tvIndoorAQI.setTypeface(Fonts.getGillsansLight(getActivity()));
		tvIndoorTitle = (TextView) vMain.findViewById(R.id.tv_indoor_aqi_status_title);
		tvIndoorTitle.setOnClickListener(this);
		tvIndoorComment = (TextView) vMain.findViewById(R.id.tv_indoor_aqi_status_message);
		tvIndoorComment.setOnClickListener(this);
		ivOutdoorCircle = (ImageView) vMain.findViewById(R.id.outdoor_circle_pointer);
		ivOutdoorCircle.setOnClickListener(this);

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
		if(((MainActivity)getActivity()).getVisits()<=3){
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
				ObjectAnimator.ofFloat(llIndoor, animTranslationY, indoorBackgroundTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeTitle, animTranslationY, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeValue, animTranslationY, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterStatusTitle, animTranslationY, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterHome, animTranslationY, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorFilterStatus, animTranslationY, indoorTextScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animTranslationY, indoorCircleScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorAQI, animTranslationY, indoorCircleScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorAQI, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(tvIndoorAQI, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animTranslationY, indoorCircleScaleDownTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivIndoorMeter, animAlpha, 0),
				ObjectAnimator.ofFloat(tvIndoorTitle, animAlpha, 0),
				ObjectAnimator.ofFloat(tvIndoorComment, animAlpha, 0));

		scaleDownIndoorFragment.setDuration(animDuration);
		scaleDownIndoorFragment.addListener(animationListener);

		scaleUpIndoorFragment = new AnimatorSet();
		scaleUpIndoorFragment.playTogether(
				ObjectAnimator.ofFloat(llIndoor, animTranslationY, 0),
				ObjectAnimator.ofFloat(tvIndoorModeTitle, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeValue, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterStatusTitle, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterHome, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorFilterStatus, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleX, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleY, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animTranslationY, 0),
				ObjectAnimator.ofFloat(tvIndoorAQI, animTranslationY, 0),
				ObjectAnimator.ofFloat(tvIndoorAQI, animScaleX, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(tvIndoorAQI, animScaleY, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleX, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleY, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animTranslationY, 0),
				ObjectAnimator.ofFloat(ivIndoorMeter, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvIndoorTitle, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvIndoorComment, animAlpha, 0, 1));
		scaleUpIndoorFragment.setDuration(animDuration);
		scaleUpIndoorFragment.addListener(animationListener);

		scaleDownOutdoorFragment = new AnimatorSet();
		scaleDownOutdoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animTranslationY, outdoorCircleScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animTranslationY, outdoorCircleScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animTranslationY, outdoorCircleScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animAlpha, 0),
				ObjectAnimator.ofFloat(tvOutdoorTitle, animAlpha, 0),
				ObjectAnimator.ofFloat(tvOutdoorComment, animAlpha, 0),
				ObjectAnimator.ofFloat(tvUpdatedTitle, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvUpdatedAtDate, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvCity, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, animAlpha, 0),
				ObjectAnimator.ofFloat(tvOutdoorTemperature, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(ivOutdoorWeatherImage, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight)));
		scaleDownOutdoorFragment.setDuration(animDuration);

		scaleUpOutdoorFragment = new AnimatorSet();
		scaleUpOutdoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleX, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleY, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animTranslationY, outdoorCircleScaleDownTranslationY(outdoorHeight), 0),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animTranslationY, outdoorCircleScaleDownTranslationY(outdoorHeight), 0),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animScaleX, 0.6f, 1f),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animScaleY, 0.6f, 1f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleX, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleY, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animTranslationY, outdoorCircleScaleDownTranslationY(outdoorHeight), 0),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvOutdoorTitle, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvOutdoorComment, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvUpdatedTitle, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvUpdatedAtDate, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvCity, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvLocality, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(tvLocality, animAlpha, 1.0f),
				ObjectAnimator.ofFloat(tvOutdoorTemperature, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)),
				ObjectAnimator.ofFloat(ivOutdoorWeatherImage, animTranslationY, outdoorTextScaleDownTranslationY(outdoorHeight), outdoorTextScaleUpTranslationY(outdoorHeight)));
		scaleUpOutdoorFragment.setDuration(animDuration);

		//Only used once to align the outdoor fragment elements at the launch of the application.
		initAnimationLocations = new AnimatorSet();
		initAnimationLocations = scaleDownOutdoorFragment.clone();
		initAnimationLocations.playTogether(
				ObjectAnimator.ofFloat(tvIndoorModeTitle, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorModeValue, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterStatusTitle, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvFilterHome, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)),
				ObjectAnimator.ofFloat(tvIndoorFilterStatus, animTranslationY, indoorTextScaleUpTranslationY(indoorHeight)));
		initAnimationLocations.setDuration(0);
		initAnimationLocations.start();
	}

	/**
	 * Starts the Outdoor AQI task. This method calls a webservice and fetches
	 * the Outdoor AQI from the same
	 */
	private void startOutdoorAQITask() {
		if ( SessionDto.getInstance().getOutdoorEventDto() == null ) {
			TaskGetHttp shanghaiAQI = new TaskGetHttp(AppConstants.SHANGHAI_OUTDOOR_AQI_URL,getActivity(),this);
			shanghaiAQI.start() ;		
		}
		else {
			//updateOutdoorAQIFields() ;
		}
	}
	

	private void startWeatherDataTask() {
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
		Log.i(TAG, "onClick " + v.getId());
		Intent intent;
		switch (v.getId()) {
		case R.id.indoor_circle_pointer:
		case R.id.tv_indoor_aqi_status_message:
		case R.id.tv_indoor_aqi_status_title:
			//Show indoor details
			intent = new Intent(getActivity(),	IndoorDetailsActivity.class);
			String indoorInfos[] = new String[6];
			indoorInfos[0] = tvIndoorModeValue.getText().toString();
			indoorInfos[1] = tvIndoorFilterStatus.getText().toString();
			indoorInfos[2] = String.valueOf(indoorPSense);
			indoorInfos[3] = tvIndoorTitle.getText().toString();
			indoorInfos[4] = tvIndoorComment.getText().toString();
			indoorInfos[5] = tvOutdoorTitle.getText().toString();
			intent.putExtra("indoor", indoorInfos);
			startActivity(intent);
			break;
		case R.id.outdoor_circle_pointer:
			if (updateOutdoorDashboard) {
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
				startActivity(intent);
			}
			break;
		case R.id.lbl_take_tour:
			intent = new Intent(getActivity(), AirTutorialActivity.class);
			startActivity(intent);
			takeTourLayout.setVisibility(View.GONE);
			break;
		case R.id.btn_close_tour_layout:
			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
			takeTourLayout.startAnimation(bottomDown);
			takeTourLayout.setVisibility(View.GONE);
			showTutorialDialog();
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
		if(AnimationListener.ignoreGesture) {
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
				}
			} else if (differenceY < 0) {
				if(isIndoorExpanded) {
					scaleDownIndoorFragment.start();
					scaleUpOutdoorFragment.start();
					isIndoorExpanded = false;
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
				tvUpdatedAtDate.setText(date[0] + "\n" + date[1]);
			}
		}
	}

	public void setOutdoorAQIvalue(int outdoorAQI) {
		this.outdoorAQIValue = outdoorAQI;
		tvOutdoorAQI.setText(String.valueOf(outdoorAQIValue));
		ivOutdoorCircle.setImageDrawable(setAQICircleBackground(outdoorAQIValue));
		if(rotateOutdoorCircle) {
			rotateAQICircle(outdoorAQIValue, ivOutdoorCircle);
			rotateOutdoorCircle = !rotateOutdoorCircle;
		}
		//		rotateAQICircle(outdoorAQIValue, ivOutdoorCircle);
		setOutdoorAQIStatusAndComment(outdoorAQIValue);
	}

	private void rotateIndoorAQICircle(int pSense, ImageView iv) {
		if(getActivity() == null) {
			return;
		}

		float ratio = (float) (300.0/10.0);
		Log.i(TAG, "ratio " + ratio + " pivot " + (iv.getHeight()/2 + rotationPivot()));
		float rotation = pSense * (300.0f/10.0f);
		ViewHelper.setPivotX(iv, iv.getWidth()/2);
		ViewHelper.setPivotY(iv, iv.getHeight()/2 + rotationPivot());
		Log.i(TAG, "OutdoorCircleDimensions " + iv.getWidth() + " X " + (iv.getHeight()/2) + " roatation " + rotation);
		ObjectAnimator.ofFloat(iv, animRotation, 0, rotation).setDuration(2000).start();
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

		float ratio = (float) (300.0/500.0);
		Log.i(TAG, "ratio " + ratio + " pivot " + (iv.getHeight()/2 + rotationPivot()));
		float rotation = aqi * (300.0f/500.0f);
		ViewHelper.setPivotX(iv, iv.getWidth()/2);
		ViewHelper.setPivotY(iv, iv.getHeight()/2 + rotationPivot());
		Log.i(TAG, "OutdoorCircleDimensions " + iv.getWidth() + " X " + (iv.getHeight()/2) + " roatation " + rotation);
		ObjectAnimator.ofFloat(iv, animRotation, 0, rotation).setDuration(2000).start();
		dergreeRotatePointer = String.valueOf(rotation);
	}

	public Drawable setIndoorCircleBackground(int pSense) {
		if(getActivity() == null) {
			return null;
		}
		if(pSense >= 0 && pSense <= 1) {
			return getActivity().getResources().getDrawable(R.drawable.blue_circle_with_arrow_2x);
		} else if(pSense == 2) {
			return getActivity().getResources().getDrawable(R.drawable.light_pink_circle_arrow1_2x);
		} else if(pSense == 3) {
			return getActivity().getResources().getDrawable(R.drawable.red_circle_arrow_2x);
		} else if(pSense > 3 && pSense <= 10) {
			return getActivity().getResources().getDrawable(R.drawable.light_red_circle_arrow_2x);
		} 
		//		else if(pSense > 200 && pSense <= 300) {
		//			return getActivity().getResources().getDrawable(R.drawable.red_circle_arrow_2x);
		//		} else if(pSense > 300 /*&& aqi <= 500*/) {
		//			return getActivity().getResources().getDrawable(R.drawable.light_red_circle_arrow_2x);
		//		}
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

	public void setOutdoorTemperature(int temperature) {
		tvOutdoorTemperature.setText(temperature+"\u2103");
	}

	private void setOutdoorTemperatureImage(String weatherDesc, String isDayTime) {
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

	public void setIndoorAQIValue(int indoorAQI, int pSense) {
		
//		if(pSense >= 0 && pSense <= 1) {
//			tvIndoorAQI.setText(getString(R.string.good)) ;
//		} else if(pSense > 2 && pSense <= 3) {
//			tvIndoorAQI.setText(getString(R.string.moderate)) ;
//		} else if(pSense > 3 && pSense <= 4) {
//			tvIndoorAQI.setText(getString(R.string.unhealthy)) ;
//		} else if(pSense < 4) {
//			tvIndoorAQI.setText(getString(R.string.very_unhealthy)) ;
//		}
		tvIndoorAQI.setText(setIndoorPSenseText(pSense));
		ivIndoorCircle.setImageDrawable(setIndoorCircleBackground(pSense));
		if(rotateIndoorCircle || indoorPSense != pSense) {
			//			rotateAQICircle(indoorAQI, ivIndoorCircle);
			rotateIndoorAQICircle(pSense, ivIndoorCircle);
			rotateIndoorCircle = !rotateIndoorCircle;
		}
		setIndoorAQIStatusAndComment(pSense);
		this.indoorAQIValue = indoorAQI;
		this.indoorPSense = pSense;
	}
	
	private String setIndoorPSenseText(int pSense) {
		if(pSense >= 0 && pSense <= 1) {
			return (getString(R.string.good)) ;
		} else if(pSense > 2 && pSense <= 3) {
			return (getString(R.string.moderate)) ;
		} else if(pSense > 3 && pSense <= 4) {
			return (getString(R.string.unhealthy)) ;
		} else if(pSense < 4) {
			return (getString(R.string.very_unhealthy)) ;
		}
		return "";
	}
	
	private void setIndoorAQIStatusAndComment(int pSense) {
		if(pSense >= 0 && pSense <= 1) {
			tvIndoorTitle.setText(getString(R.string.good)) ;
			tvIndoorComment.setText(getString(R.string.very_healthy_msg_indoor)) ;
		} else if(pSense > 2 && pSense <= 3) {
			tvIndoorTitle.setText(getString(R.string.moderate)) ;
			tvIndoorComment.setText(getString(R.string.healthy_msg_indoor)) ;
		} else if(pSense > 3 && pSense <= 4) {
			tvIndoorTitle.setText(getString(R.string.unhealthy)) ;
			tvIndoorComment.setText(getString(R.string.slightly_polluted_msg_indoor)) ;
		} else if(pSense < 4) {
			tvIndoorTitle.setText(getString(R.string.very_unhealthy)) ;
			tvIndoorComment.setText(getString(R.string.moderately_polluted_msg_indoor)) ;
		} 
	}

	public void setHomeName(String name) {
		tvFilterHome.setText(name);
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
		}
	}

	private void updateWeatherDetails() {
		handler.sendEmptyMessage(2) ;
	}

	private void updateWeatherFields() {
		List<Weatherdto> weatherDto = SessionDto.getInstance().getWeatherDetails() ;
		if ( weatherDto != null && weatherDto.size() > 0 ) {
			int weatherInC = (int) weatherDto.get(0).getTempInCentigrade() ;
			isDayTime = (String) weatherDto.get(0).getIsdaytime();
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
		Log.i(TAG, "respCode " + responseCode + " respData " + responseData);
		if (getActivity() != null) {
			if ( responseCode == 200 ) {
				new DataParser(responseData).parseOutdoorAQIData() ;
//				CityDetails city = new GsonBuilder().create().fromJson(responseData, CityDetails.class);
//				SessionDto.getInstance().setCityDetails(city) ;
				updateOutdoorAQI() ;
			}
		}
	}

}
