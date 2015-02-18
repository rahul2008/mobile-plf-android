package com.philips.cl.di.dev.pa.util;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils.FanSpeed;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.fragment.NotificationsFragment;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PurifierEvent;
import com.philips.cl.di.dev.pa.scheduler.SchedulerActivity;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class PurifierControlPanel implements OnClickListener {

	private static final String TAG = PurifierControlPanel.class.getSimpleName();

	private MainActivity mainActivity;

	private TextView autoText;

	private RelativeLayout fanSpeedLayout, timerLayout;

	private Button fanSpeed, timer;
	private ToggleButton power, childLock, indicatorLight;

	private boolean isPowerOn, isChildLockOn, isIndicatorLightOn;
	private FontTextView powerStatus;

	private Button fanSpeedSilent, fanSpeedTurbo, fanSpeedOne, fanSpeedTwo,
			fanSpeedThree;
	private ToggleButton fanSpeedAuto;

	private Button[] timerButtons = new Button[4];

	private boolean isFanSpeedMenuVisible, isTimerMenuVisible, isFanSpeedAuto;

	public PurifierControlPanel(MainActivity activity) {
		mainActivity = activity;

		isPowerOn = false;
		isChildLockOn = false;
		isIndicatorLightOn = false;

		isFanSpeedMenuVisible = false;
		isTimerMenuVisible = false;

		power = (ToggleButton) activity.findViewById(R.id.btn_rm_power);
		FontTextView scheduleTV = (FontTextView) activity.findViewById(R.id.tv_rm_scheduler);
		scheduleTV.setOnClickListener(this);
		FontTextView notificationTV = (FontTextView) activity.findViewById(R.id.tv_rm_notification);
		notificationTV.setOnClickListener(this);
		
		childLock = (ToggleButton) activity.findViewById(R.id.btn_rm_child_lock);
		indicatorLight = (ToggleButton) activity.findViewById(R.id.btn_rm_indicator_light);

		fanSpeed = (Button) activity.findViewById(R.id.btn_rm_fan_speed);

		fanSpeedSilent = (Button) activity.findViewById(R.id.fan_speed_silent);
		fanSpeedAuto = (ToggleButton) activity.findViewById(R.id.fan_speed_auto);
		autoText = (TextView) activity.findViewById(R.id.tv_fan_speed_auto);
		fanSpeedLayout = (RelativeLayout) activity.findViewById(R.id.layout_fan_speed);
		fanSpeedTurbo = (Button) activity.findViewById(R.id.fan_speed_turbo);
		fanSpeedOne = (Button) activity.findViewById(R.id.fan_speed_one);
		fanSpeedTwo = (Button) activity.findViewById(R.id.fan_speed_two);
		fanSpeedThree = (Button) activity.findViewById(R.id.fan_speed_three);

		powerStatus = (FontTextView) activity.findViewById(R.id.tv_rm_power_status);
		powerStatus.setSelected(true);

		timer = (Button) activity.findViewById(R.id.btn_rm_set_timer);
		timerLayout = (RelativeLayout) activity.findViewById(R.id.layout_timer);
		timerButtons[0] = (Button) activity.findViewById(R.id.timer_off);
		timerButtons[1] = (Button) activity.findViewById(R.id.one_hour);
		timerButtons[2] = (Button) activity.findViewById(R.id.four_hours);
		timerButtons[3] = (Button) activity.findViewById(R.id.eight_hours);
	}

	/**
	 * 
	 * public void setBackground(Drawable background) { //noinspection
	 * deprecation setBackgroundDrawable(background); }
	 * 
	 * setBackground(Drawable drawable) is supported from API 16 onwards. And it
	 * calls setBackgroundDrawable internally.
	 * 
	 * @param airPortInfo
	 */

	public void setSensorValues(AirPortInfo airPortInfo) {
		if (airPortInfo == null || airPortInfo.getPowerMode() == null)
			return;
		if (airPortInfo.getPowerMode().equals(AppConstants.POWER_ON)) {
			power.setChecked(getPowerButtonState(airPortInfo));
			setFanSpeed(airPortInfo);
			timer.setText(getTimerText(airPortInfo));
			toggleTimerButtonBackground(getButtonToBeHighlighted(getTimerText(airPortInfo)));
			childLock.setChecked(getOnOffStatus(airPortInfo.getChildLock()));
			indicatorLight.setChecked(getOnOffStatus(airPortInfo.getAqiL()));
		} else {
			power.setChecked(getPowerButtonState(airPortInfo));
			disableControlPanelButtonsOnPowerOff();
		}
	}

	private int getButtonToBeHighlighted(String timer) {
		if (timer.equals(mainActivity.getString(R.string.onehour))) {
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
		FanSpeed fan = airPurifierEventDto.getFanSpeed();
		String sActualFanSpeed = airPurifierEventDto.getActualFanSpeed();
		int buttonImage = 0;
		fanSpeedAuto.setChecked(false);
		isFanSpeedAuto = false;

		switch (fan) {
		case SILENT:
			fanSpeed.setText(mainActivity.getString(R.string.silent));
			buttonImage = R.drawable.button_blue_bg_2x;
			toggleFanSpeedButtonBackground(R.id.fan_speed_silent);
			break;
		case AUTO:
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
			break;
		case TURBO:
			fanSpeed.setText(mainActivity.getString(R.string.turbo));
			buttonImage = R.drawable.button_blue_bg_2x;
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

	private boolean getPowerButtonState(AirPortInfo airportInfo) {
		isPowerOn = false;
		if (airportInfo == null) {
			return false;
		}
		powerStatus.setText(AppConstants.EMPTY_STRING);
		power.setClickable(true);
		String powerMode = airportInfo.getPowerMode();
		if (powerMode != null
				&& powerMode.equalsIgnoreCase(AppConstants.POWER_ON)) {
			enableButtonsOnPowerOn(airportInfo);
			isPowerOn = true;
		} else if (powerMode != null
				&& powerMode.equalsIgnoreCase(AppConstants.POWER_STATUS_C)) {
			disableControlPanelButtonsOnPowerOff();
			power.setClickable(false);
			powerStatus.setText(mainActivity.getString(R.string.cover_missing));
		} else if (powerMode != null
				&& powerMode.equalsIgnoreCase(AppConstants.POWER_STATUS_E)) {
			disableControlPanelButtonsOnPowerOff();
			power.setClickable(false);
			powerStatus.setText(mainActivity.getString(R.string.error));
		} else {
			disableControlPanelButtonsOnPowerOff();
		}

		return isPowerOn;
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

	private boolean getOnOffStatus(int status) {
		return AppConstants.ON == status;
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
			fanSpeed.setBackgroundResource(R.drawable.button_blue_bg_2x);
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
			fanSpeed.setBackgroundResource(R.drawable.button_blue_bg_2x);
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

		case R.id.btn_rm_set_timer:
			collapseTimerMenu(isTimerMenuVisible);
			collapseFanSpeedMenu(true);
			break;
		case R.id.timer_off:
			timer.setText(((Button) v).getText());
			toggleTimerButtonBackground(R.id.timer_off);
			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "0");
			MetricsTracker.trackActionTimerAdded("off");
			break;
		case R.id.one_hour:
			timer.setText(((Button) v).getText());
			toggleTimerButtonBackground(R.id.one_hour);
			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "1");
			MetricsTracker.trackActionTimerAdded("1hr");
			break;
		case R.id.four_hours:
			timer.setText(((Button) v).getText());
			toggleTimerButtonBackground(R.id.four_hours);
			collapseTimerMenu(true);
			controlDevice(ParserConstants.DEVICE_TIMER, "4");
			MetricsTracker.trackActionTimerAdded("4hr");
			break;
		case R.id.eight_hours:
			timer.setText(((Button) v).getText());
			toggleTimerButtonBackground(R.id.eight_hours);
			collapseTimerMenu(true);
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
			mainActivity.closeRightMenu();
			mainActivity.showFragment(new NotificationsFragment());
			break;
		default:
			break;
		}
	}

	private void toggleFanSpeedButtonBackground(int id) {
		fanSpeedOne.setBackgroundResource((id == fanSpeedOne.getId()) ? R.drawable.fan_speed_one
						: R.drawable.fan_speed_one_white);
		fanSpeedTwo.setBackgroundResource((id == fanSpeedTwo.getId()) ? R.drawable.fan_speed_two
						: R.drawable.fan_speed_two_white);
		fanSpeedThree.setBackgroundResource((id == fanSpeedThree.getId()) ? R.drawable.fan_speed_three
						: R.drawable.fan_speed_three_white);
		fanSpeedSilent.setBackgroundResource((id == fanSpeedSilent.getId()) ? R.drawable.button_blue_bg_2x
						: R.drawable.button_bg_normal_2x);
		fanSpeedSilent.setTextColor((id == fanSpeedSilent.getId()) ? Color.WHITE
						: Color.rgb(109, 109, 109));
		fanSpeedTurbo.setBackgroundResource((id == fanSpeedTurbo.getId()) ? R.drawable.button_blue_bg_2x
						: R.drawable.button_bg_normal_2x);
		fanSpeedTurbo.setTextColor((id == fanSpeedTurbo.getId()) ? Color.WHITE
				: Color.rgb(109, 109, 109));
	}

	private void toggleTimerButtonBackground(int id) {
		for (int i = 0; i < timerButtons.length; i++) {
			if (id == timerButtons[i].getId()) {
				timerButtons[i].setBackgroundResource(R.drawable.button_blue_bg_2x);
				timerButtons[i].setTextColor(Color.WHITE);

			} else {
				timerButtons[i].setBackgroundResource(R.drawable.button_bg_normal_2x);
				timerButtons[i].setTextColor(Color.rgb(109, 109, 109));
			}
		}
	}

	private void controlDevice(String key, String value) {
//		mainActivity.setVisibilityAirPortTaskProgress(View.VISIBLE);
		PurifierManager.getInstance().setPurifierDetails(key, value, PurifierEvent.DEVICE_CONTROL);
	}

	private void disableControlPanelButtonsOnPowerOff() {
		Log.i(TAG, "disableControlPanelButtonsOnPowerOff");
		Drawable disabledButton = mainActivity.getResources().getDrawable(R.drawable.button_bg_2x);
		disabledButton.setAlpha(100);

		fanSpeed.setClickable(false);
		fanSpeed.setBackgroundResource(R.drawable.button_bg_2x);
		fanSpeed.setText(mainActivity.getString(R.string.off));

		timer.setClickable(false);
		timer.setBackgroundResource(R.drawable.button_bg_2x);

		childLock.setClickable(false);
		childLock.setChecked(false);

		indicatorLight.setClickable(false);
		indicatorLight.setChecked(false);

		collapseFanSpeedMenu(true);
		collapseTimerMenu(true);
		
		disabledButton = null;
	}

	private void enableButtonsOnPowerOn(AirPortInfo airPurifierEventDto) {

		if (airPurifierEventDto != null) {
			fanSpeed.setClickable(true);
			indicatorLight.setClickable(true);
			childLock.setClickable(true);
			timer.setClickable(true);
			timer.setBackgroundResource(R.drawable.button_blue_bg_2x);

			childLock.setChecked(getOnOffStatus(airPurifierEventDto
					.getChildLock()));
			setFanSpeed(airPurifierEventDto);
			indicatorLight.setChecked(getOnOffStatus(airPurifierEventDto
					.getAqiL()));
		} else {
			fanSpeed.setClickable(false);
			indicatorLight.setClickable(false);
			childLock.setClickable(false);
			timer.setClickable(false);
		}
	}

	/**
	 * @param if true, collapse the list else expand it.
	 */
	private void collapseTimerMenu(boolean collapse) {
		if (!collapse) {
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
	 * @param if true, collapse the list else expand it.
	 */
	private void collapseFanSpeedMenu(boolean collapse) {
		if (!collapse) {
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

	public void toggleControlPanel(boolean connected,
			AirPortInfo airPurifierEventDto) {
		if (!connected) {
			power.setClickable(false);
			power.setChecked(false);
			disableControlPanelButtonsOnPowerOff();
		} else {
			// Added if enable shop demo mode, when AirPurifier already connected.
			PurAirDevice purAirDevice = PurifierManager.getInstance().getCurrentPurifier();
			if (purAirDevice == null || purAirDevice.getName() == null)
				return;

			if (airPurifierEventDto != null) {
				power.setClickable(true);
				power.setChecked(getPowerButtonState(airPurifierEventDto));
				if (isPowerOn) {
					enableButtonsOnPowerOn(airPurifierEventDto);
				}
			} else {
				disableControlPanelButtonsOnPowerOff();
			}
		}
	}
}
