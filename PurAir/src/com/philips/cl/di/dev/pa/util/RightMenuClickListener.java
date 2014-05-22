package com.philips.cl.di.dev.pa.util;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.demo.DemoModeActivity;
import com.philips.cl.di.dev.pa.demo.DemoModeConstant;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;
import com.philips.cl.di.dev.pa.scheduler.SchedulerActivity;

public class RightMenuClickListener implements OnClickListener {
	
	private AirPurifierController airPurifierController ;
	private static final String TAG = RightMenuClickListener.class.getSimpleName();
	
	private MainActivity mainActivity;
	
	private TextView autoText;
	
	private RelativeLayout fanSpeedLayout, timerLayout; 
	
	//Control panel buttons
	private Button fanSpeed, timer, schedule;
	private ToggleButton power, childLock, indicatorLight;
	
	//On/off states for power, child lock and indicator light.
	private boolean isPowerOn, isChildLockOn, isIndicatorLightOn;
	
	//Fan speed menu buttons
	private Button fanSpeedSilent, fanSpeedTurbo, fanSpeedOne, fanSpeedTwo, fanSpeedThree;
	private ToggleButton fanSpeedAuto;
	
	//Timer buttons
	private Button[] timerButtons = new Button[4];
	
	private Button connect ;
	
	private boolean isFanSpeedMenuVisible, isTimerMenuVisible, isFanSpeedAuto;
	
	public RightMenuClickListener(MainActivity activity) {
		mainActivity = activity;
		
		isPowerOn = false;
		isChildLockOn = false;
		isIndicatorLightOn = false;
		
		isFanSpeedMenuVisible = false;
		isTimerMenuVisible = false;
		
		power = (ToggleButton) activity.findViewById(R.id.btn_rm_power);
		schedule = (Button) activity.findViewById(R.id.btn_rm_scheduler);
		childLock = (ToggleButton) activity.findViewById(R.id.btn_rm_child_lock);		
		indicatorLight = (ToggleButton) activity.findViewById(R.id.btn_rm_indicator_light);
		
		fanSpeed = (Button) activity.findViewById(R.id.btn_rm_fan_speed);
		
		fanSpeedSilent = (Button) activity.findViewById(R.id.fan_speed_silent);
		fanSpeedAuto = (ToggleButton) activity.findViewById(R.id.fan_speed_auto);
		autoText = (TextView) activity.findViewById(R.id.tv_fan_speed_auto);
		fanSpeedLayout = (RelativeLayout)activity.findViewById(R.id.layout_fan_speed);
		fanSpeedTurbo = (Button) activity.findViewById(R.id.fan_speed_turbo);
		fanSpeedOne = (Button) activity.findViewById(R.id.fan_speed_one);
		fanSpeedTwo = (Button) activity.findViewById(R.id.fan_speed_two);
		fanSpeedThree = (Button) activity.findViewById(R.id.fan_speed_three);
		
		timer = (Button) activity.findViewById(R.id.btn_rm_set_timer);
		timerLayout = (RelativeLayout)activity.findViewById(R.id.layout_timer);
		timerButtons[0] = (Button) activity.findViewById(R.id.timer_off);
		timerButtons[1] = (Button) activity.findViewById(R.id.one_hour);
		timerButtons[2] = (Button) activity.findViewById(R.id.four_hours);
		timerButtons[3] = (Button) activity.findViewById(R.id.eight_hours);
		
		connect = (Button) activity.findViewById(R.id.connect) ;
		
		airPurifierController = AirPurifierController.getInstance() ;
 		
	}
	
	/**
	 * 
	 *  public void setBackground(Drawable background) {
     *		//noinspection deprecation
     *		setBackgroundDrawable(background);
	 *	}
	 *  
	 *  setBackground(Drawable drawable) is supported from API 16 onwards.
	 *  And it calls setBackgroundDrawable internally.
	 *  
	 * @param airPortInfo
	 */
	
