package com.philips.cl.di.dev.pa.scheduler;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareResponseListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerOverviewFragment extends BaseFragment implements FirmwareResponseListener{	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		ALog.i("Scheduler", "SchedulerOverview::onCreateView() method beginning is called");
		View view = inflater.inflate(R.layout.scheduler_overview, null);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.OVERVIEW_EVENT);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		String extras = "";
		if (bundle != null) {
			extras = bundle.getString("events");
			CreateEventList(extras, view);
			ALog.i("Scheduler", "Extras is " + extras);
		}
		
		ImageView ivEdit1 = (ImageView) view.findViewById(R.id.edit);
		ivEdit1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DeleteSchedulerFragment fragEditSch = new DeleteSchedulerFragment();
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragEditSch,"DeleteSchedulerFragment").commit();
			}
		});
		
		ImageView ivEdit = (ImageView) view.findViewById(R.id.add);
		ivEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
			}
		});
		
		ALog.i("Scheduler", "SchedulerOverview::onCreateView() method ending is called");
		return view;
	}
	
	private void CreateEventList(String extras, View view) {
		ALog.i("Scheduler", "SchedulerOverview::CreateEventList() method beginning is called  ");
		String sIsEnabled, sTime, sDays, sProduct, sPort, sSpeed, sCommand;
		String sEventSetting = "";	
		
		JsonArray arrColl = new JsonParser().parse(extras).getAsJsonArray();
		for (int i=0;i<arrColl.size();i++) {
			ALog.i("Scheduler", "Printing Json object  " + i);
		    JsonObject json = arrColl.get(i).getAsJsonObject();
		    
		    sIsEnabled = getJsonPropertyAsString(json, "enabled");
		    sTime = getJsonPropertyAsString(json, "time");
		    
		    try {
		    sDays = getJsonPropertyAsString(json, "days");
		    } catch(Exception e) {
		    	ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - Excpetion caught while getting days property");
		    	sDays = "Never";
		    }
		    sDays = setWeekDays2(sDays);
		    sProduct = getJsonPropertyAsString(json, "product");
		    sPort = getJsonPropertyAsString(json, "port");
		    
		    try {
		    	sSpeed = getJsonPropertyAsString(json, "speed");
		    } catch (Exception e) {
		    	ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - Excpetion caught while getting speed property");
		    	sSpeed = "Auto";
		    }
		    
		    sCommand = getJsonPropertyAsString(json, "command");
		    sEventSetting = "";
			//sEventSetting = sSpeed + ", " + sDays;
		    sEventSetting = sDays;
			
			switch(i) {
			case 0:
				CreateEvent(view, R.id.scheduler1_outer, R.id.scheduler1_innerouter, R.id.scheduler1_text1, R.id.scheduler1_text2, R.id.scheduler1_btnActivation, R.id.divider1, sTime, sEventSetting);
				break;
			case 1:
				CreateEvent(view, R.id.scheduler2_outer, R.id.scheduler2_innerouter, R.id.scheduler2_text1, R.id.scheduler2_text2, R.id.scheduler2_btnActivation, R.id.divider2, sTime, sEventSetting);
				break;
			case 2:
				CreateEvent(view, R.id.scheduler3_outer, R.id.scheduler3_innerouter, R.id.scheduler3_text1, R.id.scheduler3_text2, R.id.scheduler3_btnActivation, R.id.divider3, sTime, sEventSetting);
				break;
			case 3:
				CreateEvent(view, R.id.scheduler4_outer, R.id.scheduler4_innerouter, R.id.scheduler4_text1, R.id.scheduler4_text2, R.id.scheduler4_btnActivation, R.id.divider4, sTime, sEventSetting);
				break;
			default:
				break;
			}
			
			ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - Enabled value is  " + sIsEnabled);
			ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - time value is  " + sTime);
			ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - days value is  " + sDays);
			ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - product value is  " + sProduct);
			ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - port value is  " + sPort);
			ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - speed value is  " + sSpeed);
			ALog.i("Scheduler", "SchedulerOverview::CreateEventList method - command value is  " + sCommand);	
		}
		ALog.i("Scheduler", "SchedulerOverview::CreateEventList() method ending is called  ");
	}
	
	private void CreateEvent(View view, int iTxtOuterView, int iTxtInnerOuterView, int iTxtView1, int iTxtView2, int iButton, int iImgView, String time, String event) {
		ALog.i("Scheduler", "SchedulerOverview::CreateEvent() method beginning is called  ");
		FontTextView txtOuterView, txtInnerOuterView;
		FontTextView txtView1, txtView2;
		Button btn;
		//ImageView imgView;
		
		txtOuterView = (FontTextView) view.findViewById(iTxtOuterView);
		txtOuterView.setVisibility(View.VISIBLE);
		
		txtInnerOuterView = (FontTextView) view.findViewById(iTxtInnerOuterView);
		txtInnerOuterView.setVisibility(View.VISIBLE);
		
		txtView1 = (FontTextView) view.findViewById(iTxtView1);
		txtView1.setText(time);
		txtView1.setVisibility(View.VISIBLE);
		
		txtView2 = (FontTextView) view.findViewById(iTxtView2);
		txtView2.setText(event);		
		//txtView2.setTypeface(null, Typeface.BOLD);
		txtView2.setVisibility(View.VISIBLE);
		
		btn = (Button) view.findViewById(iButton);
		btn.setVisibility(View.VISIBLE);
		
		//imgView = (ImageView) view.findViewById(iImgView);
		//imgView.setVisibility(View.VISIBLE);
		ALog.i("Scheduler", "SchedulerOverview::CreateEvent() method ending is called  ");
	}
	
	/*private String setWeekDays(String days) {
		ALog.i("Scheduler", "SchedulerOverview::setWeekDays() method beginning is called  ");
		
		if (days.equals("Never"))
			return "Never";
		
		String[] sParts = days.split("0123456");
		String sTempStr = "";
		String sWeekdays = "";
		
		for(int i=0; i<sParts.length;i++) {
			sTempStr = sParts[i];
			if (sTempStr.contains("0")) sWeekdays = sWeekdays + " " + "Sun";
			if (sTempStr.contains("1")) sWeekdays = sWeekdays + " " + "Mon";
			if (sTempStr.contains("2")) sWeekdays = sWeekdays + " " + "Tue";
			if (sTempStr.contains("3")) sWeekdays = sWeekdays + " " + "Wed";
			if (sTempStr.contains("4")) sWeekdays = sWeekdays + " " + "Thu";
			if (sTempStr.contains("5")) sWeekdays = sWeekdays + " " + "Fri";
			if (sTempStr.contains("6")) sWeekdays = sWeekdays + " " + "Sat";
		}
		
		ALog.i("Scheduler", "SchedulerOverview::setWeekDays() method ending is called  ");
		return sWeekdays;
	}*/
	
	
	private String setWeekDays2(String days) {
		ALog.i("Scheduler", "SchedulerOverview::setWeekDays2() method beginning is called  ");
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
		ALog.i("Scheduler", "SchedulerOverview::setWeekDays2() method ending is called  ");
		
		return sWeekdays;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void firmwareDataRecieved(String data) {
	}

	public void processFirmwareData(JsonObject jsonObject) { 	}
	
	public String getJsonPropertyAsString(JsonObject jsonObject, String property) {
		JsonElement progressElemt = jsonObject.get(property);
		return progressElemt.getAsString();
	}
}
