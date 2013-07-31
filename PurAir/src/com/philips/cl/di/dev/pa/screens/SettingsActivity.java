package com.philips.cl.di.dev.pa.screens;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.AirPurifierController;
import com.philips.cl.di.dev.pa.controller.AirPurifierController.DeviceMode;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener,OnCheckedChangeListener,AirPurifierEventListener,SensorEventListener {
	private final static String TAG = "SettingsActivity" ;
	
	private Switch swPower;

	/** The air purifier controller. */
	private AirPurifierController airpurifierController;
	
	private SensorDataController sensorDataController ;
	
	private ImageButton imageButtonOne ;
	private ImageButton imageButtonSpeedTwo ;
	private ImageButton imageButtonSpeedThree ;
	
	private Button buttonSilent ;
	private Button buttonTurbo ;
	private Button buttonAuto ;
	
	private ImageView backButton ;
	
	private TextView airQualityStatusView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Oncreate") ;
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.in, R.anim.out);
		setContentView(R.layout.activity_settings) ;
		
		initializeControls() ;
		
		airpurifierController = new AirPurifierController(this,
				this,AppConstants.GET_SENSOR_DATA_REQUEST_TYPE);
		
		sensorDataController = SensorDataController.getInstance(this) ;
		
	}
	
	private void initializeControls() {
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
			//finish() ;
			break ;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.sw_power:
			Log.i(TAG, "On Checked Change Power button : " + isChecked);
			setDevicePowerState(isChecked);
			break;
		}
	}
	
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause") ;
		super.onPause();
		overridePendingTransition(R.anim.in, R.anim.out);
		sensorDataController.unRegisterListener(this) ;
	}
	
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume") ;
		super.onResume();
		sensorDataController.registerListener(this) ;
	}
	
	
	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.interfaces.ServerResponseListener#receiveServerResponse(int, java.lang.String)
	 */
	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {
		Log.i(TAG, "Sensor Data Received") ;
		if( airPurifierEventDto != null ) {
				updateUI(airPurifierEventDto) ;
		}
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
	 * This will update the Settings User Interface
	 * @param airPurifierEventDto
	 */
	private void updateUI(AirPurifierEventDto airPurifierEventDto) {
		updateAQIStatus(airPurifierEventDto.getIndoorAQI()) ;
		updatePowerOnOff(airPurifierEventDto.getPowerMode()) ;
		//TODO update other fields also
	}
	
	private void updatePowerOnOff(String powerOnOff) {
		if(powerOnOff.equals("on")) {
			swPower.setSelected(true) ;
		}
		else {
			swPower.setSelected(false) ;
		}
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
	
}
