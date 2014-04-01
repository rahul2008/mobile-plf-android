package com.philips.cl.di.dev.pa.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.ews.EwsActivity;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;

public class RightMenuClickListener implements OnClickListener {
	
	private AirPurifierController airPurifierController ;
	private static final String TAG = RightMenuClickListener.class.getSimpleName();
	
	private Context context;
	
	private Activity activity;
	
	private TextView autoText;
	
	private View fanSpeedBackground, timerBackground;
	
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
	
	public RightMenuClickListener(Context context) {
		this.context = context;
		this.activity = (Activity) context;
		
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
		fanSpeedBackground = activity.findViewById(R.id.background_fan_speed);
		fanSpeedTurbo = (Button) activity.findViewById(R.id.fan_speed_turbo);
		fanSpeedOne = (Button) activity.findViewById(R.id.fan_speed_one);
		fanSpeedTwo = (Button) activity.findViewById(R.id.fan_speed_two);
		fanSpeedThree = (Button) activity.findViewById(R.id.fan_speed_three);
		
		timer = (Button) activity.findViewById(R.id.btn_rm_set_timer);
		timerBackground = activity.findViewById(R.id.background_timer);
		timerButtons[0] = (Button) activity.findViewById(R.id.timer_off);
		timerButtons[1] = (Button) activity.findViewById(R.id.one_hour);
		timerButtons[2] = (Button) activity.findViewById(R.id.four_hours);
		timerButtons[3] = (Button) activity.findViewById(R.id.eight_hours);
		
		connect = (Button) activity.findViewById(R.id.connect) ;
		
		this.airPurifierController = new AirPurifierController(context) ;
 		
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
	 * @param airPurifierEventDto
	 */
	
	public void setSensorValues(AirPurifierEventDto airPurifierEventDto) {
//		Log.i(TAG, "setSensorValues fan speed " + Utils.getFanSpeedText(airPurifierEventDto.getFanSpeed()) + " dto fan speed " + airPurifierEventDto.getFanSpeed());
		Log.i(TAG, "setSensorValues " + airPurifierEventDto.getPowerMode());
		if(airPurifierEventDto.getPowerMode().equals(AppConstants.POWER_ON)) {
			power.setChecked(getPowerButtonState(airPurifierEventDto));
			setFanSpeed(airPurifierEventDto);
			timer.setText(getTimerText(airPurifierEventDto));
			toggleButtonBackground(getButtonToBeHighlighted(getTimerText(airPurifierEventDto)));
			schedule.setText("N.A.");
			childLock.setChecked(getOnOffStatus(airPurifierEventDto.getChildLock()));
			indicatorLight.setChecked(getOnOffStatus(airPurifierEventDto.getAqiL()));
		}
		else {
			power.setChecked(getPowerButtonState(airPurifierEventDto));
			disableControlPanelButtonsOnPowerOff() ;
		}
	}
	
	private int getButtonToBeHighlighted(String timer) {
		if(timer.equals(context.getString(R.string.onehour))) {
			return R.id.one_hour;
		} else if (timer.equals(context.getString(R.string.fourhour))) {
			return R.id.four_hours;
		} else if (timer.equals(context.getString(R.string.eighthour))) {
			return R.id.eight_hours;
		} else if (timer.equals(context.getString(R.string.off))) {
			return R.id.timer_off;
		}
		return 0;
	}

	@SuppressWarnings("deprecation")
	private void setFanSpeed(AirPurifierEventDto airPurifierEventDto) {
		String fanSpeedText = airPurifierEventDto.getFanSpeed();
		String sActualFanSpeed = airPurifierEventDto.getActualFanSpeed();
		Drawable buttonImage = null;
		fanSpeedAuto.setChecked(false);
		Log.i(TAG, "setFanSpeed " + fanSpeedText);
		isFanSpeedAuto = false;
		if(AppConstants.FAN_SPEED_SILENT.equals(fanSpeedText)) {
			fanSpeed.setText("Silent");
			buttonImage = context.getResources().getDrawable(R.drawable.button_blue_bg_2x);
			toggleFanSpeedButtonBackground(R.id.fan_speed_silent);
		} else if(AppConstants.FAN_SPEED_TURBO.equals(fanSpeedText)) {
			fanSpeed.setText("Turbo");
			buttonImage = context.getResources().getDrawable(R.drawable.button_blue_bg_2x);
			toggleFanSpeedButtonBackground(R.id.fan_speed_turbo);
		} else if(AppConstants.FAN_SPEED_AUTO.equals(fanSpeedText)) {
			fanSpeed.setText("Auto");
			buttonImage = context.getResources().getDrawable(R.drawable.button_blue_bg_2x);
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
			buttonImage = context.getResources().getDrawable(R.drawable.fan_speed_one);
			toggleFanSpeedButtonBackground(R.id.fan_speed_one);
		} else if(AppConstants.FAN_SPEED_TWO.equals(fanSpeedText)) {
//			Log.i(TAG, "setFanSpeed (AppConstants.FAN_SPEED_TWO.equals(fanSpeedText)");
			fanSpeed.setText("");
			buttonImage = context.getResources().getDrawable(R.drawable.fan_speed_two);
			toggleFanSpeedButtonBackground(R.id.fan_speed_two);
		} else if(AppConstants.FAN_SPEED_THREE.equals(fanSpeedText)) {
			fanSpeed.setText("");
			buttonImage = context.getResources().getDrawable(R.drawable.fan_speed_three);
			toggleFanSpeedButtonBackground(R.id.fan_speed_three);
		}
		fanSpeed.setBackgroundDrawable(buttonImage);
	}

	private boolean getPowerButtonState(AirPurifierEventDto airPurifierEventDto) {
//		Log.i(TAG, "getPowerButtonState airPurifierDTO " + (airPurifierEventDto == null));
		if(airPurifierEventDto == null) {
			disableControlPanelButtonsOnPowerOff();
			isPowerOn = false;
			return false;
		}
		String powerMode = airPurifierEventDto.getPowerMode();
		if(powerMode != null && powerMode.equals(AppConstants.POWER_ON)) {
			enableButtonsOnPowerOn(MainActivity.getAirPurifierEventDto());
			isPowerOn = true;
		} else {
			disableControlPanelButtonsOnPowerOff();
			isPowerOn = false;
		}
		
//		Log.i(TAG, "isPowerOn " + isPowerOn);
		
		return isPowerOn;
	}

	private String getTimerText(AirPurifierEventDto airPurifierEventDto) {
		int timeRemaining = airPurifierEventDto.getDtrs();
		if(timeRemaining > 0 && timeRemaining <= 3600) {
			return context.getString(R.string.onehour);
		} else if (timeRemaining > 3600 && timeRemaining <= 14400){
			return context.getString(R.string.fourhour);
		} else if (timeRemaining > 14400 && timeRemaining <= 28800){
			return context.getString(R.string.eighthour);
		} else {
			return context.getString(R.string.off);
		}
	}
	
	private boolean getOnOffStatus(int status) {
//		Log.i(TAG, "getOnOffStatus " + status);
		if(status == AppConstants.ON) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.connect:
			((MainActivity) activity).isEWSStarted = true ;
			((MainActivity) activity).stopAllServices() ;
			activity.startActivityForResult(new Intent(activity,EwsActivity.class), 20) ;
			break;
		case R.id.btn_rm_power:
			if(!isPowerOn) {				
				power.setChecked(true);
				enableButtonsOnPowerOn(MainActivity.getAirPurifierEventDto());
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
			fanSpeed.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_blue_bg_2x));
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
				controlDevice(ParserConstants.FAN_SPEED, "1") ;
			}
			toggleFanSpeedButtonBackground(R.id.fan_speed_auto);
			isFanSpeedAuto = !isFanSpeedAuto;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.fan_speed_turbo:			
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_blue_bg_2x));
			toggleFanSpeedButtonBackground(R.id.fan_speed_turbo);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "t") ;
			break;
		case R.id.fan_speed_one:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fan_speed_one));
			toggleFanSpeedButtonBackground(R.id.fan_speed_one);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "1") ;
			break;
		case R.id.fan_speed_two:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fan_speed_two));
			toggleFanSpeedButtonBackground(R.id.fan_speed_two);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "2") ;
			break;
		case R.id.fan_speed_three:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fan_speed_three));
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
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void toggleFanSpeedButtonBackground(int id) {
		fanSpeedOne.setBackgroundDrawable((id == fanSpeedOne.getId()) ? context.getResources().getDrawable(R.drawable.fan_speed_one) : context.getResources().getDrawable(R.drawable.fan_speed_one_white));
		fanSpeedTwo.setBackgroundDrawable((id == fanSpeedTwo.getId()) ? context.getResources().getDrawable(R.drawable.fan_speed_two) : context.getResources().getDrawable(R.drawable.fan_speed_two_white));
		fanSpeedThree.setBackgroundDrawable((id == fanSpeedThree.getId()) ? context.getResources().getDrawable(R.drawable.fan_speed_three) : context.getResources().getDrawable(R.drawable.fan_speed_three_white));
		fanSpeedSilent.setBackgroundDrawable((id == fanSpeedSilent.getId()) ? context.getResources().getDrawable(R.drawable.button_blue_bg_2x) : context.getResources().getDrawable(R.drawable.button_bg_normal_2x));
		fanSpeedSilent.setTextColor((id == fanSpeedSilent.getId()) ? Color.WHITE : Color.rgb(109, 109, 109));
		fanSpeedTurbo.setBackgroundDrawable((id == fanSpeedTurbo.getId()) ? context.getResources().getDrawable(R.drawable.button_blue_bg_2x) : context.getResources().getDrawable(R.drawable.button_bg_normal_2x));
		fanSpeedTurbo.setTextColor((id == fanSpeedTurbo.getId()) ? Color.WHITE : Color.rgb(109, 109, 109));
	}
	
	@SuppressWarnings("deprecation")
	private void toggleButtonBackground(int id) {
		for(int i = 0; i < timerButtons.length; i++) {
			if(id == timerButtons[i].getId()) {
				timerButtons[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_blue_bg_2x));
				timerButtons[i].setTextColor(Color.WHITE);
				
			} else {
				timerButtons[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_bg_normal_2x));
				timerButtons[i].setTextColor(Color.rgb(109, 109, 109));
			}
		}
	}

	private void controlDevice(String key, String value) {
		if ( MainActivity.getAirPurifierEventDto().getConnectionStatus() == com.philips.cl.di.dev.pa.constant.AppConstants.CONNECTED) {
			airPurifierController.setDeviceDetailsLocally(key, value) ;
		}
		else if ( MainActivity.getAirPurifierEventDto().getConnectionStatus() == com.philips.cl.di.dev.pa.constant.AppConstants.CONNECTED_VIA_PHILIPS) {
				airPurifierController.setDeviceDetailsRemotely(key, value) ;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void disableControlPanelButtonsOnPowerOff() {
		Log.i(TAG, "disableControlPanelButtonsOnPowerOff");
		Drawable disabledButton = context.getResources().getDrawable(R.drawable.button_bg_2x);
		disabledButton.setAlpha(100);
		
		fanSpeed.setClickable(false);
		fanSpeed.setBackgroundDrawable(disabledButton);
		
		timer.setClickable(false);
		timer.setBackgroundDrawable(disabledButton);
		
		schedule.setClickable(false);
		schedule.setBackgroundDrawable(disabledButton);
		
		childLock.setClickable(false);
//		childLock.setBackgroundDrawable(disabledButton);
		childLock.setChecked(false);
		
		indicatorLight.setClickable(false);
//		indicatorLight.setBackgroundDrawable(disabledButton);
		indicatorLight.setChecked(false);
		
		collapseFanSpeedMenu(true);
		collapseTimerMenu(true);
	}
	
	@SuppressWarnings("deprecation")
	private void enableButtonsOnPowerOn(AirPurifierEventDto airPurifierEventDto) {
		Drawable enabledButton = context.getResources().getDrawable(R.drawable.button_blue_bg_2x);
		
		fanSpeed.setClickable(true);

		if(airPurifierEventDto != null) {
			setFanSpeed(airPurifierEventDto);
		}
		timer.setClickable(true);
		timer.setBackgroundDrawable(enabledButton);
		
		schedule.setClickable(true);
		schedule.setBackgroundDrawable(enabledButton);
		
		if(airPurifierEventDto != null) {
			childLock.setClickable(true);
			childLock.setChecked(getOnOffStatus(airPurifierEventDto.getChildLock()));
			
			indicatorLight.setClickable(true);
			indicatorLight.setChecked(getOnOffStatus(airPurifierEventDto.getAqiL()));
		} else {
			childLock.setClickable(false);
			childLock.setChecked(false);
			
			indicatorLight.setClickable(false);
			indicatorLight.setChecked(false);
		}
	}
	
	/**
	 * @param if true, collapse the list
	 *					else expand it. 
	 */
	private void collapseTimerMenu(boolean collapse) {
		if(!collapse) {
			isTimerMenuVisible = !collapse;
			timerBackground.setVisibility(View.VISIBLE);
			timerButtons[0].setVisibility(View.VISIBLE);
			timerButtons[1].setVisibility(View.VISIBLE);
			timerButtons[2].setVisibility(View.VISIBLE);
			timerButtons[3].setVisibility(View.VISIBLE);
		} else {
			isTimerMenuVisible = !collapse;
			timerBackground.setVisibility(View.GONE);
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
			fanSpeedBackground.setVisibility(View.VISIBLE);
			fanSpeedTurbo.setVisibility(View.VISIBLE);
			fanSpeedOne.setVisibility(View.VISIBLE);
			fanSpeedTwo.setVisibility(View.VISIBLE);
			fanSpeedThree.setVisibility(View.VISIBLE);
		} else {
			isFanSpeedMenuVisible = !collapse;
			fanSpeedSilent.setVisibility(View.GONE);
			fanSpeedAuto.setVisibility(View.GONE);
			autoText.setVisibility(View.GONE);
			fanSpeedBackground.setVisibility(View.GONE);
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

	public void disableControlPanel(boolean connected, AirPurifierEventDto airPurifierEventDto) {
//		Log.i(TAG, "disableControlPanel connected " + connected);
		if(!connected) {
			power.setClickable(false);
			power.setChecked(false);
			disableControlPanelButtonsOnPowerOff();
			connect.setVisibility(View.VISIBLE) ;
		} else {
			connect.setVisibility(View.GONE) ;
			power.setClickable(true);
			
			power.setChecked(getPowerButtonState(airPurifierEventDto));
			if(isPowerOn)
				enableButtonsOnPowerOn(airPurifierEventDto);
		}
	}
}
