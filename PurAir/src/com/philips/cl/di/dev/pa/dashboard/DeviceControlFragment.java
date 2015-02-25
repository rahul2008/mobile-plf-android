package com.philips.cl.di.dev.pa.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils.FanSpeed;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.NotificationsFragment;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PurifierEvent;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.scheduler.SchedulerActivity;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DeviceControlFragment extends BaseFragment implements OnClickListener, AirPurifierEventListener{
	
	private MainActivity mainActivity;
	
	private boolean isPowerOn, isFanSpeedAuto, isChildLockOn, isIndicatorLightOn, isTimerOn;
	private boolean isFanSpeedMenuVisible, isTimerMenuVisible;
	
	private RelativeLayout fanSpeedLayout, timerLayout;
	private ToggleButton power, fanSpeedAuto, indicatorLight, childLock, timer;
	private FontButton fanSpeed;
	private FontButton fanSpeedSilent, fanSpeedTurbo, fanSpeedOne, fanSpeedTwo, fanSpeedThree;
	
	private int [] timerButtonViewIds = {R.id.one_hour, R.id.four_hours, R.id.eight_hours};
	private FontButton[] timerButtons = new FontButton[timerButtonViewIds.length];
	private FontTextView setTimerText, timerState;
	
	private ProgressBar controlProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.device_control_panel, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		isFanSpeedMenuVisible = false;
		isTimerMenuVisible = false;
		mainActivity = (MainActivity) getActivity();
		initViews(getView());
	}
	
	@Override
	public void onResume() {
		PurifierManager.getInstance().addAirPurifierEventListener(this);
		PurAirDevice currentPurifier = PurifierManager.getInstance().getCurrentPurifier();
		updateButtonState(currentPurifier.getAirPortInfo());
		super.onResume();
	}
	
	@Override
	public void onPause() {
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		super.onPause();
	}

	private void initViews(View view) {
		ImageButton backButton = (ImageButton) view.findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		FontTextView heading=(FontTextView) view.findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.controls));
		power = (ToggleButton) view.findViewById(R.id.btn_rm_power);
		
		initFanSpeedButtons(view);
		initTimerViews(view);
		
		indicatorLight = (ToggleButton) view.findViewById(R.id.btn_rm_indicator_light);
		childLock = (ToggleButton) view.findViewById(R.id.btn_rm_child_lock);
		timerLayout= (RelativeLayout) view.findViewById(R.id.layout_timer);
		timer = (ToggleButton) view.findViewById(R.id.btn_rm_set_timer);

		power.setOnClickListener(this);
		indicatorLight.setOnClickListener(this);
		childLock.setOnClickListener(this);
		timer.setOnClickListener(this);
		
		controlProgress = (ProgressBar) view.findViewById(R.id.heading_progressbar);

		FontTextView scheduleTV = (FontTextView) view.findViewById(R.id.tv_rm_scheduler);
		scheduleTV.setOnClickListener(this);
		FontTextView notificationTV = (FontTextView) view.findViewById(R.id.tv_rm_notification);
		notificationTV.setOnClickListener(this);
	}

	private void initTimerViews(View view) {
		setTimerText = (FontTextView) view.findViewById(R.id.tv_rm_set_timer);
		setTimerText.setOnClickListener(this);
		timerState = (FontTextView) view.findViewById(R.id.timer_off);
		for(int i = 0; i < timerButtonViewIds.length; i++) {
			timerButtons[i] = (FontButton) view.findViewById(timerButtonViewIds[i]);
			timerButtons[i].setOnClickListener(this);
		}
	}

	private void initFanSpeedButtons(View view) {
		fanSpeedLayout = (RelativeLayout) view.findViewById(R.id.layout_fan_speed);
		fanSpeed= (FontButton) view.findViewById(R.id.btn_rm_fan_speed);
		fanSpeedAuto = (ToggleButton) view.findViewById(R.id.fan_speed_auto);
		fanSpeedSilent= (FontButton) view.findViewById(R.id.fan_speed_silent);
		fanSpeedTurbo= (FontButton) view.findViewById(R.id.fan_speed_turbo);
		fanSpeedOne= (FontButton) view.findViewById(R.id.fan_speed_one);
		fanSpeedTwo= (FontButton) view.findViewById(R.id.fan_speed_two);
		fanSpeedThree= (FontButton) view.findViewById(R.id.fan_speed_three);
		
		fanSpeed.setOnClickListener(this);
		fanSpeedOne.setOnClickListener(this);
		fanSpeedTwo.setOnClickListener(this);
		fanSpeedThree.setOnClickListener(this);
		fanSpeedTurbo.setOnClickListener(this);
		fanSpeedAuto.setOnClickListener(this);
		fanSpeedSilent.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_rm_power:
			if (!isPowerOn) {
				power.setChecked(true);
				PurAirDevice purifier = mainActivity.getCurrentPurifier();
				AirPortInfo airPortInfo = purifier == null ? null : purifier.getAirPortInfo();
				enableButtonsOnPowerOn(airPortInfo);
				controlDevice(ParserConstants.POWER_MODE, "1");
				MetricsTracker.trackActionTogglePower("Power on");
			} else {
				power.setChecked(false);
				disableControlPanelButtonsOnPowerOff();
				controlDevice(ParserConstants.POWER_MODE, "0");
				MetricsTracker.trackActionTogglePower("Power off");
			}
			isPowerOn = !isPowerOn;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.btn_rm_child_lock:
			if (!isChildLockOn) {
				childLock.setChecked(true);
				controlDevice(ParserConstants.CHILD_LOCK, "1");
				MetricsTracker.trackActionChildLock("Child lock on");
			} else {
				childLock.setChecked(false);
				controlDevice(ParserConstants.CHILD_LOCK, "0");
				MetricsTracker.trackActionChildLock("Child lock off");
			}
			isChildLockOn = !isChildLockOn;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.btn_rm_indicator_light:
			if (!isIndicatorLightOn) {
				indicatorLight.setChecked(true);
				controlDevice(ParserConstants.AQI_LIGHT, "1");
				MetricsTracker.trackActionIndicatorLight("Indicator light on");
			} else {
				indicatorLight.setChecked(false);
				controlDevice(ParserConstants.AQI_LIGHT, "0");
				MetricsTracker.trackActionIndicatorLight("Indicator light off");
			}
			isIndicatorLightOn = !isIndicatorLightOn;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.btn_rm_fan_speed:
			collapseFanSpeedMenu(isFanSpeedMenuVisible);
			collapseTimerMenu(true);
			break;
		case R.id.fan_speed_silent:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.philips_blue_btn);
			toggleFanSpeedButtonBackground(R.id.fan_speed_silent);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "s");
			MetricsTracker.trackActionFanSpeed("silent");
			break;
		case R.id.fan_speed_auto:
			if (!isFanSpeedAuto) {
				controlDevice(ParserConstants.FAN_SPEED, "a");
				MetricsTracker.trackActionFanSpeed("auto_on");
			} else {
				String fspd = "1";
				if (PurifierManager.getInstance().getCurrentPurifier() != null
						&& PurifierManager.getInstance().getCurrentPurifier().getAirPortInfo() != null) {
					fspd = PurifierManager.getInstance().getCurrentPurifier().getAirPortInfo().getActualFanSpeed();
				}
				controlDevice(ParserConstants.FAN_SPEED, fspd);
				MetricsTracker.trackActionFanSpeed("auto_off");
			}
			toggleFanSpeedButtonBackground(R.id.fan_speed_auto);
			isFanSpeedAuto = !isFanSpeedAuto;
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			break;
		case R.id.fan_speed_turbo:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.philips_blue_btn);
			toggleFanSpeedButtonBackground(R.id.fan_speed_turbo);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "t");
			MetricsTracker.trackActionFanSpeed("turbo");
			break;
		case R.id.fan_speed_one:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.fan_speed_one);
			toggleFanSpeedButtonBackground(R.id.fan_speed_one);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "1");
			MetricsTracker.trackActionFanSpeed("1");
			break;
		case R.id.fan_speed_two:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.fan_speed_two);
			toggleFanSpeedButtonBackground(R.id.fan_speed_two);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "2");
			MetricsTracker.trackActionFanSpeed("2");
			break;
		case R.id.fan_speed_three:
			fanSpeed.setText(((Button) v).getText());
			fanSpeed.setBackgroundResource(R.drawable.fan_speed_three);
			toggleFanSpeedButtonBackground(R.id.fan_speed_three);
			fanSpeedAuto.setChecked(false);
			isFanSpeedAuto = false;
			collapseFanSpeedMenu(true);
			controlDevice(ParserConstants.FAN_SPEED, "3");
			MetricsTracker.trackActionFanSpeed("3");
			break;

		case R.id.tv_rm_set_timer:
			setTimerStatus();
			collapseTimerMenu(isTimerMenuVisible);
			collapseFanSpeedMenu(true);
			break;
		case R.id.btn_rm_set_timer:
			PurAirDevice purifier = mainActivity.getCurrentPurifier();
			AirPortInfo airPortInfo = purifier == null ? null : purifier.getAirPortInfo();
			