	public void setSensorValues(AirPortInfo airPortInfo) {
//		Log.i(TAG, "setSensorValues fan speed " + Utils.getFanSpeedText(airPurifierEventDto.getFanSpeed()) + " dto fan speed " + airPurifierEventDto.getFanSpeed());
		Log.i(TAG, "setSensorValues " + airPortInfo.getPowerMode());
		if(airPortInfo.getPowerMode().equals(AppConstants.POWER_ON)) {
			power.setChecked(getPowerButtonState(airPortInfo));
			setFanSpeed(airPortInfo);
			timer.setText(getTimerText(airPortInfo));
			toggleButtonBackground(getButtonToBeHighlighted(getTimerText(airPortInfo)));
			childLock.setChecked(getOnOffStatus(airPortInfo.getChildLock()));
			indicatorLight.setChecked(getOnOffStatus(airPortInfo.getAqiL()));
		}
		else {
			power.setChecked(getPowerButtonState(airPortInfo));
			disableControlPanelButtonsOnPowerOff() ;
		}
	}
	
	private int getButtonToBeHighlighted(String timer) {
		if(timer.equals(mainActivity.getString(R.string.onehour))) {
			return R.id.one_hour;
		} else if (timer.equals(mainActivity.getString(R.string.fourhour))) {
			return R.id.four_hours;
		} else if (timer.equals(mainActivity.getString(R.string.eighthour))) {
			return R.id.eight_hours;
		} else if (timer.equals(mainActivity.getString(R.string.off))) {
			return R.id.timer_off;
		}
		return 0;
	}

	private void setFanSpeed(AirPortInfo airPurifierEventDto) {
		String fanSpeedText = airPurifierEventDto.getFanSpeed();
		String sActualFanSpeed = airPurifierEventDto.getActualFanSpeed();
		int buttonImage = 0;
		fanSpeedAuto.setChecked(false);
		Log.i(TAG, "setFanSpeed " + fanSpeedText);
		isFanSpeedAuto = false;
		if(AppConstants.FAN_SPEED_SILENT.equals(fanSpeedText)) {
			fanSpeed.setText(mainActivity.getString(R.string.silent));
			buttonImage = R.drawable.button_blue_bg_2x;
			toggleFanSpeedButtonBackground(R.id.fan_speed_silent);
		} else if(AppConstants.FAN_SPEED_TURBO.equals(fanSpeedText)) {
			fanSpeed.setText(mainActivity.getString(R.string.turbo));
			buttonImage = R.drawable.button_blue_bg_2x;
			toggleFanSpeedButtonBackground(R.id.fan_speed_turbo);
		} else if(AppConstants.FAN_SPEED_AUTO.equals(fanSpeedText)) {
			fanSpeed.setText(mainActivity.getString(R.string.auto));
			buttonImage = R.drawable.button_blue_bg_2x;
			fanSpeedAuto.setChecked(true);
			isFanSpeedAuto = true;
			toggleFanSpeedButtonBackground(R.id.fan_speed_auto);
			if (sActualFanSpeed.equals("1"))
				toggleFanSpeedButtonBackground(R.id.fan_speed_one);
			else if (sActualFanSpeed.equals("2"))
				toggleFanSpeedButtonBackground(R.id.fan_speed_two);
			else if (sActualFanSpeed.equals("3"))
				toggleFanSpeedButtonBackground(R.id.fan_speed_three);
		} else if(AppConstants.FAN_SPEED_ONE.equals(fanSpeedText)) {
			fanSpeed.setText("");
			buttonImage = R.drawable.fan_speed_one;
			toggleFanSpeedButtonBackground(R.id.fan_speed_one);
		} else if(AppConstants.FAN_SPEED_TWO.equals(fanSpeedText)) {
			fanSpeed.setText("");
			buttonImage = R.drawable.fan_speed_two;
			toggleFanSpeedButtonBackground(R.id.fan_speed_two);
		} else if(AppConstants.FAN_SPEED_THREE.equals(fanSpeedText)) {
			fanSpeed.setText("");
			buttonImage = R.drawable.fan_speed_three;
			toggleFanSpeedButtonBackground(R.id.fan_speed_three);
		}
		fanSpeed.setBackgroundResource(buttonImage);
	}

	private boolean getPowerButtonState(AirPortInfo airportInfo) {
		if(airportInfo == null) {
			return false ;
		}
		String powerMode = airportInfo.getPowerMode();
		if(powerMode != null && powerMode.equals(AppConstants.POWER_ON)) {
			enableButtonsOnPowerOn(airportInfo);
			isPowerOn = true;
		} else {
			disableControlPanelButtonsOnPowerOff();
			isPowerOn = false;
		}
		
		
		return isPowerOn;
	}

