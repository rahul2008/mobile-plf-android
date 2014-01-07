package com.philips.cl.di.dev.pa.screens.fragments;

import java.util.List;

import android.app.Activity;
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
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.DemoAppConfigurationParametersForProvisioned;
import com.philips.cl.di.dev.pa.controller.ICPCallbackHandler;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.interfaces.ICPEventListener;
import com.philips.cl.di.dev.pa.interfaces.OutdoorAQIListener;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.network.TaskGetHttp;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData;
import com.philips.cl.di.dev.pa.network.TaskGetWeatherData.WeatherDataListener;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.Utils;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.data.Errors;

/**
 * The Class HomeFragment.
 */
public class HomeFragment extends Fragment implements OnClickListener,
		OnGestureListener, SensorEventListener, OutdoorAQIListener, WeatherDataListener, ServerResponseListener, ICPEventListener {

	

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

	private CustomTextView tvOutdoorDay, tvOutdoorTime;

	private TextView tvIndoorAQI, tvOutdoorAQI;

	private TextView outdoorAQI;

	/** The params outdoor. */
	FrameLayout.LayoutParams paramsIndoor, paramsOutdoor;

	/** The main view. */
	View vMain;

	private ImageView ivIndoorQuad1, ivIndoorQuad2, ivIndoorQuad3,
			ivIndoorQuad4, ivOutdoorQuad1, ivOutdoorQuad2, ivOutdoorQuad3,
			ivOutdoorQuad4;

	private ImageView ivLeftMenu, ivCenterLabel, ivRightDeviceIcon, ivMap,
			ivTrend, ivFanIndicator;

	OnIndoorRingClick mCallback;	

	/** The sensor data controller. */
	private SensorDataController sensorDataController;

	MapButtonClick mMapButtonCallback;
	
	SignOn signon ;

	// Container Activity must implement this interface
	public interface OnIndoorRingClick {
		public void onRingClicked(int aqi, String sCityName,
				String sLastUpdatedTime, String sLastUpdatedDay,
				boolean isIndoor);
	}

	public interface MapButtonClick {

		void onMapClick(String sCityName, String sAQIValue);

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
		sensorDataController = new SensorDataController(this,getActivity());
		startOutdoorAQITask() ;
		/** Called when the activity is first created. */
		
		return vMain;
	}
	
	private void signon() {	        	
				signon = SignOn.getInstance();
				signon.setIsFirstTime(true) ;
				int res = signon.executeCommand() ;
				
				if ( res == Errors.SUCCESS ) {
					Log.e(TAG, "Signon Success") ;
					
					//Toast.makeText(getActivity(), "Sigon Success", Toast.LENGTH_LONG).show() ;
				}
				else {
					Log.i(TAG, "Signon Failed: ") ;
					Toast.makeText(getActivity(), "Sigon Failure", Toast.LENGTH_LONG).show() ;
				}        
	}
	
	private void onSignon() {
		ICPCallbackHandler callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this) ;
		DemoAppConfigurationParametersForProvisioned configParams = new DemoAppConfigurationParametersForProvisioned();
		SignOn.create(callbackHandler, configParams) ;
		signon() ;
	}

	/**
	 * Starts the Outdoor AQI task. This method calls a webservice and fetches
	 * the Outdoor AQI from the same
	 */
	private void startOutdoorAQITask() {
		TaskGetHttp shanghaiAQI = new TaskGetHttp(AppConstants.SHANGHAI_OUTDOOR_AQI_URL,getActivity(),this);
		shanghaiAQI.start() ;
		
		TaskGetWeatherData statusUpdateTask = new TaskGetWeatherData(String.format(AppConstants.WEATHER_SERVICE_URL,"31.2000,121.5000"),this);
		statusUpdateTask.start();
	}

	@Override
	public void onResume() {
		Log.i(TAG, "onResume") ;
		super.onResume();
		sensorDataController.startPolling();
		//sensorDataController.startCPPPolling() ;
		if (isIndoorExpanded)
			startAnimationsIndoor();
		else
			startAnimationsOutdoor();
	}

	/**
	 * Updates the outdoor AQI index
	 */
	private void updateOutdoorAQIFields() {
		OutdoorAQIEventDto outdoorDto = SessionDto.getInstance().getOutdoorEventDto() ;
		int idx [] = outdoorDto.getIdx()  ;
		if ( idx != null && idx.length > 0 ) {
			
			for ( int index = 0 ; index < idx.length ; index ++ ) {
				if( idx[index] != 0) {
					outdoorAQI.setText(""+idx[index]) ;
					break;
				}
			} 
			
			tvOutdoorTime.setText(outdoorDto.getT()) ;
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
				//Log.i(TAG, "Indoor height :" + iOutdoorCompressedHeight);
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

		outdoorAQI = (TextView) vMain.findViewById(R.id.tvOutdoorAQI);

		ivIndoorQuad1 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad1);
		ivIndoorQuad2 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad2);
		ivIndoorQuad3 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad3);
		ivIndoorQuad4 = (ImageView) vMain.findViewById(R.id.ivIndoorQuad4);

		ivOutdoorQuad1 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad1);
		ivOutdoorQuad2 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad2);
		ivOutdoorQuad3 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad3);
		ivOutdoorQuad4 = (ImageView) vMain.findViewById(R.id.ivOutdoorQuad4);

		tvDay = (CustomTextView) vMain.findViewById(R.id.tvDay);

		tvOutdoorDay = (CustomTextView) vMain.findViewById(R.id.tvDayOutdoor);
		tvOutdoorTime = (CustomTextView) vMain.findViewById(R.id.tvTimeOutdoor);

		tvTime = (CustomTextView) vMain.findViewById(R.id.tvTime);

		ivMap = (ImageView) vMain.findViewById(R.id.ivMap);
		ivMap.setOnClickListener(this);
		ivTrend = (ImageView) vMain.findViewById(R.id.ivTrend);
		ivTrend.setOnClickListener(this);

		ivFanIndicator = (ImageView) vMain.findViewById(R.id.ivIndicator);
	}

	/**
	 * Initialize animations.
	 */
	private void initialiseAnimations() {
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
			String sLastUpdatedDayHome = tvDay.getText().toString();
			String sLastUpdatedTimeHome = tvTime.getText().toString();
			int iAQI = Integer.parseInt(tvIndoorAQI.getText().toString());
			mCallback.onRingClicked(iAQI, "Home", sLastUpdatedTimeHome,
					sLastUpdatedDayHome, true);
			break;

		case R.id.flOutdoorRing:
			Log.i(TAG, "On Outdoor Ring click!!!");
			int iOutdoorAQI = Integer.parseInt(tvOutdoorAQI.getText()
					.toString());
			String sCityName = tvCityName.getText().toString();
			String sLastUpdatedDay = tvDayOutdoor.getText().toString();
			String sLastUpdatedTime = tvTimeOutdoor.getText().toString();
			mCallback.onRingClicked(iOutdoorAQI, sCityName, sLastUpdatedTime,
					sLastUpdatedDay, false);
			break;

		case R.id.ivTrend:
			onSignon() ;
			break;

		case R.id.ivMap:
			Log.i(TAG, "On Map click!!!");

			mMapButtonCallback.onMapClick(tvCityName.getText().toString(),
					tvOutdoorAQI.getText().toString());

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
		return true;
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
		Log.i(TAG, "Sensor Data Received") ;
		updateUI(airPurifierEventDto);
	}

	private void updateUI(AirPurifierEventDto airPurifierEventDto) {
		if (airPurifierEventDto != null) {
			int iIndoorAQI = airPurifierEventDto.getIndoorAQI();
			updateIndoorInfo(iIndoorAQI);

		}
	}

	private void updateIndoorInfo(int indoorAQI) {
		String currentDateTime = Utils.getCurrentDateTime();
		tvIndoorAQI.setText(String.valueOf(indoorAQI));
		tvDay.setText(currentDateTime.substring(0, 10));
		tvTime.setText(currentDateTime.substring(11, 16));
	}
	
	@Override
	public void onPause() {
		//sensorDataController.stopPolling() ;
		//sensorDataController.stopCPPPolling() ;
		
		super.onPause();
	}

	private void startAnimationsIndoor() {}

	private void startAnimationsOutdoor() {}

	private void stopAnimationsIndoor() {}

	@Override
	public void onDestroy() {
		Log.i(TAG, "OnDestroy");
		super.onDestroy();
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
			mMapButtonCallback = (MapButtonClick) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	/**
	 * This runnable will be called by the handler after every one hour to get
	 * the latest outdoor AQI
	 */
	private final Runnable getOutdoorAQIRunnable = new Runnable() {
		@Override
		public void run() {
			startOutdoorAQITask();
		}
	};

	/**
	 * This will start a timer for 1 hour to fetch the latest data
	 */
	private void startOutdoorAQITimer() {
		outdoorAQIHandler.postDelayed(getOutdoorAQIRunnable,
				AppConstants.OUTDOOR_AQI_UPDATE_DURATION);
	}

	private Handler outdoorAQIHandler = new Handler();
	/**
	 * Handler to update the User Interface
	 */

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
	
	private void updateWeatherFields() {
		List<Weatherdto> weatherDto = SessionDto.getInstance().getWeatherDetails() ;
		if ( weatherDto != null && weatherDto.size() > 0 ) {
			int weatherInC = (int) weatherDto.get(0).getTempInCentigrade() ;
			tvOutdoorDay.setText(weatherInC+"\u2103") ;
		}
	}

	/**
	 * Callback from outdoorAQI class
	 */
	@Override
	public void updateOutdoorAQI() {
		handler.sendEmptyMessage(1);
	}
	
	private void updateWeatherDetails() {
		handler.sendEmptyMessage(2) ;
	}

	@Override
	public void weatherDataUpdated(String weatherData) {
		if ( weatherData != null ) {
			SessionDto.getInstance().setWeatherDetails(new DataParser(weatherData).parseWeatherData()) ;
			updateWeatherDetails() ;
		}
		
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		// TODO Auto-generated method stub
		if ( responseCode == 200 ) {
			new DataParser(responseData).parseOutdoorAQIData() ;
			
			updateOutdoorAQI() ;
		}
		
	}

	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		if(status == 0) {        	
        	handler.sendEmptyMessage(3) ;
		}
		else {
			handler.sendEmptyMessage(4) ;
		}
	}

}