//			if(airPortInfo != null && airPortInfo.getDtrs() > 0) {
//				isTimerOn = true;
//			} else {
//				isTimerOn = false;
//			}
			if(!timer.isChecked()) {
				controlDevice(ParserConstants.DEVICE_TIMER, "0");
				MetricsTracker.trackActionTimerAdded("off");
				break;
			} 
			
		case R.id.one_hour:
			isTimerOn = true;
			timer.setChecked(true);
			setTimerStatus();
			toggleTimerButtonBackground(R.id.one_hour);
//			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "1");
			MetricsTracker.trackActionTimerAdded("1hr");
			break;
		case R.id.four_hours:
			isTimerOn = true;
			timer.setChecked(true);
			setTimerStatus();
			toggleTimerButtonBackground(R.id.four_hours);
//			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "4");
			MetricsTracker.trackActionTimerAdded("4hr");
			break;
		case R.id.eight_hours:
			isTimerOn = true;
			timer.setChecked(true);
			setTimerStatus();
			toggleTimerButtonBackground(R.id.eight_hours);
//			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "8");
			MetricsTracker.trackActionTimerAdded("8hr");
			break;
		case R.id.tv_rm_scheduler:
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			Intent intent = new Intent(mainActivity, SchedulerActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainActivity.startActivity(intent);
			break;
		case R.id.tv_rm_notification:
			collapseFanSpeedMenu(true);
			collapseTimerMenu(true);
			mainActivity.showFragment(new NotificationsFragment());
			break;
		case R.id.heading_back_imgbtn: 
			mainActivity.onBackPressed();
			break;
		default:
			break;
		}
	}

	private void setTimerStatus() {
		PurAirDevice purifier = mainActivity.getCurrentPurifier();
		AirPortInfo airPortInfo = purifier == null ? null : purifier.getAirPortInfo();
		if(airPortInfo != null && airPortInfo.getDtrs() > 0) {
			timerState.setText(getTimeRemaining(airPortInfo.getDtrs()));
		} else {
			timerState.setText(getString(R.string.off));
		}
	}

	private String getTimeRemaining(int timeRemaining) {
		int hours = timeRemaining / 3600;
		int minutes = (timeRemaining % 3600) / 60; 
		return "" + hours + " : " + minutes;
	}
	
	private void controlDevice(String key, String value) {
		// Start the progress dialog
		controlProgress.setVisibility(View.VISIBLE);
		
		PurAirDevice currentPurifier = PurifierManager.getInstance().getCurrentPurifier();
		if(currentPurifier!=null){
		    currentPurifier.setPurifierDetails(key, value, PurifierEvent.DEVICE_CONTROL);
		}
	}

	@Override
	public void onAirPurifierChanged() { /**NOP*/ }

	@Override
	public void onAirPurifierEventReceived() {
		PurAirDevice currentPurifier = PurifierManager.getInstance().getCurrentPurifier();
		final AirPortInfo airPortInfo = currentPurifier.getAirPortInfo();
		mainActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// Dismiss the progress dialog
				controlProgress.setVisibility(View.INVISIBLE);
				updateButtonState(airPortInfo);
				
			}
		});
	}

	private void updateButtonState(AirPortInfo airPortInfo) {
		if(airPortInfo == null || airPortInfo.getPowerMode() == null) return;
		
		if(AppConstants.POWER_ON.equals(airPortInfo.getPowerMode())) {
			power.setChecked(true);
			setFanSpeed(airPortInfo);
			timer.setChecked(getTimerStatus(airPortInfo));
			toggleTimerButtonBackground(getButtonToBeHighlighted(getTimerText(airPortInfo)));
			childLock.setChecked(getOnOffStatus(airPortInfo.getChildLock()));
			indicatorLight.setChecked(getOnOffStatus(airPortInfo.getAqiL()));
			setTimerStatus();
		} else {
			power.setChecked(false);
			disableControlPanelButtonsOnPowerOff();
		}
	}

	@Override
	public void onFirmwareEventReceived() { /**NOP*/ }

	@Override
	public void onErrorOccurred(PurifierEvent purifierEvent) { 
		controlProgress.setVisibility(View.INVISIBLE);
	}
	
	private void enableButtonsOnPowerOn(AirPortInfo airPurifierEventDto) {

		if (airPurifierEventDto != null) {
			fanSpeed.setClickable(true);
			indicatorLight.setClickable(true);
			childLock.setClickable(true);
			timer.setClickable(true);
			timer.setChecked(getTimerStatus(airPurifierEventDto));

			childLock.setChecked(getOnOffStatus(airPurifierEventDto.getChildLock()));
			setFanSpeed(airPurifierEventDto);
			indicatorLight.setChecked(getOnOffStatus(airPurifierEventDto.getAqiL()));
		} else {
			fanSpeed.setClickable(false);
			indicatorLight.setClickable(false);
			childLock.setClickable(false);
			timer.setClickable(false);
		}
	}
	
	private boolean getTimerStatus(AirPortInfo airPurifierEventDto) {
		return airPurifierEventDto.getDtrs() > 0;
	}

	private void disableControlPanelButtonsOnPowerOff() {
		Drawable disabledButton = mainActivity.getResources().getDrawable(R.drawable.button_bg_2x);
		disabledButton.setAlpha(100);

		fanSpeed.setClickable(false);
		fanSpeed.setBackgroundResource(R.drawable.button_bg_2x);
		fanSpeed.setText(mainActivity.getString(R.string.off));

		timer.setClickable(false);
		timer.setChecked(false);

		childLock.setClickable(false);
		childLock.setChecked(false);

		indicatorLight.setClickable(false);
		indicatorLight.setChecked(false);

		collapseFanSpeedMenu(true);
		collapseTimerMenu(true);
		
		disabledButton = null;
	}
	
	private boolean getOnOffStatus(int status) {
		return AppConstants.ON == status;
	}
	
	private void collapseFanSpeedMenu(boolean collapse) {
		isFanSpeedMenuVisible = !collapse;
		if (!collapse) {
			fanSpeedLayout.setVisibility(View.VISIBLE);
		} else {
			fanSpeedLayout.setVisibility(View.GONE);
		}
	}
	
	private void collapseTimerMenu(boolean collapse) {
		isTimerMenuVisible = !collapse;
		if(!collapse) {
			timerLayout.setVisibility(View.VISIBLE);
		} else {
			timerLayout.setVisibility(View.GONE);
		}
	}
	
	private void setFanSpeed(AirPortInfo airPurifierEventDto) {
		FanSpeed fan = airPurifierEventDto.getFanSpeed();
		String sActualFanSpeed = airPurifierEventDto.getActualFanSpeed();
		int buttonImage = 0;
		fanSpeedAuto.setChecked(false);
		isFanSpeedAuto = false;

		switch (fan) {
		case SILENT:
			fanSpeed.setText(mainActivity.getString(R.string.silent));
			buttonImage = R.drawable.philips_blue_btn;
			toggleFanSpeedButtonBackground(R.id.fan_speed_silent);
			break;
		case AUTO:
			fanSpeed.setText(mainActivity.getString(R.string.auto));
			buttonImage = R.drawable.philips_blue_btn;
			fanSpeedAuto.setChecked(true);
			isFanSpeedAuto = true;
			toggleFanSpeedButtonBackground(R.id.fan_speed_auto);
			if (sActualFanSpeed.equals("1"))
				toggleFanSpeedButtonBackground(R.id.fan_speed_one);
			else if (sActualFanSpeed.equals("2"))
				toggleFanSpeedButtonBackground(R.id.fan_speed_two);
			else if (sActualFanSpeed.equals("3"))
				toggleFanSpeedButtonBackground(R.id.fan_speed_three);
			break;
		case TURBO:
			fanSpeed.setText(mainActivity.getString(R.string.turbo));
			buttonImage = R.drawable.philips_blue_btn;
			toggleFanSpeedButtonBackground(R.id.fan_speed_turbo);
			break;
		case ONE:
			fanSpeed.setText("");
			buttonImage = R.drawable.fan_speed_one;
			toggleFanSpeedButtonBackground(R.id.fan_speed_one);
			break;
		case TWO:
			fanSpeed.setText("");
			buttonImage = R.drawable.fan_speed_two;
			toggleFanSpeedButtonBackground(R.id.fan_speed_two);
			break;
		case THREE:
			fanSpeed.setText("");
			buttonImage = R.drawable.fan_speed_three;
			toggleFanSpeedButtonBackground(R.id.fan_speed_three);
			break;

		default:
			break;
		}
		fanSpeed.setBackgroundResource(buttonImage);
	}
	
	private void toggleFanSpeedButtonBackground(int id) {
		fanSpeedOne.setBackgroundResource((id == fanSpeedOne.getId()) ? R.drawable.fan_speed_one
						: R.drawable.fan_speed_one_white);
		fanSpeedTwo.setBackgroundResource((id == fanSpeedTwo.getId()) ? R.drawable.fan_speed_two
						: R.drawable.fan_speed_two_white);
		fanSpeedThree.setBackgroundResource((id == fanSpeedThree.getId()) ? R.drawable.fan_speed_three
						: R.drawable.fan_speed_three_white);
		fanSpeedSilent.setBackgroundResource((id == fanSpeedSilent.getId()) ? R.drawable.philips_blue_btn
						: R.drawable.button_bg_normal_2x);
		fanSpeedSilent.setTextColor((id == fanSpeedSilent.getId()) ? Color.WHITE
						: Color.rgb(109, 109, 109));
		fanSpeedTurbo.setBackgroundResource((id == fanSpeedTurbo.getId()) ? R.drawable.philips_blue_btn
						: R.drawable.button_bg_normal_2x);
		fanSpeedTurbo.setTextColor((id == fanSpeedTurbo.getId()) ? Color.WHITE
				: Color.rgb(109, 109, 109));
	}
	
	private void toggleTimerButtonBackground(int id) {
		for (int i = 0; i < timerButtons.length; i++) {
			if (id == timerButtons[i].getId()) {
				timerButtons[i].setBackgroundResource(R.drawable.philips_blue_btn);
				timerButtons[i].setTextColor(Color.WHITE);
			} else {
				timerButtons[i].setBackgroundResource(R.drawable.button_bg_normal_2x);
				timerButtons[i].setTextColor(Color.rgb(109, 109, 109));
			}
		}
	}
	
	private String getTimerText(AirPortInfo airPurifierEventDto) {
		int timeRemaining = airPurifierEventDto.getDtrs();
		if (timeRemaining > 0 && timeRemaining <= 3600) {
			return mainActivity.getString(R.string.onehour);
		} else if (timeRemaining > 3600 && timeRemaining <= 14400) {
			return mainActivity.getString(R.string.fourhour);
		} else if (timeRemaining > 14400 && timeRemaining <= 28800) {
			return mainActivity.getString(R.string.eighthour);
		} else {
			return mainActivity.getString(R.string.off);
		}
	}
	
	private int getButtonToBeHighlighted(String timer) {
		if (timer.equals(mainActivity.getString(R.string.onehour))) {
			return R.id.one_hour;
		} else if (timer.equals(mainActivity.getString(R.string.fourhour))) {
			return R.id.four_hours;
		} else if (timer.equals(mainActivity.getString(R.string.eighthour))) {
			return R.id.eight_hours;
		}
		return 0;
	}
}
