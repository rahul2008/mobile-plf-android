package com.philips.cl.di.dev.pa.scheduler;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AddSchedulerFragment extends BaseFragment implements OnClickListener {

	private String sSelectedTime = "";
	private String sSelectedDays = "";
	private String sSelectedFanspeed = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_scheduler, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.ADD_EVENT);
		initViews(getView());
	}

	private void initViews(View view) {
		if (getActivity() == null) return;
		sSelectedTime = getArguments().getString(SchedulerConstants.TIME);
		sSelectedDays = getArguments().getString(SchedulerConstants.DAYS);
		sSelectedFanspeed = getArguments().getString(SchedulerConstants.SPEED);
		
		if (sSelectedDays == null || sSelectedDays.isEmpty() ) {
			((SchedulerActivity)getActivity()).setDays(SchedulerConstants.ONE_TIME);
		}
		
		if (sSelectedFanspeed == null || sSelectedFanspeed.isEmpty()) {
			((SchedulerActivity)getActivity()).setFanSpeed(SchedulerConstants.DEFAULT_FANSPEED_SCHEDULER);
		}
		
		sSelectedDays = setWeekDays(sSelectedDays);
		
		FontTextView tvAddTime = (FontTextView) view.findViewById(R.id.tvAddTime);
		tvAddTime.setOnClickListener(this);
		tvAddTime.setTypeface(null, Typeface.BOLD);
		FontTextView txtView = (FontTextView) view.findViewById(R.id.tvAddSchdeduler);
		txtView.setText(sSelectedTime);
		
		FontTextView tvRepeat = (FontTextView) view.findViewById(R.id.repeat);
		tvRepeat.setOnClickListener(this);
		tvRepeat.setTypeface(null, Typeface.BOLD);
		FontTextView tvFanSpeed = (FontTextView) view.findViewById(R.id.fanspeed);
		tvFanSpeed.setOnClickListener(this);
		tvFanSpeed.setTypeface(null, Typeface.BOLD);
		
		FontTextView repeat_text = (FontTextView) view.findViewById(R.id.repeattext);
		if(sSelectedDays != null && !sSelectedDays.isEmpty()) {
			repeat_text.setText(sSelectedDays);
		}
		
		FontTextView fanspeed_text = (FontTextView) view.findViewById(R.id.fanspeedtext);
		if(sSelectedFanspeed != null && !sSelectedFanspeed.isEmpty()) {
			fanspeed_text.setText(SchedulerUtil.getFanspeedName(sSelectedFanspeed));
		}
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
		Bundle bundle = new Bundle();
		switch(v.getId()) {
			case R.id.tvAddTime:
			try {
				android.support.v4.app.DialogFragment newFragment = new TimePickerFragment();
				((TimePickerFragment) newFragment).setTime(sSelectedTime);
				newFragment.show(getActivity().getSupportFragmentManager(), SchedulerConstants.TIMER_FRAGMENT_TAG);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			break;
			case R.id.repeat:
				bundle.putString(SchedulerConstants.DAYS, sSelectedDays);
				RepeatFragment fragRepeat = new RepeatFragment();
				fragRepeat.setArguments(bundle);
				showFragment(fragRepeat, SchedulerConstants.REPEAT_FRAGMENT_TAG);
				break;
			case R.id.fanspeed:
				bundle.putString(SchedulerConstants.SPEED, sSelectedFanspeed);
				FanspeedFragment fragFanSpeed = new FanspeedFragment();
				fragFanSpeed.setArguments(bundle);
				showFragment(fragFanSpeed, SchedulerConstants.FANSPEED_FRAGMENT_TAG);
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
			ALog.e(ALog.SCHEDULER, e.getMessage());
		}
	}
}
