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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareResponseListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerOverviewFragment extends BaseFragment implements FirmwareResponseListener{	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::onCreateView() method enter");
		View view = inflater.inflate(R.layout.scheduler_overview, null);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.OVERVIEW_EVENT);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		String extras = "";
		if (bundle != null) {
			extras = bundle.getString("events");
			CreateEventList(extras, view);
			ALog.i("ALog.SCHEDULER", "SchedulerOverview::onCreateView() method - extras is " + extras);
		}
		
		ImageView ivEdit1 = (ImageView) view.findViewById(R.id.edit);
		ivEdit1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.UPDATE_EVENT);
				/*DeleteSchedulerFragment fragEditSch = new DeleteSchedulerFragment();
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragEditSch,"DeleteSchedulerFragment").commit();*/
			}
		});
		
		ImageView ivEdit = (ImageView) view.findViewById(R.id.add);
		ivEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.CREATE_EVENT);
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
			}
		});
		
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::onCreateView() method exit");
		return view;
	}
	
	private void CreateEventList(String extras, View view) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method enter  ");
		String sIsEnabled, sTime, sDays, sProduct, sPort, sSpeed, sCommand;
		String sEventSetting = "";	
		
		try{
		JSONArray arrColl = new JSONArray(extras);
		
		for (int i=0;i<arrColl.length();i++) {
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method - printing Json object  " + i);
		    JSONObject json = arrColl.getJSONObject(i);
		    
		    sIsEnabled = json.getString(SchedulerConstants.ENABLED);
		    sTime = json.getString(SchedulerConstants.TIME);
		    
		    try {
		    sDays = json.getString(SchedulerConstants.DAYS);
		    } catch(Exception e) {
		    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting days property");
		    	sDays = "Never";
		    }
		    sDays = setWeekDays2(sDays);
		    sProduct = json.getString(SchedulerConstants.PRODUCT);
		    sPort = json.getString(SchedulerConstants.PORT);
		    
		    try {
		    	sSpeed = json.getString(SchedulerConstants.SPEED);
		    } catch (Exception e) {
		    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting speed property");
		    	sSpeed = "Auto";
		    }
		    
		    sCommand = json.getString(SchedulerConstants.COMMAND);
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
			
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Enabled value is  " + sIsEnabled);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - time value is  " + sTime);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - days value is  " + sDays);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - product value is  " + sProduct);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - port value is  " + sPort);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - speed value is  " + sSpeed);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - command value is  " + sCommand);	
		}
		} catch(Exception e) {
		}
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method exit  ");
	}
	
	private void CreateEvent(View view, int iTxtOuterView, int iTxtInnerOuterView, int iTxtView1, int iTxtView2, int iButton, int iImgView, String time, String event) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEvent() method enter");
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
		txtView2.setVisibility(View.VISIBLE);
		
		btn = (Button) view.findViewById(iButton);
		btn.setVisibility(View.VISIBLE);
		
		//imgView = (ImageView) view.findViewById(iImgView);
		//imgView.setVisibility(View.VISIBLE);
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEvent() method exit");
	}
	
	private String setWeekDays2(String days) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::setWeekDays2() method enter");
		String sWeekdays = "";
		
		if (days.equals("Never"))
			return days;
		
		String[] sParts = days.split(SchedulerConstants.DIGITS);
		String sTempStr = "";
		//int beginIndex = 0;
		
		for(int i=0; i<sParts.length;i++) {
			sTempStr = sParts[i];
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::setWeekDays2() method - sTempStr is " + sTempStr);
			if (sTempStr.contains("0")) sWeekdays = sWeekdays + SchedulerConstants.SUNDAY + ", ";
			if (sTempStr.contains("1")) sWeekdays = sWeekdays + SchedulerConstants.MONDAY + ", ";
			if (sTempStr.contains("2")) sWeekdays = sWeekdays + SchedulerConstants.TUESDAY + ", ";
			if (sTempStr.contains("3")) sWeekdays = sWeekdays + SchedulerConstants.WEDNESDAY + ", ";
			if (sTempStr.contains("4")) sWeekdays = sWeekdays + SchedulerConstants.THURSDAY + ", ";
			if (sTempStr.contains("5")) sWeekdays = sWeekdays + SchedulerConstants.FRIDAY + ", ";
			if (sTempStr.contains("6")) sWeekdays = sWeekdays + SchedulerConstants.SATURDAY + ", ";
		}
		
		sWeekdays = sWeekdays.substring(0, sWeekdays.length() - 2);
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::setWeekDays2() method exit");
		
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
