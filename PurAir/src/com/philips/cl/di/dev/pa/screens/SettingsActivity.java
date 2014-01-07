package com.philips.cl.di.dev.pa.screens;

import com.philips.cl.di.dev.pa.R;


import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.constants.ParserConstants;
import com.philips.cl.di.dev.pa.controller.AirPurifierController;
import com.philips.cl.di.dev.pa.controller.ICPCallbackHandler;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.interfaces.ICPEventListener;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.utils.JSONBuilder;
import com.philips.icpinterface.EventPublisher;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.data.Commands;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

public class SettingsActivity extends Activity implements OnClickListener,
OnCheckedChangeListener, AirPurifierEventListener, SensorEventListener,ICPEventListener,
OnFocusChangeListener {
	private final static String TAG = "SettingsActivity";

	private Switch swPower;

	private Switch swChildLock;

	private Switch swIndicatorLight;

	/** The air purifier controller. */
	private AirPurifierController airpurifierController;

	private SensorDataController sensorDataController;

	private ImageButton imageButtonOne;
	private ImageButton imageButtonSpeedTwo;
	private ImageButton imageButtonSpeedThree;

	private Button buttonSilent;
	private Button buttonTurbo;
	private Button buttonAuto;

	private Button buttonTimer;

	private ImageView backButton;

	private CustomTextView airQualityStatusView;

	private ICPCallbackHandler icpHandler ;
	
	private EventSubscription evSubscription = null;
	
	private static boolean isRunning = false ;

	private EventPublisher eventPublisher ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Oncreate");
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.in, R.anim.out);
		setContentView(R.layout.activity_settings);
		initializeControls();

		airpurifierController = new AirPurifierController(this, this);

		sensorDataController = new SensorDataController(this,this) ;
		sensorDataController.startPolling() ;

		if(icpHandler == null)
			icpHandler  = new ICPCallbackHandler();
		icpHandler.setHandler(this);
		evSubscription = EventSubscription.getInstance();
		eventPublisher = new EventPublisher(icpHandler);
	}
	
	private void startDCS() {
		if (!isRunning) {
			evSubscription = EventSubscription.create(icpHandler,1000);
			evSubscription.executeCommand() ;
		}
	}
	
	private void publishEvent(String key, String value) {
		eventPublisher.setEventInformation(AppConstants.DI_COMM_REQUEST, AppConstants.DI_ACTION_PUTPROPS, "", "", 20, 120);
		eventPublisher.setEventData(JSONBuilder.getPublishEventBuilder(key, value));
		eventPublisher.setTargets(new String[] {""});
		eventPublisher.setEventCommand(Commands.PUBLISH_EVENT);
		
		eventPublisher.executeCommand();
	}
	
	private void stopDCS() {
		isRunning = false ;
		evSubscription.stopCommand() ;
	}

	private void initializeControls() {
		swPower = (Switch) findViewById(R.id.sw_power);
		swPower.setOnCheckedChangeListener(this);
		swPower.setOnFocusChangeListener(this);

		swChildLock = (Switch) findViewById(R.id.sw_child_settings);
		swChildLock.setOnCheckedChangeListener(this);
		swChildLock.setOnFocusChangeListener(this);

		swIndicatorLight = (Switch) findViewById(R.id.sw_indicator_light);
		swIndicatorLight.setOnCheckedChangeListener(this);
		swIndicatorLight.setOnFocusChangeListener(this);

		imageButtonOne = (ImageButton) findViewById(R.id.ib_fanspeed_one);
		imageButtonOne.setOnClickListener(this);
		imageButtonOne.setOnFocusChangeListener(this);

		imageButtonSpeedTwo = (ImageButton) findViewById(R.id.ib_fanspeed_two);
		imageButtonSpeedTwo.setOnClickListener(this);
		imageButtonSpeedTwo.setOnFocusChangeListener(this);

		imageButtonSpeedThree = (ImageButton) findViewById(R.id.ib_fanspeed_three);
		imageButtonSpeedThree.setOnClickListener(this);
		imageButtonSpeedThree.setOnFocusChangeListener(this);

		buttonSilent = (Button) findViewById(R.id.btn_fanspeed_silent);
		buttonSilent.setOnClickListener(this);
		buttonSilent.setOnFocusChangeListener(this);

		buttonTurbo = (Button) findViewById(R.id.btn_fanspeed_turbo);
		buttonTurbo.setOnClickListener(this);
		buttonTurbo.setOnFocusChangeListener(this);

		buttonAuto = (Button) findViewById(R.id.btn_fanspeed_auto);
		buttonAuto.setOnClickListener(this);
		buttonAuto.setOnFocusChangeListener(this);

		buttonTimer = (Button) findViewById(R.id.btn_timer);
		buttonTimer.setOnClickListener(this);

		backButton = (ImageView) findViewById(R.id.iv_back);
		backButton.setOnClickListener(this);

		airQualityStatusView = (CustomTextView) findViewById(R.id.tv_airquality_status);
		airQualityStatusView.setOnFocusChangeListener(this);

		disableSettingsControls();
	}

	/**
	 * Enable all Setting controls. This gets enabled only if the power switch
	 * is on
	 */
	private void enableSettingsControls() {
		imageButtonOne.setEnabled(true);
		imageButtonSpeedTwo.setEnabled(true);
		imageButtonSpeedThree.setEnabled(true);
		buttonAuto.setEnabled(true);
		buttonSilent.setEnabled(true);
		buttonTurbo.setEnabled(true);

		swChildLock.setEnabled(true);
		swIndicatorLight.setEnabled(true);

		buttonTimer.setEnabled(true);
	}

	/**
	 * Disable all Setting controls. This gets disabled if the power switch is
	 * off
	 */
	private void disableSettingsControls() {
		imageButtonOne.setEnabled(false) ;
		imageButtonSpeedTwo.setEnabled(false);
		imageButtonSpeedThree.setEnabled(false);
		buttonAuto.setEnabled(false);
		buttonSilent.setEnabled(false);
		buttonTurbo.setEnabled(false);	

		airQualityStatusView.setText(getString(R.string.na));
		airQualityStatusView.setTextColor(Color.BLACK);

		swChildLock.setEnabled(false);		
		swChildLock.setChecked(false) ;
		swIndicatorLight.setEnabled(false);
		swIndicatorLight.setChecked(false) ;

		buttonTimer.setEnabled(false);

		// tbLayout.setEnabled(false) ;
		imageButtonOne.setBackgroundResource(R.drawable.fan_speed_control_bg);
		imageButtonSpeedTwo.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
		imageButtonSpeedThree.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
		buttonTurbo.setBackgroundResource(R.drawable.fan_speed_control_bg1) ;
		buttonSilent.setBackgroundResource(R.drawable.fan_speed_control_bg1) ;
		buttonAuto.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
	}

	private boolean isSentReq ;
	@Override
	public void onClick(View v) {
		isSentReq = true ;
		Log.i(TAG, "OnClick");
		switch (v.getId()) {
		case R.id.btn_fanspeed_auto:
			controlDevice(ParserConstants.MACHINE_MODE, "a") ;
			updateMotorSpeed("0","a") ;
			break;

		case R.id.btn_fanspeed_silent:
			controlDevice(ParserConstants.MACHINE_MODE, "s") ;
			updateMotorSpeed("s","s") ;
			break;

		case R.id.btn_fanspeed_turbo:
			controlDevice(ParserConstants.MACHINE_MODE, "t") ;
			updateMotorSpeed("t","t") ;
			break;

		case R.id.ib_fanspeed_one:
			controlDevice(ParserConstants.MACHINE_MODE, "1") ;
			updateMotorSpeed("1","") ;
			break;

		case R.id.ib_fanspeed_two:
			controlDevice(ParserConstants.MACHINE_MODE, "2") ;
			updateMotorSpeed("2","") ;
			break;

		case R.id.ib_fanspeed_three:
			controlDevice(ParserConstants.MACHINE_MODE, "3") ;
			updateMotorSpeed("3","") ;
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_timer:
			showTimerDialog();
			break;
		}
	}
	
	private boolean isLocal ;
	
	/**
	 * This method controls the device
	 * Depending on the connection type it controls either through locally or remotely
	 * @param key
	 * @param value
	 */
	private void controlDevice(String key, String value) {
		if ( isLocal ) {
			airpurifierController.setDeviceDetailsLocally(key, value) ;
		}
		else {
			airpurifierController.setDeviceDetailsRemotely(key, value) ;
		}
	}
	
	private void setChildSettings(boolean isChecked) {
		String childLockValue = (isChecked) ? "1" : "0" ;	
		controlDevice(ParserConstants.CL,childLockValue) ;
	}

	private void showTimerDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.getWindow().setBackgroundDrawableResource(R.color.whitesmoke);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setContentView(R.layout.dialog_timer);

		dialog.show();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.sw_power:
			if (!isSwitchControlled) {
				Log.i(TAG, "On Checked Change Power button : " + isChecked);
				setDevicePowerState(isChecked);
			}
			break;
		case R.id.sw_child_settings:
			if(!isChildLockSwitchControlled)
				setChildSettings(isChecked) ;
			break;
		case R.id.sw_indicator_light:
			if(!isIndicatorSwitchControlled)
				setIndicatorLight(isChecked) ;
			break ;
		}
	}

	private void setIndicatorLight(boolean isChecked) {
		String indicatorLightValue = (isChecked) ? "1" : "0" ;
		controlDevice(ParserConstants.AQI_LIGHT,indicatorLightValue) ;
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
		overridePendingTransition(R.anim.in, R.anim.out);
	}

	@Override
	protected void onStop() {
		stopDCS() ;
		super.onStop();
		//sensorDataController.stopPolling() ;
		sensorDataController.stopCPPPolling() ;
		stopDCS() ;
	}

	@Override
	protected void onStart() {
		//sensorDataController.startPolling() ;
		sensorDataController.startCPPPolling() ;
		startDCS() ;
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philips.cl.di.dev.pa.interfaces.ServerResponseListener#
	 * receiveServerResponse(int, java.lang.String)
	 */
	@Override
	public void airPurifierEventReceived(AirPurifierEventDto airPurifierEventDto) {
		Log.i(TAG, "Received") ;
		if (airPurifierEventDto != null) {
			updateUI(airPurifierEventDto);
		}
		isSentReq = false ;
	}

	/**
	 * Sets the device power state.
	 * 
	 * @param deviceState
	 *            the new device power state
	 */
	private void setDevicePowerState(boolean isPowerOnOff) {
		Log.e(TAG, "setDevicePowerState:" + isPowerOnOff);
		String pwrMode = (isPowerOnOff) ? "1" : "0" ;	
		controlDevice(ParserConstants.POWER_MODE,pwrMode);
		if (isPowerOnOff) {
			imageButtonSpeedTwo.setAlpha(2.0f);
			enableSettingsControls();
		} else {
			disableSettingsControls();
		}
	}

	/**
	 * This will update the Settings User Interface
	 * 
	 * @param airPurifierEventDto
	 */
	private void updateUI(AirPurifierEventDto airPurifierEventDto) {
		updateAQIStatus(airPurifierEventDto);
		updatePowerOnOff(airPurifierEventDto.getPowerMode());
		if (airPurifierEventDto.getPowerMode().equals("1")) {
			updateMotorSpeed(airPurifierEventDto.getFanSpeed(), airPurifierEventDto.getMachineMode()) ;
			updateChildLock(airPurifierEventDto.getChildLock()) ;
			updateIndicatorLight(airPurifierEventDto.getAqiL()) ;
		}
		// TODO update other fields also
	}

	/**
	 * This updates the AQI Indicator Light
	 * @param aqiL
	 */
	private void updateIndicatorLight(int aqiL) {
		isIndicatorSwitchControlled = true ;
		if (aqiL == 1) {
			swIndicatorLight.setChecked(true);
		} else {
			swIndicatorLight.setChecked(false);
		}
		isIndicatorSwitchControlled = false ;
		
	}

	/**
	 * This method updates the child lock property of the Air Purifier device
	 * @param childLock
	 */
	private void updateChildLock(int childLock) {
		isChildLockSwitchControlled = true;
		if (childLock == 1) {
			swChildLock.setChecked(true);
		} else {
			swChildLock.setChecked(false);
		}
		isChildLockSwitchControlled = false;
	}

	private void updateMotorSpeed(String motorSpeed, String mode) {
		imageButtonOne.setBackgroundResource(R.drawable.fan_speed_control_bg);
		imageButtonSpeedTwo.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
		imageButtonSpeedThree.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
		buttonTurbo.setBackgroundResource(R.drawable.fan_speed_control_bg1) ;
		buttonSilent.setBackgroundResource(R.drawable.fan_speed_control_bg1) ;
		buttonAuto.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
		
		if( motorSpeed.equals("s") ) {
			buttonSilent.setBackgroundResource(R.drawable.fan_speed_control_selection_bg1) ;
		}
		else if(motorSpeed.equals("1")) {
			imageButtonOne
			.setBackgroundResource(R.drawable.fan_speed_control_selection_bg);
		}
		else if(motorSpeed.equals("2")) {
			imageButtonSpeedTwo
			.setBackgroundResource(R.drawable.fan_speed_control_selection_bg);
		}
		else if(motorSpeed.equals("3")) {
			imageButtonSpeedThree
			.setBackgroundResource(R.drawable.fan_speed_control_selection_bg);
		}
		else if(motorSpeed.equals("t")) {
			buttonTurbo
			.setBackgroundResource(R.drawable.fan_speed_control_selection_bg1);
		}
		else if ( mode.equals("a")) {
			buttonAuto.setBackgroundResource(R.drawable.fan_speed_control_selection_bg) ;
		}
	} 

	private boolean isSwitchControlled, isChildLockSwitchControlled, isIndicatorSwitchControlled;

	private void updatePowerOnOff(String powerOnOff) {
		isSwitchControlled = true;
		if (powerOnOff.equalsIgnoreCase("1")) {
			swPower.setChecked(true);
			enableSettingsControls();
		} else if (powerOnOff.equalsIgnoreCase("0")) {
			swPower.setChecked(false);
			disableSettingsControls() ;
		}
		isSwitchControlled = false;
	}

	/**
	 * Depending on the AQI value set the status and the color of the Text.
	 * 
	 * @param aqi
	 */
	private void updateAQIStatus(AirPurifierEventDto airPurifierEventDto) {
		int aqi = airPurifierEventDto.getIndoorAQI();
		int textColor = getResources().getColor(R.color.black);
		String aqiMessage = getString(R.string.na);

		if (airPurifierEventDto.getPowerMode().equals("on")) {
			if (aqi <= 125) {
				aqiMessage = getString(R.string.very_good);
				textColor = getResources().getColor(R.color.aqi_very_good);
			}

			else if (aqi >= 126 && aqi <= 250) {
				aqiMessage = getString(R.string.good);
				textColor = getResources().getColor(R.color.aqi_good);
			}

			else if (aqi >= 251 && aqi <= 375) {
				aqiMessage = getString(R.string.fair);
				textColor = getResources().getColor(R.color.aqi_fair);
			}

			else if (aqi >= 376 && aqi <= 500) {
				aqiMessage = getString(R.string.bad);
				textColor = getResources().getColor(R.color.aqi_bad);
			}

			aqiMessage = aqi + " " + aqiMessage + "";
		}

		airQualityStatusView.setText(aqiMessage);
		airQualityStatusView.setTextColor(textColor);
	}

	@Override
	public void onFocusChange(View v, boolean isFocused) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {
		if ( !isSentReq ) {
			updateUI(airPurifierEventDto) ;
		}
	}

	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		
		EventSubscription evs = (EventSubscription) obj ;
		System.out.println("DCS events received: "+evs);
		if ( null != evs ) {
			System.out.println("Length: "+evs.getNumberOfEventsReturned());
			for (int i = 0; i < evs.getNumberOfEventsReturned(); i++)
			{
				System.out.println("Events Received");
				System.out.println(evs.getData(i));
			}
		}
	}
	
	
}
