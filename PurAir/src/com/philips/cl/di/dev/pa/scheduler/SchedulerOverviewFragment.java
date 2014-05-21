package com.philips.cl.di.dev.pa.scheduler;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class SchedulerOverviewFragment extends BaseFragment implements FirmwareResponseListener{
	
	ImageView ivEdit, ivDelete1, ivDelete2, ivDelete3, ivDelete4, ivDelete5;
	ImageView ivRhtArr1, ivRhtArr2, ivRhtArr3, ivRhtArr4, ivRhtArr5;
	FontTextView tvRgtArrTxt1, tvRgtArrTxt2, tvRgtArrTxt3, tvRgtArrTxt4, tvRgtArrTxt5;
	TextView tvEditTxt;
	FontTextView tvRgtArrTxt;
	FontTextView tvScheduler1_text2, tvScheduler2_text2, tvScheduler3_text2, tvScheduler4_text2, tvScheduler5_text2;
	FontTextView tvScheduler1_text1, tvScheduler2_text1, tvScheduler3_text1, tvScheduler4_text1, tvScheduler5_text1;	
	JsonArray arrColl;
	int iSchedulersCount;
	private JSONArray schedulersList;
	List<Integer> lstSchedulerMarkedDel = new ArrayList<Integer>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::onCreateView() method enter");
		View view = inflater.inflate(R.layout.scheduler_overview, null);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.OVERVIEW_EVENT);
		
		iSchedulersCount = 0;
		
		Bundle bundle = getActivity().getIntent().getExtras();
		String extras = "";
		initViews(view);
		
		if (bundle != null) {
			try{
				extras = bundle.getString("events");
				arrColl = new JsonParser().parse(extras).getAsJsonArray();
				iSchedulersCount = arrColl.size();
				ALog.i("ALog.SCHEDULER", "SchedulerOverview::onCreateView() count is " + iSchedulersCount);
			} catch (Exception e) {
				ALog.i("ALog.SCHEDULER", "SchedulerOverview::onCreateView() method - initializing arrColl failed");
			}			
			CreateEventList(extras, view);
			ALog.i("ALog.SCHEDULER", "SchedulerOverview::onCreateView() method - extras is " + extras);
		}
		
		schedulersList = ((SchedulerActivity) getActivity()).getSchedulerList();
		
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::onCreateView() method exit");
		return view;
	}
	
	private void initViews(View view) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::initViews() method enter");
		ivEdit = (ImageView) view.findViewById(R.id.edit);
		ivEdit.setOnClickListener(onClickListener);		
		tvEditTxt = (TextView) view.findViewById(R.id.edittext);
		final ImageView ivAdd = (ImageView) view.findViewById(R.id.add);
		ivAdd.setOnClickListener(onClickListener);		
		ivDelete1 = (ImageView) view.findViewById(R.id.delete1);
		ivDelete1.setOnClickListener(onClickListener);
		ivDelete2 = (ImageView) view.findViewById(R.id.delete2);
		ivDelete2.setOnClickListener(onClickListener);
		ivDelete3 = (ImageView) view.findViewById(R.id.delete3);
		ivDelete3.setOnClickListener(onClickListener);
		ivDelete4 = (ImageView) view.findViewById(R.id.delete4);
		ivDelete4.setOnClickListener(onClickListener);
		ivDelete5 = (ImageView) view.findViewById(R.id.delete5);
		ivDelete5.setOnClickListener(onClickListener);
		ivRhtArr1 = (ImageView) view.findViewById(R.id.RightArrow1);
		ivRhtArr1.setOnClickListener(onClickListener);
		ivRhtArr2 = (ImageView) view.findViewById(R.id.RightArrow2);
		ivRhtArr2.setOnClickListener(onClickListener);
		ivRhtArr3 = (ImageView) view.findViewById(R.id.RightArrow3);
		ivRhtArr3.setOnClickListener(onClickListener);
		ivRhtArr4 = (ImageView) view.findViewById(R.id.RightArrow4);
		ivRhtArr4.setOnClickListener(onClickListener);
		ivRhtArr5 = (ImageView) view.findViewById(R.id.RightArrow5);
		ivRhtArr5.setOnClickListener(onClickListener);
		tvRgtArrTxt1 = (FontTextView) view.findViewById(R.id.RightArrow1_text);
		tvRgtArrTxt1.setOnClickListener(onClickListener);
		tvRgtArrTxt2 = (FontTextView) view.findViewById(R.id.RightArrow2_text);
		tvRgtArrTxt2.setOnClickListener(onClickListener);
		tvRgtArrTxt3 = (FontTextView) view.findViewById(R.id.RightArrow3_text);
		tvRgtArrTxt3.setOnClickListener(onClickListener);
		tvRgtArrTxt4 = (FontTextView) view.findViewById(R.id.RightArrow4_text);
		tvRgtArrTxt4.setOnClickListener(onClickListener);
		tvRgtArrTxt5 = (FontTextView) view.findViewById(R.id.RightArrow5_text);
		tvRgtArrTxt5.setOnClickListener(onClickListener);
		tvScheduler1_text1 = (FontTextView) view.findViewById(R.id.scheduler1_text1);
		tvScheduler2_text1 = (FontTextView) view.findViewById(R.id.scheduler2_text1);
		tvScheduler3_text1 = (FontTextView) view.findViewById(R.id.scheduler3_text1);
		tvScheduler4_text1 = (FontTextView) view.findViewById(R.id.scheduler4_text1);
		tvScheduler5_text1 = (FontTextView) view.findViewById(R.id.scheduler5_text1);
		tvScheduler1_text2 = (FontTextView) view.findViewById(R.id.scheduler1_text2);
		tvScheduler2_text2 = (FontTextView) view.findViewById(R.id.scheduler2_text2);
		tvScheduler3_text2 = (FontTextView) view.findViewById(R.id.scheduler3_text2);
		tvScheduler4_text2 = (FontTextView) view.findViewById(R.id.scheduler4_text2);
		tvScheduler5_text2 = (FontTextView) view.findViewById(R.id.scheduler5_text2);
		
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::initViews() method exit");
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method enter");
			
			switch (v.getId()) {
				case R.id.edit:
					((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.UPDATE_EVENT);
					toggleLayoutButtons();
					break;
				case R.id.add:
					JSONArray arrList = ((SchedulerActivity) getActivity()).getSchedulerList();
					((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.CREATE_EVENT);
					DialogFragment newFragment = new TimePickerFragment();
					newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
				case R.id.RightArrow1:
				case R.id.RightArrow1_text:
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method - RightArrow1 is invoked");
					takeActionOnRightArrowButtonClick(tvRgtArrTxt1, 0);
					break;
				case R.id.RightArrow2:
				case R.id.RightArrow2_text:
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method - RightArrow2 is invoked");
					takeActionOnRightArrowButtonClick(tvRgtArrTxt2, 1);
					break;
				case R.id.RightArrow3:
				case R.id.RightArrow3_text:
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method - RightArrow3 is invoked");
					takeActionOnRightArrowButtonClick(tvRgtArrTxt3, 2);
					break;
				case R.id.RightArrow4:
				case R.id.RightArrow4_text:
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method - RightArrow4 is invoked");
					takeActionOnRightArrowButtonClick(tvRgtArrTxt4, 3);
					break;
				case R.id.RightArrow5:
				case R.id.RightArrow5_text:
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method - RightArrow5 is invoked");
					takeActionOnRightArrowButtonClick(tvRgtArrTxt5, 4);
					break;
				case R.id.delete1:
					toggleRightArrowButton(ivRhtArr1, tvRgtArrTxt1, ivDelete1, 0);
					break;
				case R.id.delete2:
					toggleRightArrowButton(ivRhtArr2, tvRgtArrTxt2, ivDelete2, 1);
					break;
				case R.id.delete3:
					toggleRightArrowButton(ivRhtArr3, tvRgtArrTxt3, ivDelete3, 2);
					break;
				case R.id.delete4:
					toggleRightArrowButton(ivRhtArr4, tvRgtArrTxt4, ivDelete4, 3);
					break;
				case R.id.delete5:
					toggleRightArrowButton(ivRhtArr5, tvRgtArrTxt5, ivDelete5, 4);
					break;
				default:
					break;
			}
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method exit");
		}
	};
	
	private void toggleLayoutButtons() {
		if (tvEditTxt.getText().equals("Edit")) {
			tvEditTxt.setText("Done");
			toggleVisibility4SchedulerList(View.VISIBLE);
		} else {
			tvEditTxt.setText("Edit");
			toggleVisibility4SchedulerList(View.INVISIBLE);
		}	
	}
	
	private void toggleVisibility4SchedulerList(int iVisible) {
		
		if (iSchedulersCount >= 1) {
			toggleVisibility4SchedulerItem(ivDelete1, ivRhtArr1, tvRgtArrTxt1, tvScheduler1_text2, iVisible);
			MarginLayoutParams params = (MarginLayoutParams) tvScheduler1_text1.getLayoutParams();
			params.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT;			
			tvScheduler1_text1.setLayoutParams(params);
		}
		
		if (iSchedulersCount >= 2) {
			toggleVisibility4SchedulerItem(ivDelete2, ivRhtArr2, tvRgtArrTxt2, tvScheduler2_text2, iVisible);
		}
		
		if (iSchedulersCount >= 3) {
			toggleVisibility4SchedulerItem(ivDelete3, ivRhtArr3, tvRgtArrTxt3, tvScheduler3_text2, iVisible);
		}
		
		if (iSchedulersCount >= 4) {
			toggleVisibility4SchedulerItem(ivDelete4, ivRhtArr4, tvRgtArrTxt4, tvScheduler4_text2, iVisible);
		}
		
		if (iSchedulersCount >= 5) {
			toggleVisibility4SchedulerItem(ivDelete5, ivRhtArr5, tvRgtArrTxt5, tvScheduler5_text2, iVisible);
		}
	}
	
	private void toggleVisibility4SchedulerItem(ImageView delete, ImageView rhtArrow, FontTextView rhtArrText, FontTextView inner_text2, int iVisible) {
		delete.setVisibility(iVisible);
		rhtArrow.setVisibility(iVisible);
		rhtArrText.setVisibility(iVisible);
		inner_text2.setVisibility(iVisible);
	}
	
	private void takeActionOnRightArrowButtonClick(FontTextView RhtArrText, int index)  {
		if (RhtArrText.length() > 0) {
			refreshDeleteSchedulerPage(index);
		} else {
			editSchededuler(index);
		}
	}
	
	private void refreshDeleteSchedulerPage(int index) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() method enter");
		((SchedulerActivity)getActivity()).deleteScheduler(index);
		if (lstSchedulerMarkedDel.contains(index)) {
			lstSchedulerMarkedDel.remove(index);
		}
		((SchedulerActivity)getActivity()).dispatchInfo4MarkedSchDeletion(lstSchedulerMarkedDel);
	}
	
	private void editSchededuler(int index) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::editSchededuler() method enter");
		Bundle bundle = new Bundle();
		try{
			((SchedulerActivity)getActivity()).dispatchInformationsForCRUDIndex(index);
			JSONObject obj = schedulersList.getJSONObject(index);
			
			try{
				bundle.putString("time", obj.getString("time"));
				bundle.putString("days", obj.getString("days"));
				bundle.putString("speed", obj.getString("speed"));
			}
			catch (Exception e){
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::editSchededuler() method - exception caught while getting property");
			}
			
			AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
			fragAddSch.setArguments(bundle);
			getFragmentManager()
	  	    .beginTransaction()
	  		.replace(R.id.ll_scheduler_container, fragAddSch, "AddSchedulerFragment")
	  		.commit();
		}
		catch (Exception e) {
			ALog.d(ALog.SCHEDULER, "Error in editSchededuler: " + e.getMessage());
		}
	}
	
	private void toggleRightArrowButton(ImageView btnRhtArr, FontTextView  rhtArrowText, ImageView delete, int index) {
		if (rhtArrowText.getText().length() > 0) {
			rhtArrowText.setText("");
			MarginLayoutParams params = (MarginLayoutParams) btnRhtArr.getLayoutParams();
			params.rightMargin = 1;
			btnRhtArr.setLayoutParams(params);
			delete.setBackgroundResource(R.drawable.delete_l2r);
			btnRhtArr.setImageResource(R.drawable.about_air_quality_goto);
			delete.setImageResource(R.drawable.delete_l2r);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleRightArrowButton() method - index is  " + index);
			lstSchedulerMarkedDel.remove((Integer)index);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleRightArrowButton() method - item removed  " + index);
		} else {
			rhtArrowText.setText("Delete");
			MarginLayoutParams params = (MarginLayoutParams) btnRhtArr.getLayoutParams();
			params.rightMargin = 50;
			btnRhtArr.setLayoutParams(params);
			rhtArrowText.setLayoutParams(params);
			btnRhtArr.setImageResource(R.drawable.delete);
			delete.setImageResource(R.drawable.delete_t2b);
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleRightArrowButton() method - index is  " + index);
			if (!lstSchedulerMarkedDel.contains(index)) {
				lstSchedulerMarkedDel.add(index);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleRightArrowButton() method - item added  " + index);
			}
		}
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleRightArrowButton() method - items marked 4 deletion " + lstSchedulerMarkedDel.toString());
	}
	private void CreateEventList(String extras, View view) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method enter  ");
		String sProduct = "";
		String sPort = "";
		String sSpeed = "";
		String sCommand = "";;
		String sEventSetting = "";
		String sTime = "";
		String sDays = "";
		String sEnabled = "";
		
		try{
			JSONArray arrColl = new JSONArray(extras);
		
			for (int i=0;i<arrColl.length();i++) {
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method - printing Json object  " + i);
			    JSONObject json = arrColl.getJSONObject(i);
			    
			    try {
			    	sEnabled = json.getString(SchedulerConstants.ENABLED);
			    }
			    catch(Exception e) {
			    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting enabled property");
			    }
			    
			    try {
			    	sTime = json.getString(SchedulerConstants.TIME);
			    }
			    catch(Exception e) {
			    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting time property");
			    }
			    
			    try {
			    	sDays = json.getString(SchedulerConstants.DAYS);
			    } 
			    
			    catch(Exception e) {
			    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting days property");
			    	sDays = SchedulerConstants.ONE_TIME;
			    }
			    
			    try {
			    	sDays = setWeekDays2(sDays);
			    	sProduct = json.getString(SchedulerConstants.PRODUCT);
			    	sPort = json.getString(SchedulerConstants.PORT);
			    }
			    catch(Exception e) {
			    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting sDays, sProduct and sPort property");
			    }
			    
			    try {
			    	sSpeed = json.getString(SchedulerConstants.SPEED);
			    } 			    
			    catch (Exception e) {
			    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting speed property");
			    	sSpeed = "Auto";
			    }
			    
			    try {
			    	sCommand = json.getString(SchedulerConstants.COMMAND);
			    	sEventSetting = "";
			    }
			    catch (Exception e) {
			    	ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting sCommand property");
			    }
				//sEventSetting = sSpeed + ", " + sDays;
			    sEventSetting = sDays;
			    
			    int bButton = 0;
				
				switch(i) {
				case 0:
					CreateEvent(view, R.id.event1, R.id.scheduler1_outer, R.id.scheduler1_innerouter, R.id.scheduler1_text1, R.id.scheduler1_text2, bButton, R.id.divider1, sTime, sEventSetting, sEnabled);
					break;
				case 1:
					CreateEvent(view, R.id.event2, R.id.scheduler2_outer, R.id.scheduler2_innerouter, R.id.scheduler2_text1, R.id.scheduler2_text2, bButton, R.id.divider2, sTime, sEventSetting, sEnabled);
					break;
				case 2:
					CreateEvent(view, R.id.event3, R.id.scheduler3_outer, R.id.scheduler3_innerouter, R.id.scheduler3_text1, R.id.scheduler3_text2, bButton, R.id.divider3, sTime, sEventSetting, sEnabled);
					break;
				case 3:
					CreateEvent(view, R.id.event4, R.id.scheduler4_outer, R.id.scheduler4_innerouter, R.id.scheduler4_text1, R.id.scheduler4_text2, bButton, R.id.divider4, sTime, sEventSetting, sEnabled);
					break;
				case 4:
					CreateEvent(view, R.id.event5, R.id.scheduler5_outer, R.id.scheduler5_innerouter, R.id.scheduler5_text1, R.id.scheduler5_text2, bButton, R.id.divider5, sTime, sEventSetting, sEnabled);
					break;
				default:
					break;
				}
				
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Enabled value is  " + sEnabled);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - time value is  " + sTime);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - days value is  " + sDays);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - product value is  " + sProduct);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - port value is  " + sPort);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - speed value is  " + sSpeed);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - command value is  " + sCommand);	
			}
		} catch(Exception e) {
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - exception caught while retrieving property  ");	
		}
		
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method exit  ");
	}
	
	private void CreateEvent(View view, int layout, int iTxtOuterView, int iTxtInnerOuterView, int iTxtView1, int iTxtView2, int iButton, int iImgView, String time, String event, String enabled) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEvent() method enter");
		FontTextView txtOuterView, txtInnerOuterView;
		FontTextView txtView1, txtView2;
		Button btn;
		LinearLayout lLayout;
		//ImageView imgView;
		
		lLayout = (LinearLayout) view.findViewById(layout);
		lLayout.setVisibility(View.VISIBLE);
		
		txtOuterView = (FontTextView) view.findViewById(iTxtOuterView);
		txtOuterView.setVisibility(View.VISIBLE);
		
		txtInnerOuterView = (FontTextView) view.findViewById(iTxtInnerOuterView);
		txtInnerOuterView.setVisibility(View.VISIBLE);
		
		txtView1 = (FontTextView) view.findViewById(iTxtView1);
		txtView1.setText(time);
		txtView1.setVisibility(View.VISIBLE);
		
		txtView2 = (FontTextView) view.findViewById(iTxtView2);
		txtView2.setText(event);
		//txtView2.setVisibility(View.VISIBLE);
		
		/*btn = (Button) view.findViewById(iButton);
		//btn.setVisibility(View.VISIBLE);
		
		if (enabled.equals("true")) {
			btn.setBackgroundResource(R.drawable.toggle_on);
			btn.setTag("on");
		} else {
			btn.setBackgroundResource(R.drawable.toggle_off);
			btn.setTag("off");
		}*/	
		
		//imgView = (ImageView) view.findViewById(iImgView);
		//imgView.setVisibility(View.VISIBLE);
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEvent() method exit");
	}
	
	private String setWeekDays2(String days) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::setWeekDays2() method enter");
		String sWeekdays = "";
		
		if (days.equals(SchedulerConstants.ONE_TIME))
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
