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
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener,OnCheckedChangeListener,AirPurifierEventListener,SensorEventListener,OnFocusChangeListener {
	private final static String TAG = "SettingsActivity" ;
	
	private Switch swPower;
	
	private Switch swChildLock ;
	
	private Switch swIndicatorLight ;

	/** The air purifier controller. */
	private AirPurifierController airpurifierController;
	
	private SensorDataController sensorDataController ;
	
	private ImageButton imageButtonOne ;
	private ImageButton imageButtonSpeedTwo ;
	private ImageButton imageButtonSpeedThree ;
	
	private Button buttonSilent ;
	private Button buttonTurbo ;
	private Button buttonAuto ;
	
	private Button buttonTimer ;
	
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
		swPower.setOnFocusChangeListener(this) ;
		
		swChildLock = (Switch) findViewById(R.id.sw_child_settings) ;
		
		swIndicatorLight = (Switch) findViewById(R.id.sw_indicator_light) ;
		
		imageButtonOne = (ImageButton) findViewById(R.id.ib_fanspeed_one) ;
		imageButtonOne.setOnClickListener(this) ;
		imageButtonOne.setOnFocusChangeListener(this) ;
		
		imageButtonSpeedTwo = (ImageButton) findViewById(R.id.ib_fanspeed_two) ;
		imageButtonSpeedTwo.setOnClickListener(this) ;
		imageButtonSpeedTwo.setOnFocusChangeListener(this) ;
		
		imageButtonSpeedThree = (ImageButton) findViewById(R.id.ib_fanspeed_three) ;
		imageButtonSpeedThree.setOnClickListener(this) ;
		imageButtonSpeedThree.setOnFocusChangeListener(this) ;
		
		buttonSilent = (Button) findViewById(R.id.btn_fanspeed_silent) ;
		buttonSilent.setOnClickListener(this) ;
		buttonSilent.setOnFocusChangeListener(this) ;
		
		buttonTurbo = (Button) findViewById(R.id.btn_fanspeed_turbo) ;
		buttonTurbo.setOnClickListener(this) ;
		buttonTurbo.setOnFocusChangeListener(this) ;
		
		buttonAuto = (Button) findViewById(R.id.btn_fanspeed_auto) ;
		buttonAuto.setOnClickListener(this) ;
		buttonAuto.setOnFocusChangeListener(this) ;
		
		buttonTimer = (Button) findViewById(R.id.btn_timer) ;
		buttonTimer.setOnClickListener(this) ;
		
		backButton = (ImageView) findViewById(R.id.iv_back) ;
		backButton.setOnClickListener(this) ;
		
		airQualityStatusView = (TextView) findViewById(R.id.tv_airquality_status) ;
		airQualityStatusView.setOnFocusChangeListener(this) ;
		
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
		
		swChildLock.setEnabled(true) ;
		swIndicatorLight.setEnabled(true) ;
		
		buttonTimer.setEnabled(true) ;
	}
	
	/**
	 * Disable all Setting controls.
	 * This gets disabled if the power switch is off
	 */
	private void disableSettingsControls() {
		imageButtonOne.setEnabled(false) ;
		imageButtonOne.setColorFilter(Color.GRAY) ;
		
		imageButtonSpeedTwo.setEnabled(false) ;
		imageButtonSpeedThree.setEnabled(false) ;
		buttonAuto.setEnabled(false) ;
		buttonSilent.setEnabled(false) ;
		buttonSilent.setAlpha(10.0f) ;
		
		buttonTurbo.setEnabled(false) ;
		
		airQualityStatusView.setText(getString(R.string.na)) ;
		airQualityStatusView.setTextColor(Color.BLACK) ;
		
		swChildLock.setEnabled(false) ;
		swIndicatorLight.setEnabled(false) ;
		
		buttonTimer.setEnabled(false) ;
	}
	
	
	@Override
	public void onClick(View v) {
		Log.i(TAG, "OnClick") ;
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
			if(!isSwitchControlled) {
				Log.i(TAG, "On Checked Change Power button : " + isChecked);
				setDevicePowerState(isChecked);				
			}
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
			imageButtonSpeedTwo.setAlpha(2.0f) ;
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
		
		updateAQIStatus(airPurifierEventDto) ;
		updatePowerOnOff(airPurifierEventDto.getPowerMode()) ;
		//TODO update other fields also
	}
	
	private boolean isSwitchControlled ;
	private void updatePowerOnOff(String powerOnOff) {
		isSwitchControlled = true ;
		Log.i(TAG, ""+powerOnOff) ;
		if(powerOnOff.equalsIgnoreCase("on")) {			
			swPower.setChecked(true) ;
			enableSettingsControls() ;
		}
		else if(powerOnOff.equalsIgnoreCase("off")) {
			swPower.setChecked(false) ;
		}
		isSwitchControlled = false ;
	}
	
	/**
	 * Depending on the AQI value set the status and the color of the Text.
	 * @param aqi
	 */
	private void updateAQIStatus(AirPurifierEventDto airPurifierEventDto) {
			int aqi = airPurifierEventDto.getIndoorAQI() ;
			Log.i(TAG, "AQI: "+aqi) ;
			int textColor = getResources().getColor(R.color.black) ;			
			String aqiMessage = getString(R.string.na) ;			
			
			if( airPurifierEventDto.getPowerMode().equals("on")) {
				if( aqi  <= 125 ) {
					aqiMessage = getString(R.string.very_good) ;
					textColor = getResources().getColor(R.color.aqi_very_good) ;
				}
				
				else if( aqi  >= 126 && aqi <=250 ) {
					aqiMessage = getString(R.string.good) ;
					textColor = getResources().getColor(R.color.aqi_good) ;
				}
				
				else if( aqi  >= 251 && aqi <= 375 ) {
					aqiMessage = getString(R.string.fair) ;
					textColor = getResources().getColor(R.color.aqi_fair) ;
				}
				
				else if( aqi  >= 376 && aqi <= 500 ) {
					aqiMessage = getString(R.string.bad) ;
					textColor = getResources().getColor(R.color.aqi_bad) ;
				}
			}
			
			airQualityStatusView.setText(aqiMessage);
			airQualityStatusView.setTextColor(textColor) ;
		}

	@Override
	public void onFocusChange(View v, boolean isFocused) {
		// TODO Auto-generated method stub
		Log.d(TAG, "OnFocusChanged: "+isFocused) ;
			switch(v.getId()) {
			case R.id.sw_power:
				break ;
			case  R.id.btn_fanspeed_auto:
					if( isFocused ) {
						buttonAuto.setBackgroundResource(R.drawable.fan_speed_control_selection_bg) ;
						buttonAuto.callOnClick() ;
					}
					else 
						buttonAuto.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
					break ;
			case  R.id.ib_fanspeed_one:
				if( isFocused ) {
					imageButtonOne.setBackgroundResource(R.drawable.fan_speed_control_selection_bg) ;
					imageButtonOne.callOnClick() ;
				}
				else 
					imageButtonOne.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
				break ;
			case  R.id.ib_fanspeed_three:
				if( isFocused ) {
					imageButtonSpeedThree.setBackgroundResource(R.drawable.fan_speed_control_selection_bg) ;
					imageButtonSpeedThree.callOnClick() ;
				}
				else 
					imageButtonSpeedThree.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
				break ;
			case  R.id.ib_fanspeed_two:
				if( isFocused ) {
					imageButtonSpeedTwo.setBackgroundResource(R.drawable.fan_speed_control_selection_bg) ;
					imageButtonSpeedTwo.callOnClick() ;
				}
				else 
					imageButtonSpeedTwo.setBackgroundResource(R.drawable.fan_speed_control_bg) ;
				break ;
			case  R.id.btn_fanspeed_silent:
					if( isFocused ) {
						buttonSilent.setBackgroundResource(R.drawable.fan_speed_control_selection_bg1) ;
						buttonSilent.callOnClick() ;
					}
					else
						buttonSilent.setBackgroundResource(R.drawable.fan_speed_control_bg1) ;
					break ;
			case  R.id.btn_fanspeed_turbo:
				if( isFocused ) {
					buttonTurbo.setBackgroundResource(R.drawable.fan_speed_control_selection_bg1) ;	
					buttonTurbo.callOnClick() ;
				}
				else
					buttonTurbo.setBackgroundResource(R.drawable.fan_speed_control_bg1) ;
				break ;
			}
	}
}