	private String getTimerText(AirPortInfo airPurifierEventDto) {
		int timeRemaining = airPurifierEventDto.getDtrs();
		if(timeRemaining > 0 && timeRemaining <= 3600) {
			return mainActivity.getString(R.string.onehour);
		} else if (timeRemaining > 3600 && timeRemaining <= 14400){
			return mainActivity.getString(R.string.fourhour);
		} else if (timeRemaining > 14400 && timeRemaining <= 28800){
			return mainActivity.getString(R.string.eighthour);
		} else {
			return mainActivity.getString(R.string.off);
		}
	}
	
	private boolean getOnOffStatus(int status) {
		if(status == AppConstants.ON) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.connect:
			Intent intent = null;
			if (PurAirApplication.isDemoModeEnable()) {
				intent = new Intent(mainActivity, DemoModeActivity.class);
			} else {
				intent = new Intent(mainActivity, EWSActivity.class);
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainActivity.startActivity(intent) ;
			break;
		case R.id.btn_rm_power:
			if(!isPowerOn) {				
				power.setChecked(true);
				PurAirDevice purifier = mainActivity.getCurrentPurifier();
				AirPortInfo airPortInfo = purifier == null ? null : purifier.getAirPortInfo();
				enableButtonsOnPowerOn(airPortInfo);
				controlDevice(ParserConstants.POWER_MODE, "1") ;
				
			} else {
				power.setChecked(false);
				disableControlPanelButtonsOnPowerOff();
				controlDevice(ParserConstants.POWER_MODE, "0") ;
			}
			isPowerOn = !isPowerOn;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.btn_rm_child_lock:
			if(!isChildLockOn) {
				childLock.setChecked(true);
				controlDevice(ParserConstants.CHILD_LOCK, "1") ;
			} else {
				childLock.setChecked(false);
				controlDevice(ParserConstants.CHILD_LOCK, "0") ;
			}
			isChildLockOn = !isChildLockOn;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.btn_rm_indicator_light:
			if(!isIndicatorLightOn) {
				indicatorLight.setChecked(true);
				controlDevice(ParserConstants.AQI_LIGHT, "1") ;
			} else {
				indicatorLight.setChecked(false);
				controlDevice(ParserConstants.AQI_LIGHT, "0") ;
			}
			isIndicatorLightOn = !isIndicatorLightOn;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.btn_rm_fan_speed :
			collapseFanSpeedMenu(isFanSpeedMenuVisible);
			collapseTimerMenu(true);
			break;
		case R.id.fan_speed_silent:		
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.button_blue_bg_2x);
			toggleFanSpeedButtonBackground(R.id.fan_speed_silent);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "s") ;
			break;
		case R.id.fan_speed_auto:
			if(!isFanSpeedAuto) {
				controlDevice(ParserConstants.FAN_SPEED, "a") ;
			} else {
				String fspd = "1" ;
				if(PurifierManager.getInstance().getCurrentPurifier() != null &&
						PurifierManager.getInstance().getCurrentPurifier().getAirPortInfo() != null) {
					fspd = PurifierManager.getInstance().getCurrentPurifier().getAirPortInfo().getActualFanSpeed() ;
				}
				controlDevice(ParserConstants.FAN_SPEED, fspd) ;
			}
			toggleFanSpeedButtonBackground(R.id.fan_speed_auto);
			isFanSpeedAuto = !isFanSpeedAuto;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.fan_speed_turbo:			
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.button_blue_bg_2x);
			toggleFanSpeedButtonBackground(R.id.fan_speed_turbo);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "t") ;
			break;
		case R.id.fan_speed_one:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.fan_speed_one);
			toggleFanSpeedButtonBackground(R.id.fan_speed_one);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "1") ;
			break;
		case R.id.fan_speed_two:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.fan_speed_two);
			toggleFanSpeedButtonBackground(R.id.fan_speed_two);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "2") ;
			break;
		case R.id.fan_speed_three:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.fan_speed_three);
			toggleFanSpeedButtonBackground(R.id.fan_speed_three);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "3") ;
			break;
			
		case R.id.btn_rm_set_timer:
			collapseTimerMenu(isTimerMenuVisible);
			collapseFanSpeedMenu(true);
			break;
		case R.id.timer_off:
			timer.setText(((Button) v).getText());
			toggleButtonBackground(R.id.timer_off);
			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "0") ;
			break;
		case R.id.one_hour:
			timer.setText(((Button) v).getText());
			toggleButtonBackground(R.id.one_hour);
			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "1") ;
			break;
		case R.id.four_hours:
			timer.setText(((Button) v).getText());
			toggleButtonBackground(R.id.four_hours);
			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "4") ;
			break;
		case R.id.eight_hours:
			timer.setText(((Button) v).getText());
			toggleButtonBackground(R.id.eight_hours);
			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "8") ;
			break;
		case R.id.btn_rm_scheduler:
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			intent = new Intent(mainActivity,SchedulerActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainActivity.startActivity(intent) ;
			break;
		default:
			break;
		}
	}
	
	private void toggleFanSpeedButtonBackground(int id) {
		fanSpeedOne.setBackgroundResource((id == fanSpeedOne.getId()) ? R.drawable.fan_speed_one : R.drawable.fan_speed_one_white);
		fanSpeedTwo.setBackgroundResource((id == fanSpeedTwo.getId()) ? R.drawable.fan_speed_two : R.drawable.fan_speed_two_white);
		fanSpeedThree.setBackgroundResource((id == fanSpeedThree.getId()) ? R.drawable.fan_speed_three : R.drawable.fan_speed_three_white);
		fanSpeedSilent.setBackgroundResource((id == fanSpeedSilent.getId()) ? R.drawable.button_blue_bg_2x : R.drawable.button_bg_normal_2x);
		fanSpeedSilent.setTextColor((id == fanSpeedSilent.getId()) ? Color.WHITE : Color.rgb(109, 109, 109));
		fanSpeedTurbo.setBackgroundResource((id == fanSpeedTurbo.getId()) ? R.drawable.button_blue_bg_2x : R.drawable.button_bg_normal_2x);
		fanSpeedTurbo.setTextColor((id == fanSpeedTurbo.getId()) ? Color.WHITE : Color.rgb(109, 109, 109));
	}
	
	private void toggleButtonBackground(int id) {
		for(int i = 0; i < timerButtons.length; i++) {
			if(id == timerButtons[i].getId()) {
				timerButtons[i].setBackgroundResource(R.drawable.button_blue_bg_2x);
				timerButtons[i].setTextColor(Color.WHITE);
				
			} else {
				timerButtons[i].setBackgroundResource(R.drawable.button_bg_normal_2x);
				timerButtons[i].setTextColor(Color.rgb(109, 109, 109));
			}
		}
	}

	private void controlDevice(String key, String value) {
		PurAirDevice purifier = mainActivity.getCurrentPurifier();
		if (purifier == null ) {
			return ;
		}
		
		switch (purifier.getConnectionState()) {
			case CONNECTED_LOCALLY 	: airPurifierController.setDeviceDetailsLocally(key, value, purifier);
			case CONNECTED_REMOTELY : airPurifierController.setDeviceDetailsRemotely(key, value, purifier);
			case DISCONNECTED		: //NOP
		}
	}
	
	private void disableControlPanelButtonsOnPowerOff() {
		Log.i(TAG, "disableControlPanelButtonsOnPowerOff");
		Drawable disabledButton = mainActivity.getResources().getDrawable(R.drawable.button_bg_2x);
		disabledButton.setAlpha(100);
		
		fanSpeed.setClickable(false);
		fanSpeed.setBackgroundResource(R.drawable.button_bg_2x);
		
		timer.setClickable(false);
		timer.setBackgroundResource(R.drawable.button_bg_2x);
		
		schedule.setClickable(false);
		schedule.setBackgroundResource(R.drawable.button_bg_2x);
		
		childLock.setClickable(false);
		childLock.setChecked(false);
		
		indicatorLight.setClickable(false);
		indicatorLight.setChecked(false);
		
		collapseFanSpeedMenu(true);
		collapseTimerMenu(true);
	}
	
	private void enableButtonsOnPowerOn(AirPortInfo airPurifierEventDto) {
		
		if(airPurifierEventDto != null) {
			fanSpeed.setClickable(true);
			indicatorLight.setClickable(true);
			childLock.setClickable(true);
			timer.setClickable(true);
			timer.setBackgroundResource(R.drawable.button_blue_bg_2x);
			
			schedule.setClickable(true);
			schedule.setBackgroundResource(R.drawable.button_blue_bg_2x);
			childLock.setChecked(getOnOffStatus(airPurifierEventDto.getChildLock()));			
			setFanSpeed(airPurifierEventDto);
			indicatorLight.setChecked(getOnOffStatus(airPurifierEventDto.getAqiL()));
		}
		else {
			fanSpeed.setClickable(false);
			indicatorLight.setClickable(false);
			childLock.setClickable(false);
			timer.setClickable(false);			
			schedule.setClickable(false);
		}
	}
	
	/**
	 * @param if true, collapse the list
	 *					else expand it. 
	 */
	private void collapseTimerMenu(boolean collapse) {
		if(!collapse) {
			isTimerMenuVisible = !collapse;
			timerLayout.setVisibility(View.VISIBLE);
			timerButtons[0].setVisibility(View.VISIBLE);
			timerButtons[1].setVisibility(View.VISIBLE);
			timerButtons[2].setVisibility(View.VISIBLE);
			timerButtons[3].setVisibility(View.VISIBLE);
		} else {
			isTimerMenuVisible = !collapse;
			timerLayout.setVisibility(View.GONE);
			timerButtons[0].setVisibility(View.GONE);
			timerButtons[1].setVisibility(View.GONE);
			timerButtons[2].setVisibility(View.GONE);
			timerButtons[3].setVisibility(View.GONE);
		}
	}

	/**
	 * @param  if true, collapse the list
	 *					else expand it. 
	 */
	private void collapseFanSpeedMenu(boolean collapse) {
		if(!collapse) {
			isFanSpeedMenuVisible = !collapse;
			fanSpeedSilent.setVisibility(View.VISIBLE);
			fanSpeedAuto.setVisibility(View.VISIBLE);
			autoText.setVisibility(View.VISIBLE);
			fanSpeedLayout.setVisibility(View.VISIBLE);
			fanSpeedTurbo.setVisibility(View.VISIBLE);
			fanSpeedOne.setVisibility(View.VISIBLE);
			fanSpeedTwo.setVisibility(View.VISIBLE);
			fanSpeedThree.setVisibility(View.VISIBLE);
		} else {
			isFanSpeedMenuVisible = !collapse;
			fanSpeedSilent.setVisibility(View.GONE);
			fanSpeedAuto.setVisibility(View.GONE);
			autoText.setVisibility(View.GONE);
			fanSpeedLayout.setVisibility(View.GONE);
			fanSpeedTurbo.setVisibility(View.GONE);
			fanSpeedOne.setVisibility(View.GONE);
			fanSpeedTwo.setVisibility(View.GONE);
			fanSpeedThree.setVisibility(View.GONE);
		}
	}
	
	/**
	 * @param viewGroup loops through the entire view group and adds
	 * 					an onClickListerner to the buttons.
	 */
	public void setAllButtonListener(ViewGroup viewGroup) {
	    View v;
	    for (int i = 0; i < viewGroup.getChildCount(); i++) {
	        v = viewGroup.getChildAt(i);
	        if (v instanceof ViewGroup) {
	            setAllButtonListener((ViewGroup) v);
	        } else if (v instanceof Button) {
	            ((Button) v).setOnClickListener(this);
	        }
	    }
	}

	public void toggleControlPanel(boolean connected, AirPortInfo airPurifierEventDto) {
		if(!connected) {
			power.setClickable(false);
			power.setChecked(false);
			disableControlPanelButtonsOnPowerOff();
			connect.setVisibility(View.VISIBLE) ;
		} else {
			//Added if enable shop demo mode, when AirPurifier all ready connected. 
			PurAirDevice purAirDevice = PurifierManager.getInstance().getCurrentPurifier();
			if (purAirDevice == null || purAirDevice.getName() == null) return;
			if (PurAirApplication.isDemoModeEnable() && !purAirDevice.getName().endsWith(DemoModeConstant.DEMO)) {
				connect.setVisibility(View.VISIBLE) ;
			} else {
				connect.setVisibility(View.GONE) ;
			}
			
			if( airPurifierEventDto != null ) {
				power.setClickable(true);
				power.setChecked(getPowerButtonState(airPurifierEventDto));
				if(isPowerOn)
					enableButtonsOnPowerOn(airPurifierEventDto);
			}
			else {
				disableControlPanelButtonsOnPowerOff() ;
			}
		}
	}
}
