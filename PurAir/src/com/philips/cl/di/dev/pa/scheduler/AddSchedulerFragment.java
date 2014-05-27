package com.philips.cl.di.dev.pa.scheduler;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AddSchedulerFragment extends BaseFragment implements OnClickListener {

	private String sSelectedTime = "";
	private String sSelectedDays = "";
	private String sSelectedFanspeed = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View view = inflater.inflate(R.layout.add_scheduler, null);
		initViews(view);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.ADD_EVENT);
		return view;
	}

	private void initViews(View view) {
		
		try{
			sSelectedTime = getArguments().getString(SchedulerConstants.TIME);
			sSelectedDays = getArguments().getString(SchedulerConstants.DAYS);
			sSelectedFanspeed = getArguments().getString(SchedulerConstants.SPEED);
			ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::initViews() method - sSelectedTime is " + sSelectedTime);
			ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::initViews() method - sSelectedDays is " + sSelectedDays);
			ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::initViews() method - sSelectedFanspeed is " + sSelectedFanspeed);
		}
		catch(Exception e) {
			ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::initViews() method - exception caught while getting property");
			ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::initViews() method - exception caught - sSelectedTime is " + sSelectedTime);
			ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::initViews() method - exception caught - sSelectedDays is " + sSelectedDays);
			ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::initViews() method - exception caught - sSelectedFanspeed is " + sSelectedFanspeed);
		}
		
		if ((sSelectedDays == null) && (sSelectedFanspeed == null)) {
			((SchedulerActivity)getActivity()).dispatchInformations(SchedulerConstants.ONE_TIME);
			((SchedulerActivity)getActivity()).dispatchInformations2("Auto");
		}
		
		if (sSelectedDays != null) { 
			sSelectedDays = setWeekDays2(sSelectedDays);
		}
		
		FontTextView tvAddTime = (FontTextView) view.findViewById(R.id.tvAddTime);
		tvAddTime.setOnClickListener(this);
		tvAddTime.setTypeface(null, Typeface.BOLD);
		FontTextView txtView = (FontTextView) view.findViewById(R.id.tvAddSchdeduler);
		txtView.setText(sSelectedTime);
		
		ImageView btnTime = (ImageView) view.findViewById(R.id.btnTime);
		btnTime.setOnClickListener(this);
		/*btnTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				android.support.v4.app.DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
			}
		});*/
		
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
		
		ImageView btnRepeat = (ImageView) view.findViewById(R.id.btnrepeat);
		btnRepeat.setOnClickListener(this);
		/*btnRepeat.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(SchedulerConstants.DAYS, sSelectedDays);
				RepeatFragment fragRepeat = new RepeatFragment();
				fragRepeat.setArguments(bundle);
				getFragmentManager()
		  	    .beginTransaction()
		  		.replace(R.id.ll_scheduler_container, fragRepeat, "RepeatFragment")
		  		.commit();
			    
			}
		});*/
		
		FontTextView fanspeed_text = (FontTextView) view.findViewById(R.id.fanspeedtext);
		if(sSelectedFanspeed != null && !sSelectedFanspeed.isEmpty()) {
			fanspeed_text.setText(sSelectedFanspeed);
		}
		
		ImageView btnFanSpeed = (ImageView) view.findViewById(R.id.btnFanSpeed);
		/*btnFanSpeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(SchedulerConstants.TIME, sSelectedFanspeed);
				FanspeedFragment fragFanSpeed = new FanspeedFragment();
				fragFanSpeed.setArguments(bundle);
				
				getFragmentManager()
		  	    .beginTransaction()
		  		.replace(R.id.ll_scheduler_container, fragFanSpeed, "FanspeedFragment")
		  		.commit();
			}
		});*/
	}
	
	public void setTime(String time) {
		sSelectedTime = time;
	}
	
	private String setWeekDays2(String days) {
		ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::setWeekDays2() method enter");
		String sWeekdays = "";
		
		if (days.equals(SchedulerConstants.ONE_TIME))
			return days;
		
		String[] sParts = days.split(SchedulerConstants.DIGITS);
		String sTempStr = "";
		//int beginIndex = 0;
		
		for(int i=0; i<sParts.length;i++) {
			sTempStr = sParts[i];
			if (sTempStr.contains("0")) sWeekdays = sWeekdays + SchedulerConstants.SUNDAY + ", ";
			if (sTempStr.contains("1")) sWeekdays = sWeekdays + SchedulerConstants.MONDAY + ", ";
			if (sTempStr.contains("2")) sWeekdays = sWeekdays + SchedulerConstants.TUESDAY + ", ";
			if (sTempStr.contains("3")) sWeekdays = sWeekdays + SchedulerConstants.WEDNESDAY + ", ";
			if (sTempStr.contains("4")) sWeekdays = sWeekdays + SchedulerConstants.THURSDAY + ", ";
			if (sTempStr.contains("5")) sWeekdays = sWeekdays + SchedulerConstants.FRIDAY + ", ";
			if (sTempStr.contains("6")) sWeekdays = sWeekdays + SchedulerConstants.SATURDAY + ", ";
		}
		
		if (!sWeekdays.isEmpty()) {
			sWeekdays = sWeekdays.substring(0, sWeekdays.length() - 2);
		}
		
		ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::setWeekDays2() method sWeekdays is " + sWeekdays);
		ALog.i(ALog.SCHEDULER, "AddSchedulerFragment::setWeekDays2() method exit");
		return sWeekdays;
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch(v.getId()) {
			case R.id.tvAddTime:
			case R.id.btnTime:
				android.support.v4.app.DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
				break;
			case R.id.repeat:
			case R.id.btnrepeat:
				bundle.putString(SchedulerConstants.DAYS, sSelectedDays);
				RepeatFragment fragRepeat = new RepeatFragment();
				fragRepeat.setArguments(bundle);
				getFragmentManager()
		  	    .beginTransaction()
		  		.replace(R.id.ll_scheduler_container, fragRepeat, "RepeatFragment")
		  		.commit();
				break;
			case R.id.fanspeed:
			case R.id.btnFanSpeed:
				bundle.putString(SchedulerConstants.TIME, sSelectedFanspeed);
				FanspeedFragment fragFanSpeed = new FanspeedFragment();
				fragFanSpeed.setArguments(bundle);
				
				getFragmentManager()
		  	    .beginTransaction()
		  		.replace(R.id.ll_scheduler_container, fragFanSpeed, "FanspeedFragment")
		  		.commit();
				break;
			default:
				break;
		}
		
	}	
}

