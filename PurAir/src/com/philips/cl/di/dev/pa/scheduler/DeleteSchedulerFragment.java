package com.philips.cl.di.dev.pa.scheduler;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareResponseListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DeleteSchedulerFragment extends BaseFragment implements FirmwareResponseListener{
	//private String selectedTime;
	//private String selectedDays;
	//private String selectedFanspeed;
	private JSONArray schedulersList;
	ImageView ivRhtArr1, ivRhtArr2, ivRhtArr3, ivRhtArr4; 
	Button deleteconf1, deleteconf2, deleteconf3, deleteconf4;
	ImageView add, edit;
	ImageView delete1, delete2, delete3, delete4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onCreateView() method enter");
		View view = inflater.inflate(R.layout.delete_scheduler, null);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.DELETE_EVENT);
		Bundle bundle = getActivity().getIntent().getExtras();
		String extras = "";
		if (bundle != null) {
			extras = bundle.getString("events");
			CreateEventList(extras, view);
			ALog.i("Scheduler", "Extras is " + extras);
		}
		schedulersList = ((SchedulerActivity) getActivity()).getSchedulerList();
		initViews(view);
		
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onCreateView() method exit");
		return view;
	}
	
	private void initViews(View view) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::initViews() method enter");
	
		edit = (ImageView) view.findViewById(R.id.edit);
		edit.setOnClickListener(onClickListener);
		add = (ImageView) view.findViewById(R.id.add);
		add.setOnClickListener(onClickListener);
		ivRhtArr1 = (ImageView) view.findViewById(R.id.RightArrow1);
		ivRhtArr1.setOnClickListener(onClickListener);
		ivRhtArr2 = (ImageView) view.findViewById(R.id.RightArrow2);
		ivRhtArr2.setOnClickListener(onClickListener);
		ivRhtArr3 = (ImageView) view.findViewById(R.id.RightArrow3);
		ivRhtArr3.setOnClickListener(onClickListener);
		ivRhtArr4 = (ImageView) view.findViewById(R.id.RightArrow4);
		ivRhtArr4.setOnClickListener(onClickListener);
		delete1 = (ImageView) view.findViewById(R.id.delete1);
		delete1.setOnClickListener(onClickListener);
		delete2 = (ImageView) view.findViewById(R.id.delete2);
		delete2.setOnClickListener(onClickListener);
		delete3 = (ImageView) view.findViewById(R.id.delete3);
		delete3.setOnClickListener(onClickListener);
		delete4 = (ImageView) view.findViewById(R.id.delete4);
		delete4.setOnClickListener(onClickListener);
		deleteconf1 = (Button) view.findViewById(R.id.deleteconfirm1);
		deleteconf1.setOnClickListener(onClickListener);
		deleteconf2 = (Button) view.findViewById(R.id.deleteconfirm2);
		deleteconf2.setOnClickListener(onClickListener);
		deleteconf3 = (Button) view.findViewById(R.id.deleteconfirm3);
		deleteconf3.setOnClickListener(onClickListener);
		deleteconf4 = (Button) view.findViewById(R.id.deleteconfirm4);
		deleteconf4.setOnClickListener(onClickListener);
		double viewWidth = (double) view.getWidth();
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::initViews() method - View width is - " + viewWidth);
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::initViews() method exit");
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method enter");
			
			switch (v.getId()) {
				case R.id.RightArrow1:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow1 is invoked");
					editSchededuler(0);
					delete1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					deleteconf1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					break;
				case R.id.RightArrow2:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow2 is invoked");
					editSchededuler(1);
					break;
				case R.id.RightArrow3:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow3 is invoked");
					editSchededuler(2);
					break;
				case R.id.RightArrow4:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow4 is invoked");
					editSchededuler(3);
					break;
				case R.id.edit:
					showSchedulerOverviewFragment();
					break;
				case R.id.add:
					((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.CREATE_EVENT);
					DialogFragment newFragment = new TimePickerFragment();
					newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
					break;
				case R.id.delete1:
					ivRhtArr1.setVisibility(View.INVISIBLE);
					deleteconf1.setVisibility(View.VISIBLE);
					break;
				case R.id.delete2:
					ivRhtArr2.setVisibility(View.INVISIBLE);
					deleteconf2.setVisibility(View.VISIBLE);
					break;
				case R.id.delete3:
					ivRhtArr3.setVisibility(View.INVISIBLE);
					deleteconf3.setVisibility(View.VISIBLE);
					break;
				case R.id.delete4:
					ivRhtArr4.setVisibility(View.INVISIBLE);
					deleteconf4.setVisibility(View.VISIBLE);
					break;
				case R.id.deleteconfirm1:
					refreshDeleteSchedulerPage(0);
					break;
				case R.id.deleteconfirm2:
					refreshDeleteSchedulerPage(1);
					break;
				case R.id.deleteconfirm3:
					refreshDeleteSchedulerPage(2);
					break;
				case R.id.deleteconfirm4:
					refreshDeleteSchedulerPage(3);
					break;
			}
			ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method exit");
		}
	};
	
	private void refreshDeleteSchedulerPage(int index) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::refreshDeleteSchedulerPage() method enter");
		((SchedulerActivity)getActivity()).deleteScheduler(index);
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::refreshDeleteSchedulerPage() method exit");
	}
	
	private void editSchededuler(int index) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::editSchededuler() method enter");
		Bundle bundle = new Bundle();
		try{
			((SchedulerActivity)getActivity()).dispatchInformationsForCRUDIndex(index);
			JSONObject obj = schedulersList.getJSONObject(index);
			bundle.putString("time", obj.getString("time"));
			bundle.putString("days", obj.getString("days"));
			bundle.putString("speed", obj.getString("speed"));		
			
			AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
			fragAddSch.setArguments(bundle);
			getFragmentManager()
	  	    .beginTransaction()
	  		.replace(R.id.ll_scheduler_container, fragAddSch, "AddSchedulerFragment")
	  		.commit();
		}
		catch (Exception e) {
		}
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::editSchededuler() method exit");
	}
	
	private void CreateEventList(String extras, View view) {
		ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method enter");
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
		    	ALog.i("Scheduler", "SDeleteSchedulerFragment::CreateEventList() method - Excpetion caught while getting days property");
		    	sDays = "Never";
		    }
		    
		    sDays = setWeekDays2(sDays);
		    sProduct = getJsonPropertyAsString(json, "product");
		    sPort = getJsonPropertyAsString(json, "port");
		    
		    try {
		    	sSpeed = getJsonPropertyAsString(json, "speed");
		    } catch (Exception e) {
		    	ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - Excpetion caught while getting speed property");
		    	sSpeed = "Auto";
		    }
		    
		    sCommand = getJsonPropertyAsString(json, "command");
		    sEventSetting = "";
			//sEventSetting = sSpeed + ", " + sDays;
		    sEventSetting = sDays;
		    
		    ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - Enabled value is  " + sIsEnabled);
			ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - time value is  " + sTime);
			ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - days value is  " + sDays);
			ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - product value is  " + sProduct);
			ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - port value is  " + sPort);
			ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - speed value is  " + sSpeed);
			ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method - command value is  " + sCommand);	
			
			switch(i) {
			case 0:
				CreateEvent(view, R.id.event1, R.id.scheduler1_outer, R.id.scheduler1_innerouter, R.id.scheduler1_text1, R.id.scheduler1_text2, R.id.RightArrow1, R.id.divider1, R.id.delete1, sTime, sEventSetting);
				break;
			case 1:
				CreateEvent(view, R.id.event2, R.id.scheduler2_outer, R.id.scheduler2_innerouter, R.id.scheduler2_text1, R.id.scheduler2_text2, R.id.RightArrow2, R.id.divider2, R.id.delete2, sTime, sEventSetting);
				break;
			case 2:
				CreateEvent(view, R.id.event3, R.id.scheduler3_outer, R.id.scheduler3_innerouter, R.id.scheduler3_text1, R.id.scheduler3_text2, R.id.RightArrow3, R.id.divider3, R.id.delete3, sTime, sEventSetting);
				break;
			case 3:
				CreateEvent(view, R.id.event4, R.id.scheduler4_outer, R.id.scheduler4_innerouter, R.id.scheduler4_text1, R.id.scheduler4_text2, R.id.RightArrow4, R.id.divider4, R.id.delete4, sTime, sEventSetting);
				break;
			default:
				break;
			}
		}
		ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method exit");
	}
	
	private void CreateEvent(View view, int layout, int iTxtOuterView, int iTxtInnerOuterView, int iTxtView1, int iTxtView2, int iImageView, int iImgView, int delete, String time, String event) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() method enter");
		FontTextView txtOuterView, txtInnerOuterView;
		FontTextView txtView1, txtView2;
		ImageView ivImage;
		//ImageView imgView;
		ImageView ivDelete;
		LinearLayout lLayout;
		
		lLayout = (LinearLayout) view.findViewById(layout);
		lLayout.setVisibility(View.VISIBLE);
		
		ivDelete = (ImageView) view.findViewById(delete);
		ivDelete.setVisibility(View.VISIBLE);
		
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
		
		ivImage = (ImageView) view.findViewById(iImageView);
		ivImage.setVisibility(View.VISIBLE);
		
		
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() method exit");
	}
	
	private String setWeekDays2(String days) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::setWeekDays2() method enter");
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
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::setWeekDays2() method exit");
		
		return sWeekdays;
	}
	
	private void showSchedulerOverviewFragment() {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::showSchedulerOverviewFragment() method enter");
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::showSchedulerOverviewFragment() method exit");
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

	public void processFirmwareData(JsonObject jsonObject) { }
	
	public String getJsonPropertyAsString(JsonObject jsonObject, String property) {
		JsonElement progressElemt = jsonObject.get(property);
		return progressElemt.getAsString();
	}
}
