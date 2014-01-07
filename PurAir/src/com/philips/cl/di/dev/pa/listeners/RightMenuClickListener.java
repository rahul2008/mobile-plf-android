package com.philips.cl.di.dev.pa.listeners;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RightMenuClickListener implements OnClickListener {
	private static final String TAG = RightMenuClickListener.class.getSimpleName();
	
	private Context context;
	
	private Activity activity;
	
	//Control panel buttons
	private Button power, fanSpeed, timer, schedule, childLock, indicatorLight;
	
	//On/off states for power, child lock and indicator light.
	private boolean isPowerOn, isChildLockOn, isIndicatorLightOn;
	
	//Fan speed menu buttons
	private Button fanSpeedSilent, fanSpeedTurbo, fanSpeedAuto, fanSpeedOne, fanSpeedTwo, fanSpeedThree;
	
	//Timer buttons
	private Button timerOff, timerTwoHours, timerFourHours, timerEightHours;
	
	private boolean isFanSpeedMenuVisible, isTimerMenuVisible;
	
	public RightMenuClickListener(Context context) {
		this.context = context;
		this.activity = (Activity) context;
		
		isPowerOn = false;
		isChildLockOn = false;
		isIndicatorLightOn = false;
		
		isFanSpeedMenuVisible = false;
		isTimerMenuVisible = false;
		
		power = (Button) activity.findViewById(R.id.btn_rm_power);
		schedule = (Button) activity.findViewById(R.id.btn_rm_scheduler);
		childLock = (Button) activity.findViewById(R.id.btn_rm_child_lock);
		indicatorLight = (Button) activity.findViewById(R.id.btn_rm_indicator_light);
		
		fanSpeed = (Button) activity.findViewById(R.id.btn_rm_fan_speed);
		
		fanSpeedSilent = (Button) activity.findViewById(R.id.fan_speed_silent);
		fanSpeedAuto = (Button) activity.findViewById(R.id.fan_speed_auto);
		fanSpeedTurbo = (Button) activity.findViewById(R.id.fan_speed_turbo);
		fanSpeedOne = (Button) activity.findViewById(R.id.fan_speed_one);
		fanSpeedTwo = (Button) activity.findViewById(R.id.fan_speed_two);
		fanSpeedThree = (Button) activity.findViewById(R.id.fan_speed_three);
		
		timer = (Button) activity.findViewById(R.id.btn_rm_set_timer);
		
		timerOff = (Button) activity.findViewById(R.id.timer_off);
		timerTwoHours = (Button) activity.findViewById(R.id.two_hours);
		timerFourHours = (Button) activity.findViewById(R.id.four_hours);
		timerEightHours = (Button) activity.findViewById(R.id.eight_hours);
		
	}
	
	public void setSensorValues(AirPurifierEventDto airPurifierEventDto) {
		Log.i(TAG, "setSensorValues fan speed " + Utils.getFanSpeedText(airPurifierEventDto.getFanSpeed()) + " dto fan speed" + airPurifierEventDto.getFanSpeed());
		power.setBackgroundDrawable(getPowerButtonState(airPurifierEventDto));
		
		fanSpeed.setText(Utils.getFanSpeedText(airPurifierEventDto.getFanSpeed()));
		timer.setText(getTimerText(airPurifierEventDto));
		schedule.setText("N.A.");
		childLock.setBackgroundDrawable(getOnOffStatus(airPurifierEventDto.getChildLock()));
		indicatorLight.setBackgroundDrawable(getOnOffStatus(airPurifierEventDto.getAqiL()));
		
	}
	
	private Drawable getPowerButtonState(AirPurifierEventDto airPurifierEventDto) {
		Drawable powerState = null;
		String powerMode = airPurifierEventDto.getPowerMode();
		if(powerMode.equals("1")) {
			powerState = context.getResources().getDrawable(R.drawable.switch_off);
		} else {
			powerState = context.getResources().getDrawable(R.drawable.switch_on);
		}
		
		return powerState;
	}

	private String getPowerModeText(AirPurifierEventDto airPurifierEventDto) {
		String powerMode = airPurifierEventDto.getPowerMode();
		Log.i(TAG, "powerMode " + powerMode);
		if(powerMode.equals("1")) {
			return "On";
		} else {
			return "Off";
		}
	}
	
	private String getTimerText(AirPurifierEventDto airPurifierEventDto) {
		if(airPurifierEventDto.getDtrs() > 0) {
			return "-";
		} else {
			return "Off";
		}
	}
	
	private Drawable getOnOffStatus(int status) {
		Drawable buttonState = null;
		if(status == 1) {
			buttonState = context.getResources().getDrawable(R.drawable.switch_off);
		} else {
			buttonState = context.getResources().getDrawable(R.drawable.switch_off);
		}
		
		return buttonState;
	}
	
	@Override
	public void onClick(View v) {
		
//		Toast.makeText(context, TAG + " onClick", Toast.LENGTH_LONG).show();
		
		switch (v.getId()) {
		case R.id.connect:
			Toast.makeText(context, "Connect", Toast.LENGTH_LONG).show();
			break;
		case R.id.btn_rm_power:
			if(isPowerOn) {
				Log.i(TAG, "power is on");
				power.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.switch_on));
				enableOtherButtons();
				
			} else {
				power.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.switch_off));
				disableOtherButtons();
			}
			isPowerOn = !isPowerOn;
			break;
		case R.id.btn_rm_child_lock:
			if(isChildLockOn) {
				childLock.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.switch_on));
			} else {
				childLock.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.switch_off));
			}
			isChildLockOn = !isChildLockOn;
			break;
		case R.id.btn_rm_indicator_light:
			if(isIndicatorLightOn) {
				indicatorLight.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.switch_on));
			} else {
				indicatorLight.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.switch_off));
			}
			isIndicatorLightOn = !isIndicatorLightOn;
			break;
		case R.id.btn_rm_fan_speed :
			collapseOrExpandFanSpeedMenu(isFanSpeedMenuVisible);
			collapseOrExpandTimerMenu(true);
			break;
		case R.id.fan_speed_silent:
		case R.id.fan_speed_auto:
		case R.id.fan_speed_turbo:
		case R.id.fan_speed_one:
		case R.id.fan_speed_two:
		case R.id.fan_speed_three:
			fanSpeed.setText(((Button) v).getText());
			collapseOrExpandFanSpeedMenu(true);
			break;
			
		case R.id.btn_rm_set_timer:
			collapseOrExpandTimerMenu(isTimerMenuVisible);
			collapseOrExpandFanSpeedMenu(true);
			break;
		case R.id.timer_off:
		case R.id.two_hours:
		case R.id.four_hours:
		case R.id.eight_hours:
			timer.setText(((Button) v).getText());
			collapseOrExpandTimerMenu(true);
			break;
		default:
			break;
		}
	}
	
	private void disableOtherButtons() {
		Drawable disabledButton = context.getResources().getDrawable(R.drawable.button_bg_2x);
		
		fanSpeed.setClickable(false);
		fanSpeed.setBackgroundDrawable(disabledButton);
		disabledButton.setAlpha(100);
		timer.setClickable(false);
		timer.setBackgroundDrawable(disabledButton);
		
		
	}
	
	private void enableOtherButtons() {
		Drawable enabledButton = context.getResources().getDrawable(R.drawable.button_blue_bg_2x);
		Drawable switchOn = context.getResources().getDrawable(R.drawable.switch_on);
		fanSpeed.setClickable(true);
		fanSpeed.setBackgroundDrawable(enabledButton);
		timer.setClickable(true);
		timer.setBackgroundDrawable(enabledButton);
	}

	private void collapseOrExpandTimerMenu(boolean collapse) {
		if(!collapse) {
			isTimerMenuVisible = !collapse;
			timerOff.setVisibility(View.VISIBLE);
			timerTwoHours.setVisibility(View.VISIBLE);
			timerFourHours.setVisibility(View.VISIBLE);
			timerEightHours.setVisibility(View.VISIBLE);
		} else {
			isTimerMenuVisible = !collapse;
			timerOff.setVisibility(View.GONE);
			timerTwoHours.setVisibility(View.GONE);
			timerFourHours.setVisibility(View.GONE);
			timerEightHours.setVisibility(View.GONE);
		}
	}
	
	private void collapseOrExpandFanSpeedMenu(boolean collapse) {
		if(!collapse) {
			isFanSpeedMenuVisible = !collapse;
			fanSpeedSilent.setVisibility(View.VISIBLE);
			fanSpeedAuto.setVisibility(View.VISIBLE);
			fanSpeedTurbo.setVisibility(View.VISIBLE);
			fanSpeedOne.setVisibility(View.VISIBLE);
			fanSpeedTwo.setVisibility(View.VISIBLE);
			fanSpeedThree.setVisibility(View.VISIBLE);
		} else {
			isFanSpeedMenuVisible = !collapse;
			fanSpeedSilent.setVisibility(View.GONE);
			fanSpeedAuto.setVisibility(View.GONE);
			fanSpeedTurbo.setVisibility(View.GONE);
			fanSpeedOne.setVisibility(View.GONE);
			fanSpeedTwo.setVisibility(View.GONE);
			fanSpeedThree.setVisibility(View.GONE);
		}
	}
	
	
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
	
}
