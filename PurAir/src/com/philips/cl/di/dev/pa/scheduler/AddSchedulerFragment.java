package com.philips.cl.di.dev.pa.scheduler;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AddSchedulerFragment extends BaseFragment implements OnClickListener, 
	OnCheckedChangeListener {

	private String sSelectedTime = "";
	private String sSelectedDays = "";
	private String sSelectedFanspeed = "";
	private boolean enabled ;
	private TimePicker timePicker;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_scheduler, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.ADD_SCHEDULE);
		initViews(getView());		
	}
	
	@Override
	public void onResume() {
		setTimePickerTime();
		super.onResume();
	}

	private void initViews(View view) {
		if (getActivity() == null) return;
		
		ImageView backImageView = (ImageView) getView().findViewById(R.id.scheduler_back_img);
		backImageView.setOnClickListener(this);
		FontButton saveBtn = (FontButton) getView().findViewById(R.id.scheduler_save_btn);
		saveBtn.setVisibility(View.VISIBLE);
		saveBtn.setOnClickListener(this);
		
		FontTextView headingTV = (FontTextView) getView().findViewById(R.id.scheduler_heading_tv);
		timePicker = (TimePicker) view.findViewById(R.id.scheduler_timePicker);
		timePicker.setIs24HourView(true);
		
		sSelectedTime = getArguments().getString(SchedulerConstants.TIME);
		sSelectedDays = getArguments().getString(SchedulerConstants.DAYS);
		sSelectedFanspeed = getArguments().getString(SchedulerConstants.SPEED);
		enabled = getArguments().getBoolean(SchedulerConstants.ENABLED) ;
		String heading = getArguments().getString(SchedulerConstants.HEADING);
		
		if (heading != null) {
			headingTV.setText(heading);
		}
		
		if (sSelectedDays == null || sSelectedDays.isEmpty() ) {
			((SchedulerActivity)getActivity()).setDays(SchedulerConstants.ONE_TIME);
		}
		
		if (sSelectedFanspeed == null || sSelectedFanspeed.isEmpty()) {
			((SchedulerActivity)getActivity()).setFanSpeed(SchedulerConstants.DEFAULT_FANSPEED_SCHEDULER);
		}
		
		sSelectedDays = setWeekDays(sSelectedDays);
		
		ViewGroup tvRepeat = (RelativeLayout) view.findViewById(R.id.repeat_rl);
		tvRepeat.setOnClickListener(this);
		ViewGroup tvFanSpeed = (RelativeLayout) view.findViewById(R.id.fanspeed_rl);
		tvFanSpeed.setOnClickListener(this);
		
		FontTextView repeat_text = (FontTextView) view.findViewById(R.id.repeattext);
		if(sSelectedDays != null && !sSelectedDays.isEmpty()) {
			repeat_text.setText(sSelectedDays);
		}
		
		FontTextView fanspeed_text = (FontTextView) view.findViewById(R.id.fanspeedtext);
		if(sSelectedFanspeed != null && !sSelectedFanspeed.isEmpty()) {
			fanspeed_text.setText(SchedulerUtil.getFanspeedName(sSelectedFanspeed));
		}
		ToggleButton enableDisableBtn = (ToggleButton) view.findViewById(R.id.enable_schedule_toggle);
		enableDisableBtn.setChecked(enabled);
		enableDisableBtn.setOnCheckedChangeListener(this);
	}
	
	private void setTimePickerTime() {
		int hour = 0;
		int min = 0;
		if (sSelectedTime == null || sSelectedTime.isEmpty() ) {
			Calendar calendar = Calendar.getInstance();
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			min = calendar.get(Calendar.MINUTE);
		} else {
			String[] times = sSelectedTime.split(":"); // HH:mm
			if (times.length == 2) {
				try {
					hour = Integer.parseInt(times[0].trim());
					min = Integer.parseInt(times[1].trim());
				} catch (NumberFormatException e) {
					ALog.e(ALog.ERROR, "NumberFormatException");
				}
			}
		}
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(min);
	}
	
	public void setTime(String time) {
		sSelectedTime = time;
	}
	
	private String setWeekDays(String days) {
		ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::setWeekDays() method enter: " + days);
		StringBuffer stringBuffer = new StringBuffer();
		if (days == null || days.isEmpty()) return SchedulerConstants.ONE_TIME;
			
		if (days.equals(SchedulerConstants.ONE_TIME)) return days;
		char[] dayCharArray = days.toCharArray();
		if(dayCharArray != null) {
			int charArrayLengh = dayCharArray.length ;
			for (int index = 0 ; index < charArrayLengh ; index ++) {
				switch (dayCharArray[index]) { 
				case SchedulerConstants.CHAR_ZERO:
					stringBuffer.append(SchedulerConstants.SUNDAY);
					break;
				case SchedulerConstants.CHAR_ONE:
					stringBuffer.append(SchedulerConstants.MONDAY);
					break;
				case SchedulerConstants.CHAR_TWO:
					stringBuffer.append(SchedulerConstants.TUESDAY);
					break;
				case SchedulerConstants.CHAR_THREE:
					stringBuffer.append(SchedulerConstants.WEDNESDAY);
					break;
				case SchedulerConstants.CHAR_FOUR:
					stringBuffer.append(SchedulerConstants.THURSDAY);
					break;
				case SchedulerConstants.CHAR_FIVE:
					stringBuffer.append(SchedulerConstants.FRIDAY);
					break;
				case SchedulerConstants.CHAR_SIX:
					stringBuffer.append(SchedulerConstants.SATURDAY);
					break;
				default:
					ALog.i(ALog.SCHEDULER, "Default");
					break;
					
				}
				if( index != charArrayLengh - 1 ) {
					stringBuffer.append(", ") ;
				}
			}
		}
		return stringBuffer.toString();
	}

	@Override
	public void onClick(View v) {
		SchedulerActivity activity = (SchedulerActivity) getActivity();
		activity.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		Bundle bundle = new Bundle();
		switch(v.getId()) {
			case R.id.repeat_rl:
				bundle.putString(SchedulerConstants.DAYS, sSelectedDays);
				RepeatFragment fragRepeat = new RepeatFragment();
				fragRepeat.setArguments(bundle);
				showFragment(fragRepeat, SchedulerConstants.REPEAT_FRAGMENT_TAG);
				break;
			case R.id.fanspeed_rl:
				bundle.putString(SchedulerConstants.SPEED, sSelectedFanspeed);
				FanspeedFragment fragFanSpeed = new FanspeedFragment();
				fragFanSpeed.setArguments(bundle);
				showFragment(fragFanSpeed, SchedulerConstants.FANSPEED_FRAGMENT_TAG);
				break;
			case R.id.scheduler_back_img:
				if (activity != null) {
					activity.onBackPressed();
				}
				break;
			case R.id.scheduler_save_btn:
				if (activity != null) {
					activity.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
					activity.save();
				}
				break;
			default:
				break;
		}
	}	
	
	private void showFragment(Fragment fragment, String fragTag) {
		try {
			getFragmentManager().beginTransaction()
			.replace(R.id.ll_scheduler_container, fragment, fragTag).commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.SCHEDULER, "Error: " + e.getMessage());
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() == R.id.enable_schedule_toggle) {
			if(getActivity() != null ) {
				((SchedulerActivity) getActivity()).isEnabled(isChecked);
			}
		}
		
	}
}
