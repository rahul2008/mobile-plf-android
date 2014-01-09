package com.philips.cl.di.dev.pa.pureairui.fragments;


import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animAlpha;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animDuration;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animRotation;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animScaleX;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animScaleY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.animTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.getCityInfoScaleUpTransformY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.getIndoorBGTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.getIndoorGaugeTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.getOutdoorCityInfoTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.getOutdoorGaugeTranslationY;
import static com.philips.cl.di.dev.pa.util.AnimatorConstants.rotationPivot;
import static com.philips.cl.di.dev.pa.util.AppConstants.SWIPE_THRESHOLD;
import static com.philips.cl.di.dev.pa.util.AppConstants.SWIPE_VELOCITY_THRESHOLD;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.listeners.AnimationListener;
import com.philips.cl.di.dev.pa.network.TaskGetHttp;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData.WeatherDataListener;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
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
	private TextView tvIndoorAQI, tvIndoorTitle, tvIndoorComment,
					 tvIndoorMode, tvIndoorFilterStatus, tvFilterHome,
					 tvOutdoorAQI, tvOutdoorTitle, tvOutdoorComment,
					 tvUpdatedTitle, tvUpdatedValue, tvCity, tvLocality, tvOutdoorTemperature;

	private AnimatorSet scaleDownIndoorFragment, scaleUpIndoorFragment, 
						scaleDownOutdoorFragment, scaleUpOutdoorFragment, initAnimationLocations;
	
	private AnimationListener animationListener;

	/** Indoor and outdoor LinearLayouts which listen for gestures*/
	private LinearLayout llIndoor, llOutdoor;

	/** Gesture listener to detect fling*/
	private GestureDetectorCompat gestureDetectorCompat;
	
	/** Dashboard values*/
	private int indoorAQIValue, outdoorAQIValue, outdoorTemperature;
	private String outdoorUpdatedAt = "";
	private boolean rotateOutdoorCircle, updateOutdoorDashboard;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		vMain = inflater.inflate(R.layout.rl_home_master_fragment, container, false);
		((ViewGroup) vMain).setClipChildren(false);

		gestureDetectorCompat = new GestureDetectorCompat(getActivity(), this);
		animationListener = new AnimationListener();
		initViews();
		initAnimations();
		startOutdoorAQITask() ;
		startWeatherDataTask() ;
		
		isIndoorExpanded = true;
		rotateOutdoorCircle = true;
		
		if(MainActivity.getAirPurifierEventDto() != null) {
			setIndoorDashBoardValues(indoorAQIValue);
		}
		if(updateOutdoorDashboard)
			setOutdoorDashboardValues(outdoorAQIValue);
		return vMain;
	}

	/** Update dashboard values on resume*/
	private void setIndoorDashBoardValues(int indoorAQI) {
		tvIndoorAQI.setText(String.valueOf(indoorAQI));
		setIndoorAQIStatusAndComment(indoorAQI);
		ivIndoorCircle.setImageDrawable(setAQICircleBackground(indoorAQI));
		setMode((MainActivity.getAirPurifierEventDto().getFanSpeed().equals(AppConstants.FAN_SPEED_AUTO) ? "Auto" : "Manual"));
		setFilterStatus(Utils.getFilterStatusForDashboard(MainActivity.getAirPurifierEventDto()));
	}

	/** Update dashboard values on resume*/
	private void setOutdoorDashboardValues(int outdoorAQI) {
		tvOutdoorAQI.setText(String.valueOf(outdoorAQI));
		setOutdoorAQIStatusAndComment(outdoorAQI);
		ivOutdoorCircle.setImageDrawable(setAQICircleBackground(outdoorAQI));
		setUpdatedAtTime(outdoorUpdatedAt);
		setOutdoorTemperature(outdoorTemperature);
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

		ivIndoorCircle = (ImageView) vMain.findViewById(R.id.indoor_circle_pointer);
		ivIndoorCircle.setOnClickListener(this);

		ivIndoorMeter = (ImageView) vMain.findViewById(R.id.indoor_circle_meter);

		ivIndoorBackground = (ImageView) vMain.findViewById(R.id.iv_indoor_bg);

		tvFilterHome = (TextView) vMain.findViewById(R.id.tv_filter_home);
		tvIndoorMode = (TextView) vMain.findViewById(R.id.tv_filter_mode_value);
		tvIndoorFilterStatus = (TextView) vMain.findViewById(R.id.tv_filter_status_value);
		tvIndoorAQI = (TextView) vMain.findViewById(R.id.indoor_aqi_reading);
		tvIndoorTitle = (TextView) vMain.findViewById(R.id.tv_indoor_aqi_status_title);
		tvIndoorComment = (TextView) vMain.findViewById(R.id.tv_indoor_aqi_status_message);

		ivOutdoorCircle = (ImageView) vMain.findViewById(R.id.outdoor_circle_pointer);
		ivOutdoorCircle.setOnClickListener(this);

		ivOutdoorMeter = (ImageView) vMain.findViewById(R.id.outdoor_circle_meter);

		tvOutdoorAQI = (TextView) vMain.findViewById(R.id.outdoor_aqi_reading);
		tvOutdoorTitle = (TextView) vMain.findViewById(R.id.tv_outdoor_aqi_status_title);
		tvOutdoorComment = (TextView) vMain.findViewById(R.id.tv_outdoor_aqi_status_message);
		tvUpdatedTitle = (TextView) vMain.findViewById(R.id.tv_updated_title);
		tvUpdatedValue = (TextView) vMain.findViewById(R.id.tv_updated_value);
		tvCity = (TextView) vMain.findViewById(R.id.tv_location_city);
		tvLocality = (TextView) vMain.findViewById(R.id.tv_location);
		ivOutdoorWeatherImage = (ImageView) vMain.findViewById(R.id.iv_outdoor_weather_image);
		tvOutdoorTemperature = (TextView) vMain.findViewById(R.id.tv_outdoor_weather_value);

	}

	private void initAnimations() {
		scaleDownIndoorFragment = new AnimatorSet();
		scaleDownIndoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animTranslationY, getIndoorGaugeTranslationY()),
				ObjectAnimator.ofFloat(ivIndoorBackground, animTranslationY, getIndoorBGTranslationY()),
				ObjectAnimator.ofFloat(tvIndoorAQI, animTranslationY, getIndoorGaugeTranslationY()),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animTranslationY, getIndoorGaugeTranslationY()),
				ObjectAnimator.ofFloat(ivIndoorMeter, animAlpha, 0),
				ObjectAnimator.ofFloat(tvIndoorTitle, animTranslationY, getIndoorGaugeTranslationY() - 250),
				ObjectAnimator.ofFloat(tvIndoorTitle, animAlpha, 0),
				ObjectAnimator.ofFloat(tvIndoorComment, animTranslationY, getIndoorGaugeTranslationY() - 250),
				ObjectAnimator.ofFloat(tvIndoorComment, animAlpha, 0));
		scaleDownIndoorFragment.setDuration(animDuration);
		scaleDownIndoorFragment.addListener(animationListener);

		scaleUpIndoorFragment = new AnimatorSet();
		scaleUpIndoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleX, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animScaleY, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorCircle, animTranslationY, 0),
				ObjectAnimator.ofFloat(ivIndoorBackground, animTranslationY, 0),
				ObjectAnimator.ofFloat(tvIndoorAQI, animTranslationY, 0),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleX, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animScaleY, 1.0f),
				ObjectAnimator.ofFloat(ivIndoorMeter, animTranslationY, 0),
				ObjectAnimator.ofFloat(ivIndoorMeter, animAlpha, 1),
				ObjectAnimator.ofFloat(tvIndoorTitle, animAlpha, 1),
				ObjectAnimator.ofFloat(tvIndoorTitle, animTranslationY, 0),
				ObjectAnimator.ofFloat(tvIndoorComment, animAlpha, 1),
				ObjectAnimator.ofFloat(tvIndoorComment, animTranslationY, 0));
		scaleUpIndoorFragment.setDuration(animDuration);
		scaleUpIndoorFragment.addListener(animationListener);

		scaleDownOutdoorFragment = new AnimatorSet();
		scaleDownOutdoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animTranslationY, getOutdoorGaugeTranslationY()),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animTranslationY, getOutdoorGaugeTranslationY()),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleX, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleY, 0.6f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animTranslationY, getOutdoorGaugeTranslationY()),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animAlpha, 0),
				ObjectAnimator.ofFloat(tvOutdoorTitle, animAlpha, 0),
				ObjectAnimator.ofFloat(tvOutdoorComment, animAlpha, 0),
				ObjectAnimator.ofFloat(tvUpdatedTitle, animTranslationY, getCityInfoScaleUpTransformY()),
				ObjectAnimator.ofFloat(tvUpdatedValue, animTranslationY, getCityInfoScaleUpTransformY()),
				ObjectAnimator.ofFloat(tvCity, animTranslationY, getCityInfoScaleUpTransformY()),
				ObjectAnimator.ofFloat(tvLocality, animTranslationY, getCityInfoScaleUpTransformY()),
				ObjectAnimator.ofFloat(tvLocality, animAlpha, 0),
				ObjectAnimator.ofFloat(tvOutdoorTemperature, animTranslationY, getCityInfoScaleUpTransformY()),
				ObjectAnimator.ofFloat(ivOutdoorWeatherImage, animTranslationY, getCityInfoScaleUpTransformY()));
		scaleDownOutdoorFragment.setDuration(animDuration);

		scaleUpOutdoorFragment = new AnimatorSet();
		scaleUpOutdoorFragment.playTogether(
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleX, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animScaleY, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorCircle, animTranslationY, getOutdoorGaugeTranslationY(), 0),
				ObjectAnimator.ofFloat(tvOutdoorAQI, animTranslationY, getOutdoorGaugeTranslationY(), 0),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleX, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animScaleY, 0.6f, 1.0f),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animTranslationY, getOutdoorGaugeTranslationY(), 0),
				ObjectAnimator.ofFloat(ivOutdoorMeter, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvOutdoorTitle, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvOutdoorComment, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvUpdatedTitle, animTranslationY, getCityInfoScaleUpTransformY(), getOutdoorCityInfoTranslationY()),
				ObjectAnimator.ofFloat(tvUpdatedValue, animTranslationY, getCityInfoScaleUpTransformY(), getOutdoorCityInfoTranslationY()),
				ObjectAnimator.ofFloat(tvCity, animTranslationY, getCityInfoScaleUpTransformY(), getOutdoorCityInfoTranslationY()),
				ObjectAnimator.ofFloat(tvLocality, animAlpha, 0, 1),
				ObjectAnimator.ofFloat(tvLocality, animTranslationY, getCityInfoScaleUpTransformY(), getOutdoorCityInfoTranslationY()),
				ObjectAnimator.ofFloat(tvLocality, animAlpha, 1.0f),
				ObjectAnimator.ofFloat(tvOutdoorTemperature, animTranslationY, getCityInfoScaleUpTransformY(), getOutdoorCityInfoTranslationY()),
				ObjectAnimator.ofFloat(ivOutdoorWeatherImage, animTranslationY, getCityInfoScaleUpTransformY(), getOutdoorCityInfoTranslationY()));
		scaleUpOutdoorFragment.setDuration(animDuration);

		//Only used once to align the outdoor fragment elements at the launch of the application.
		initAnimationLocations = new AnimatorSet();
		initAnimationLocations = scaleDownOutdoorFragment.clone();
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
		switch (v.getId()) {
		case R.id.rl_indoor_circle_container:
			//Show indoor details
			break;
		case R.id.rl_outdoor_circle_container:
			//Show outdoor details
			break;

		}
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
		Log.i(TAG, "Processing onFling isIndoorExpanded " + isIndoorExpanded);
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
		tvUpdatedValue.setText(time);
	}

	public void setOutdoorAQIvalue(int outdoorAQI) {
		this.outdoorAQIValue = outdoorAQI;
		tvOutdoorAQI.setText(String.valueOf(outdoorAQIValue));
		rotateAQICircle(outdoorAQIValue, ivOutdoorCircle);
		setOutdoorAQIStatusAndComment(outdoorAQIValue);
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
		
		iv.setImageDrawable(setAQICircleBackground(aqi));

		float ratio = (float) (300.0/500.0);
		Log.i(TAG, "ratio " + ratio + " pivot " + (iv.getHeight()/2 + rotationPivot()));
		float roatation = aqi * (300.0f/500.0f);
		ViewHelper.setPivotX(iv, iv.getWidth()/2);
		ViewHelper.setPivotY(iv, iv.getHeight()/2 + rotationPivot());
		Log.i(TAG, "OutdoorCircleDimensions " + iv.getWidth() + " X " + (iv.getHeight()/2) + " roatation " + roatation);
		ObjectAnimator.ofFloat(iv, animRotation, 0, roatation).setDuration(2000).start();
	}
	
	private Drawable setAQICircleBackground(int aqi) {
		if(aqi >= 0 && aqi <= 50) {
			return getActivity().getResources().getDrawable(R.drawable.blue_circle_with_arrow_2x);
		} else if(aqi > 50 && aqi <= 100) {
			return getActivity().getResources().getDrawable(R.drawable.pink_circle_with_arrow_2x);
		} else if(aqi > 100 && aqi <= 150) {
			return getActivity().getResources().getDrawable(R.drawable.light_pink_circle_arrow_2x);
		} else if(aqi > 150 && aqi <= 200) {
			return getActivity().getResources().getDrawable(R.drawable.light_pink_circle_arrow1_2x);
		} else if(aqi > 200 && aqi <= 300) {
			return getActivity().getResources().getDrawable(R.drawable.light_red_circle_arrow_2x);
		} else if(aqi > 300 && aqi <= 500) {
			return getActivity().getResources().getDrawable(R.drawable.red_circle_arrow_2x);
		}
		return null;
	}

	private void setOutdoorAQIStatusAndComment(int aqi) {
		String title = "";
		String message = "";
		if(aqi >= 0 && aqi <= 50) {
			
		} else if(aqi > 50 && aqi <= 100) {
			
		} else if(aqi > 100 && aqi <= 150) {
			
		} else if(aqi > 150 && aqi <= 200) {
			
		} else if(aqi > 200 && aqi <= 300) {
			
		} else if(aqi > 300 && aqi <= 500) {
			
		}
		tvOutdoorTitle.setText(title);
		tvOutdoorComment.setText(message);
	}

	public void setOutdoorTemperature(int temperature) {
		tvOutdoorTemperature.setText(temperature+"\u2103");
		setOutdoorTemperatureImage(temperature);
	}

	private void setOutdoorTemperatureImage(int temperature) {
		//Set appropriate image depending on temperature range
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
		tvIndoorMode.setText(mode);
	}

	public void setFilterStatus(String status) {
		tvIndoorFilterStatus.setText(status);
	}

	public void setIndoorAQIValue(int indoorAQI) {
		this.indoorAQIValue = indoorAQI;
		tvIndoorAQI.setText(String.valueOf(indoorAQIValue));
		rotateAQICircle(indoorAQIValue, ivIndoorCircle);
		setIndoorAQIStatusAndComment(indoorAQIValue);
	}

	private void setIndoorAQIStatusAndComment(int aqi) {
		if(aqi >= 0 && aqi <= 50) {
			tvIndoorTitle.setText(getString(R.string.very_healthy)) ;
			tvIndoorComment.setText(getString(R.string.very_healthy_message)) ;
		} else if(aqi > 50 && aqi <= 100) {
			tvIndoorTitle.setText(getString(R.string.healthy)) ;
			tvIndoorComment.setText(getString(R.string.healthy_message)) ;
		} else if(aqi > 100 && aqi <= 150) {
			tvIndoorTitle.setText(getString(R.string.slightly_polluted)) ;
			tvIndoorComment.setText(getString(R.string.slightly_polluted_message)) ;
		} else if(aqi > 150 && aqi <= 200) {
			tvIndoorTitle.setText(getString(R.string.moderately_polluted)) ;
			tvIndoorComment.setText(getString(R.string.moderately_polluted_message)) ;
		} else if(aqi > 200 && aqi <= 300) {
			tvIndoorTitle.setText(getString(R.string.unhealthy)) ;
			tvIndoorComment.setText(getString(R.string.unhealthy_message)) ;
		} else if(aqi > 300 && aqi <= 1000) {
			tvIndoorTitle.setText(getString(R.string.hazardous)) ;
			tvIndoorComment.setText(getString(R.string.hazardous_message)) ;
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
		if ( weatherData != null ) {
			SessionDto.getInstance().setWeatherDetails(new DataParser(weatherData).parseWeatherData()) ;
			updateWeatherDetails() ;
		}

	}

	private void updateOutdoorAQIFields() {
		OutdoorAQIEventDto outdoorDto = SessionDto.getInstance().getOutdoorEventDto() ;
		int idx [] = outdoorDto.getIdx()  ;
		if ( idx != null && idx.length > 0 ) {

			for ( int index = 0 ; index < idx.length ; index ++ ) {
				if( idx[index] != 0) {
					setOutdoorAQIvalue(idx[index]) ;
					break;
				}
			}
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
			outdoorTemperature = weatherInC;
			setOutdoorTemperature(outdoorTemperature) ;
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
		if ( responseCode == 200 ) {
			new DataParser(responseData).parseOutdoorAQIData() ;

			updateOutdoorAQI() ;
		}

	}

}
