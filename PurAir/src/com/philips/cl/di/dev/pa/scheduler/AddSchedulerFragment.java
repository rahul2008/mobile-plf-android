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

public class AddSchedulerFragment extends BaseFragment {

	private String sSelectedTime = "";
	private String sSelectedDays = "";
	private String sSelectedFanspeed = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i("Scheduler", "AddSchedulerFragment::onCreateView method beginning is called");
		sSelectedTime = getArguments().getString("time");
		sSelectedDays = getArguments().getString("days");
		sSelectedFanspeed = getArguments().getString("fanspeed");
		View view = inflater.inflate(R.layout.add_scheduler, null);
		initViews(view);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.ADD_EVENT);
		ALog.i("Scheduler", "AddSchedulerFragment::onCreateView method ending is called");
		return view;
	}

	private void initViews(View view) {
		ALog.i("Scheduler", "AddSchedulerFragment::initViews method beginning is called");
		
		if ((sSelectedDays == null) && (sSelectedFanspeed == null)) {
			ALog.i("Scheduler", "AddSchedulerFragment::initViews method - Both Time and Fanspeed are null");
			((SchedulerActivity)getActivity()).dispatchInformations("Never");
			((SchedulerActivity)getActivity()).dispatchInformations2("Auto");
		}
		
		if (sSelectedDays != null) { 
			ALog.i("Scheduler", "AddSchedulerFragment::initViews method - Days is null");
			sSelectedDays = setWeekDays2(sSelectedDays);
		}
		
		FontTextView tvAddTime = (FontTextView) view.findViewById(R.id.tvAddTime);
		tvAddTime.setTypeface(null, Typeface.BOLD);
		FontTextView txtView = (FontTextView) view.findViewById(R.id.tvAddSchdeduler);
		txtView.setText(sSelectedTime);
		
		ImageView btnTime = (ImageView) view.findViewById(R.id.btnTime);
		btnTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				android.support.v4.app.DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
			}
		});
		
		FontTextView tvRepeat = (FontTextView) view.findViewById(R.id.repeat);
		tvRepeat.setTypeface(null, Typeface.BOLD);
		FontTextView tvFanSpeed = (FontTextView) view.findViewById(R.id.fanspeed);
		tvFanSpeed.setTypeface(null, Typeface.BOLD);
		
		FontTextView repeat_text = (FontTextView) view.findViewById(R.id.repeattext);
		if(sSelectedDays != null && !sSelectedDays.isEmpty()) {
			repeat_text.setText(sSelectedDays);
		}
		
		ImageView btnRepeat = (ImageView) view.findViewById(R.id.btnrepeat);
		btnRepeat.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ALog.i("Scheduler", "btnRepeat is pressed ");
				getFragmentManager()
		  	    .beginTransaction()
		  		.replace(R.id.ll_scheduler_container, new RepeatFragment(), "RepeatFragment")
		  		.commit();
			    
			}
		});
		
		FontTextView fanspeed_text = (FontTextView) view.findViewById(R.id.fanspeedtext);
		if(sSelectedFanspeed != null && !sSelectedFanspeed.isEmpty()) {
			fanspeed_text.setText(sSelectedFanspeed);
		}
		
		ImageView btnFanSpeed = (ImageView) view.findViewById(R.id.btnFanSpeed);
		btnFanSpeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ALog.i("Scheduler", "Fan Speed button is pressed ");
				getFragmentManager()
		  	    .beginTransaction()
		  		.replace(R.id.ll_scheduler_container, new FanspeedFragment(), "FanspeedFragment")
		  		.commit();
			}
		});
		ALog.i("Scheduler", "AddSchedulerFragment::initViews method ending is called");
	}
	
	public void setTime(String time) {
		sSelectedTime = time;
	}
	
	/*private String setWeekDays(String days) {
		String sWeekdays = "";
		ALog.i("Scheduler", "setWeekDays method is called - days is " + "'" + days + "'");
		String[] sParts = days.split("0123456");
		String sTempStr = "";
		
		for(int i=0; i<sParts.length;i++) {
			sTempStr = sParts[i];
			if (sTempStr.contains("0")) sWeekdays = sWeekdays + "Sunday, ";
			if (sTempStr.contains("1")) sWeekdays = sWeekdays + "Monday, ";
			if (sTempStr.contains("2")) sWeekdays = sWeekdays + "Tuesday, ";
			if (sTempStr.contains("3")) sWeekdays = sWeekdays + "Wednesday, ";
			if (sTempStr.contains("4")) sWeekdays = sWeekdays + "Thursday, ";
			if (sTempStr.contains("5")) sWeekdays = sWeekdays + "Friday, ";
			if (sTempStr.contains("6")) sWeekdays = sWeekdays + "Saturday, ";
		}
		
		sWeekdays = sWeekdays.substring(0, sWeekdays.length() - 2);
		
		return sWeekdays;
	}*/
	
	private String setWeekDays2(String days) {
		ALog.i("Scheduler", "AddSchedulerFragment::setWeekDays2() method is called ");
		String sWeekdays = "";
		
		if (days.equals("Never"))
			return days;
		
		String[] sParts = days.split("0123456789");
		String sTempStr = "";
		//int beginIndex = 0;
		
		for(int i=0; i<sParts.length;i++) {
			sTempStr = sParts[i];
			ALog.i("Scheduler", "setWeekDays2 method - inside" + sTempStr);
			if (sTempStr.contains("0")) sWeekdays = sWeekdays + "Sunday, ";
			if (sTempStr.contains("1")) sWeekdays = sWeekdays + "Monday, ";
			if (sTempStr.contains("2")) sWeekdays = sWeekdays + "Tuesday, ";
			if (sTempStr.contains("3")) sWeekdays = sWeekdays + "Wednesday, ";
			if (sTempStr.contains("4")) sWeekdays = sWeekdays + "Thursday, ";
			if (sTempStr.contains("5")) sWeekdays = sWeekdays + "Friday, ";
			if (sTempStr.contains("6")) sWeekdays = sWeekdays + "Saturday, ";
		}
		
		sWeekdays = sWeekdays.substring(0, sWeekdays.length() - 2);
		ALog.i("Scheduler", "AddSchedulerFragment::setWeekDays2() method ending is called ");
		
		return sWeekdays;
	}	
}

