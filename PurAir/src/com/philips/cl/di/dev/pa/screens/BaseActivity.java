package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.widget.Button;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.AirPurifierController;
import com.philips.cl.di.dev.pa.controller.AirPurifierController.DeviceMode;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.interfaces.SizeCallback;
import com.philips.cl.di.dev.pa.screens.adapters.MenuListAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.CustomHorizontalScrollView;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
public abstract class BaseActivity extends Activity implements
		OnItemClickListener, OnCheckedChangeListener, AirPurifierEventListener, OnClickListener {
	/** The scroll view. */
	protected CustomHorizontalScrollView scrollView;

	/** The left menu , center view , right settings. */
	protected View leftMenu, centerView, rightSettings;

	/**
	 * Menu must NOT be out/shown to start with.
	 */
	static boolean menuOut = false;

	/** The settings out. */
	static boolean settingsOut = false;

	/** The inflater. */
	protected LayoutInflater inflater;

	/** The screen width. */
	private static int screenwidth;

	/** The Constant TAG. */
	private static final String TAG = BaseActivity.class.getName();

	/** The power. */
	private Switch swPower;

	/** The air purifier controller. */
	private AirPurifierController airpurifierController;
	
	private ImageButton imageButtonOne ;
	private ImageButton imageButtonSpeedTwo ;
	private ImageButton imageButtonSpeedThree ;
	
	private Button buttonSilent ;
	private Button buttonTurbo ;
	private Button buttonAuto ;
	
	private ImageView backButton ;
	
	private TextView airQualityStatusView;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "--BaseActivity.onCreate--");
		airpurifierController = new AirPurifierController(this,
				this,AppConstants.GET_SENSOR_DATA_REQUEST_TYPE);
	//	airpurifierController.startPolling() ;
		inflater = LayoutInflater.from(this);
		scrollView = (CustomHorizontalScrollView) inflater.inflate(
				R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		screenwidth = Utils.getScreenWidth(this);
		initializeLeftAndRightNavigationalViews();
		centerView = getCenterView();
		populateScrollView();
		initializeControls();

	}

	/**
	 * Initialize left and right navigational views.
	 */
	void initializeLeftAndRightNavigationalViews() {
		// Left Menu - Common for all activities
		leftMenu = inflater.inflate(R.layout.left_menu, null);
		ListView lvMenu = (ListView) leftMenu.findViewById(R.id.lvMenu);
		lvMenu.setOnItemClickListener(this);
		Utils.getIconArray();
		Utils.getLabelArray();
		lvMenu.setAdapter(new MenuListAdapter(getApplicationContext(), 0, Utils
				.getIconArray(), Utils.getLabelArray()));
		// Settings Page - Common
		rightSettings = inflater.inflate(R.layout.activity_settings, null);

	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		
		switch (position) {
		case AppConstants.HOME:
			Log.i(TAG, "On Item Click --- HOME ");
			int left = leftMenu.getMeasuredWidth();
			scrollView.smoothScrollTo(left, 0);
			menuOut = !menuOut;
			break;
		case AppConstants.MYCITIES:
			Log.i(TAG, "On Item Click --- CITY");
			break;
		case AppConstants.ABOUTAQI:
			Log.i(TAG, "On Item Click --- ABout AQI ");
			break;
		case AppConstants.PRODUCTREG:
			Log.i(TAG, "On Item Click --- Product Reg ");
			break;
		case AppConstants.HELP:
			Log.i(TAG, "On Item Click --- HELP");
			break;
		case AppConstants.SETTINGS:
			Log.i(TAG, "On Item Click --- Settings");
			showIPDialog(this);
			break;

		}

	}
	
	
	/**
	 * Shows ip dialog.
	 *
	 * @param context the context
	 */
	public void showIPDialog(Context context)
	{
		Utils.showIpDialog(context);
	}

	/**
	 * Initialize controls.
	 */
	void initializeControls() {
		swPower = (Switch) findViewById(R.id.sw_power);
		swPower.setOnCheckedChangeListener(this);
		
		imageButtonOne = (ImageButton) findViewById(R.id.ib_fanspeed_one) ;
		imageButtonOne.setOnClickListener(this) ;
		
		imageButtonSpeedTwo = (ImageButton) findViewById(R.id.ib_fanspeed_two) ;
		imageButtonSpeedTwo.setOnClickListener(this) ;
		
		imageButtonSpeedThree = (ImageButton) findViewById(R.id.ib_fanspeed_three) ;
		imageButtonSpeedThree.setOnClickListener(this) ;
		
		buttonSilent = (Button) findViewById(R.id.btn_fanspeed_silent) ;
		buttonSilent.setOnClickListener(this) ;
		
		buttonTurbo = (Button) findViewById(R.id.btn_fanspeed_turbo) ;
		buttonTurbo.setOnClickListener(this) ;
		
		buttonAuto = (Button) findViewById(R.id.btn_fanspeed_auto) ;
		buttonAuto.setOnClickListener(this) ;
		
		backButton = (ImageView) findViewById(R.id.iv_back) ;
		backButton.setOnClickListener(this) ;
		
		airQualityStatusView = (TextView) findViewById(R.id.tv_airquality_status) ;
		
		disableSettingsControls() ;
	}
	
	/**
	 * Enable all Setting controls.
	 * This gets enabled only if the power switch is on
	 */
	private void enableSettingsControls() {
		imageButtonOne.setEnabled(true) ;
		imageButtonSpeedTwo.setEnabled(true) ;
		imageButtonSpeedThree.setEnabled(true) ;
		buttonAuto.setEnabled(true) ;
		buttonSilent.setEnabled(true) ;
		buttonTurbo.setEnabled(true) ;
	}
	
	/**
	 * Disable all Setting controls.
	 * This gets disabled if the power switch is off
	 */
	private void disableSettingsControls() {
		imageButtonOne.setEnabled(false) ;
		imageButtonSpeedTwo.setEnabled(false) ;
		imageButtonSpeedThree.setEnabled(false) ;
		buttonAuto.setEnabled(false) ;
		buttonSilent.setEnabled(false) ;
		buttonTurbo.setEnabled(false) ;
		
		airQualityStatusView.setText(getString(R.string.na)) ;
		airQualityStatusView.setTextColor(Color.WHITE) ;
	}

	/**
	 * Sets the device power state.
	 * 
	 * @param deviceState
	 *            the new device power state
	 */
	private void setDevicePowerState(boolean deviceState) {
		Log.e(TAG, "setDevicePowerState:" + deviceState);
		airpurifierController.setDevicePowerState(deviceState);
		
		if( deviceState ) {
			enableSettingsControls() ;
		}
		else {
			disableSettingsControls() ;
		}
	}

	/**
	 * Populate scroll view.
	 */
	private void populateScrollView() {
		final View[] children = new View[] { leftMenu, centerView,
				rightSettings };
		scrollView.initViews(children, AppConstants.SCROLLTOVIEWID,
				new SizeCallbackForMenu(getApplicationContext()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.sw_power:
			Log.i(TAG, "On Checked Change Power button : " + isChecked);
			setDevicePowerState(isChecked);
			break;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_fanspeed_auto:
			airpurifierController.setDeviceMode(DeviceMode.auto) ;
			break;

		case R.id.btn_fanspeed_silent:
			airpurifierController.setDeviceMotorSpeed(1) ;
			break;
		
		case R.id.btn_fanspeed_turbo:
			airpurifierController.setDeviceMotorSpeed(5) ;
			break;
		
		case R.id.ib_fanspeed_one:
			airpurifierController.setDeviceMotorSpeed(2) ;
			break;
		
		case R.id.ib_fanspeed_two:
			airpurifierController.setDeviceMotorSpeed(3) ;
			break;
		
		case R.id.ib_fanspeed_three:
			airpurifierController.setDeviceMotorSpeed(4) ;
			break;
		case R.id.iv_back:			
			scrollView.smoothScrollTo(leftMenu.getMeasuredWidth(), 0);
			settingsOut = !settingsOut;
			break ;
		}
	}
	
	/**
	 * Back button handler.
	 */
	@Override
	public void onBackPressed() {
		if (menuOut) {
			int left = leftMenu.getMeasuredWidth();
			scrollView.smoothScrollTo(left, 0);
			menuOut = !menuOut;
		} else if (settingsOut) {
			int left = leftMenu.getMeasuredWidth();
			scrollView.smoothScrollTo(left, 0);
			settingsOut = !settingsOut;
		} else {
			super.onBackPressed();
		}
	}

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.interfaces.ServerResponseListener#receiveServerResponse(int, java.lang.String)
	 */
	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {
		Log.i(TAG, "Sensor Data Received") ;
		if( airPurifierEventDto != null ) {			
			if( settingsOut)
				updateSettingsUI(airPurifierEventDto) ;
		}

	}
	
	/**
	 * This will update the Settings User Interface
	 * @param airPurifierEventDto
	 */
	private void updateSettingsUI(AirPurifierEventDto airPurifierEventDto) {
		updateAQIStatus(airPurifierEventDto.getIndoorAQI()) ;
		//TODO update other fields also
	}
	
	/**
	 * Depending on the AQI value set the status and the color of the Text.
	 * @param aqi
	 */
	private void updateAQIStatus(int aqi) {
			int textColor = getResources().getColor(R.color.green) ;
			Log.i(TAG, "AQI: "+aqi) ;
			String aqiMessage = "" ;
			if( aqi  <= 50 ) {
				aqiMessage = getString(R.string.good) ;
			}
			
			else if( aqi  >= 51 && aqi <=100 ) {
				aqiMessage = getString(R.string.moderate) ;
				textColor = getResources().getColor(R.color.yellow) ;
			}
			
			else if( aqi  >= 101 && aqi <= 150 ) {
				aqiMessage = getString(R.string.unhealthy_for_sensitive_groups) ;
				textColor = getResources().getColor(R.color.orange) ;
			}
			
			else if( aqi  >= 151 && aqi <= 200 ) {
				aqiMessage = getString(R.string.unhealthy) ;
				textColor = getResources().getColor(R.color.red) ;
			}
			
			else if( aqi  >= 201 && aqi <= 300 ) {
				aqiMessage = getString(R.string.very_unhealthy) ;
				textColor = getResources().getColor(R.color.purple) ;
			}
			
			else if( aqi  > 300 ) {
				aqiMessage = getString(R.string.hazardous) ;
				textColor = getResources().getColor(R.color.maroon) ;
			}
			
			airQualityStatusView.setText(aqiMessage);
			airQualityStatusView.setTextColor(textColor) ;
		}
	

	/**
	 * Gets the center view.
	 * 
	 * @return the center view
	 */
	protected abstract View getCenterView();

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {

		/** The scroll view. */
		HorizontalScrollView scrollView;

		/** The menu. */
		View menu;

		/**
		 * Menu must NOT be out/shown to start with.
		 * 
		 * @param scrollView
		 *            the scroll view
		 * @param menu
		 *            the menu
		 */
		/*
		 * boolean menuOut = false;
		 *//** The settings out. */
		/*
		 * boolean settingsOut = false;
		 */
		/**
		 * Instantiates a new click listener for scrolling.
		 * 
		 * @param scrollView
		 *            the scroll view
		 * @param menu
		 *            the menu
		 */
		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			int menuWidth = menu.getMeasuredWidth();
			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);
			switch (v.getId()) {
			case R.id.ivMenu:
				if (!menuOut) {
					int left = 0;
					scrollView.smoothScrollTo(left, 0);
				} else {
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
				}
				menuOut = !menuOut;
				break;

			case R.id.ivSettings:
				if (!settingsOut) {
					scrollView.smoothScrollBy(screenwidth, 0);
				} else {
					scrollView.smoothScrollTo(0, 0);
				}
				settingsOut = !settingsOut;
				break;
			}
		}
	}

	/**
	 * Helper that remembers the width of the 'slide' button, so that the
	 * 'slide' button remains in view, even when the menu is showing.
	 */
	static class SizeCallbackForMenu implements SizeCallback {

		/** The width. */
		int Width;
		/** The context. */
		Context context;

		/**
		 * Instantiates a new size callback for menu.
		 * 
		 * @param context
		 *            the context
		 */
		public SizeCallbackForMenu(Context context) {
			super();
			this.context = context;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.philips.cl.di.dev.pa.interfaces.SizeCallback#onGlobalLayout()
		 */
		@Override
		public void onGlobalLayout() {
			Width = Utils.getScreenWidth(context);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.philips.cl.di.dev.pa.interfaces.SizeCallback#getViewSize(int,
		 * int, int, int[])
		 */
		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = (int) ((Width) * (AppConstants.SCALELEFTMENU));
			}
		}
	}
}
