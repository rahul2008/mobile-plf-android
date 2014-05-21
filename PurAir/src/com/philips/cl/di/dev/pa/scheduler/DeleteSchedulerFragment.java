package com.philips.cl.di.dev.pa.scheduler;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
	private JSONArray schedulersList;
	//Button ivRhtArr2, ivRhtArr3, ivRhtArr4; 
	ImageView ivRhtArr1, ivRhtArr2, ivRhtArr3, ivRhtArr4, ivRhtArr5; 
	ImageView add, edit, delete1, delete2, delete3, delete4, delete5;
	FontTextView RightArrow1_text, RightArrow2_text, RightArrow3_text, RightArrow4_text, RightArrow5_text;
	LinearLayout event1;
	String marked4Deletion = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onCreateView() method enter");
		View view = inflater.inflate(R.layout.delete_scheduler, null);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.DELETE_EVENT);
		//marked4Deletion = ((SchedulerActivity) getActivity()).getSchedulerMarked4Deletion();
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onCreateView() method marked4Deletion is " + marked4Deletion);
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
		RightArrow1_text = (FontTextView) view.findViewById(R.id.RightArrow1_text);
		RightArrow1_text.setOnClickListener(onClickListener);
		
		ivRhtArr2 = (ImageView) view.findViewById(R.id.RightArrow2);
		ivRhtArr2.setOnClickListener(onClickListener);
		RightArrow2_text = (FontTextView) view.findViewById(R.id.RightArrow2_text);
		RightArrow2_text.setOnClickListener(onClickListener);
		
		ivRhtArr3 = (ImageView) view.findViewById(R.id.RightArrow3);
		ivRhtArr3.setOnClickListener(onClickListener);
		RightArrow3_text = (FontTextView) view.findViewById(R.id.RightArrow3_text);
		RightArrow3_text.setOnClickListener(onClickListener);
		
		ivRhtArr4 = (ImageView) view.findViewById(R.id.RightArrow4);
		ivRhtArr4.setOnClickListener(onClickListener);
		RightArrow4_text = (FontTextView) view.findViewById(R.id.RightArrow4_text);
		RightArrow4_text.setOnClickListener(onClickListener);
		
		ivRhtArr5 = (ImageView) view.findViewById(R.id.RightArrow5);
		ivRhtArr5.setOnClickListener(onClickListener);
		RightArrow5_text = (FontTextView) view.findViewById(R.id.RightArrow5_text);
		RightArrow5_text.setOnClickListener(onClickListener);
		
		delete1 = (ImageView) view.findViewById(R.id.delete1);
		delete1.setOnClickListener(onClickListener);		
		delete2 = (ImageView) view.findViewById(R.id.delete2);
		delete2.setOnClickListener(onClickListener);
		delete3 = (ImageView) view.findViewById(R.id.delete3);
		delete3.setOnClickListener(onClickListener);
		delete4 = (ImageView) view.findViewById(R.id.delete4);
		delete4.setOnClickListener(onClickListener);
		delete5 = (ImageView) view.findViewById(R.id.delete5);
		delete5.setOnClickListener(onClickListener);
		
		event1 = (LinearLayout) view.findViewById(R.id.event1);
		event1.setOnClickListener(onClickListener);
		
		double viewWidth = (double) view.getWidth();
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method enter");
			
			switch (v.getId()) {
				case R.id.RightArrow1:
				case R.id.RightArrow1_text:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow1 is invoked");
					takeActionOnRightArrowButtonClick(RightArrow1_text, 0);
					break;
				case R.id.RightArrow2:
				case R.id.RightArrow2_text:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow2 is invoked");
					takeActionOnRightArrowButtonClick(RightArrow2_text, 1);
					break;
				case R.id.RightArrow3:
				case R.id.RightArrow3_text:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow3 is invoked");
					takeActionOnRightArrowButtonClick(RightArrow3_text, 2);
					break;
				case R.id.RightArrow4:
				case R.id.RightArrow4_text:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow4 is invoked");
					takeActionOnRightArrowButtonClick(RightArrow4_text, 3);
					break;
				case R.id.RightArrow5:
				case R.id.RightArrow5_text:
					ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method - RightArrow5 is invoked");
					takeActionOnRightArrowButtonClick(RightArrow5_text, 4);
					break;
				case R.id.add:
					JSONArray arrList = ((SchedulerActivity) getActivity()).getSchedulerList();
					if (arrList.length() < SchedulerConstants.SCHEDULER_COUNT ) {
						((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.CREATE_EVENT);
						DialogFragment newFragment = new TimePickerFragment();
						newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
					} else {
						add.setEnabled(false);
					}
					break;
				case R.id.edit:
					showSchedulerOverviewFragment();
					break;
				case R.id.delete1:
					toggleRightArrowButtonView(ivRhtArr1, RightArrow1_text, delete1, 0);
					break;
				case R.id.delete2:
					toggleRightArrowButtonView(ivRhtArr2, RightArrow2_text, delete2, 1);
					break;
				case R.id.delete3:
					toggleRightArrowButtonView(ivRhtArr3, RightArrow3_text, delete3, 2);
					break;
				case R.id.delete4:
					toggleRightArrowButtonView(ivRhtArr4, RightArrow4_text, delete4, 3);
					break;
				case R.id.delete5:
					toggleRightArrowButtonView(ivRhtArr5, RightArrow5_text, delete5, 4);
					break;
				default:
					break;
			}
			ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::onClick() method exit");
		}
	};
	
	private void toggleRightArrowButtonView(ImageView btnRhtArr, FontTextView  rhtArrowText, ImageView delete, int index) {
		if (rhtArrowText.getText().length() > 0) {
			rhtArrowText.setText("");
			MarginLayoutParams params = (MarginLayoutParams) btnRhtArr.getLayoutParams();
			params.rightMargin = 1;
			btnRhtArr.setLayoutParams(params);
			delete.setBackgroundResource(R.drawable.delete_l2r);
			btnRhtArr.setImageResource(R.drawable.about_air_quality_goto);
			delete.setImageResource(R.drawable.delete_l2r);
			setMarked4Deletion(index, '0');
		} else {
			rhtArrowText.setText("Delete");
			MarginLayoutParams params = (MarginLayoutParams) btnRhtArr.getLayoutParams();
			params.rightMargin = 50;
			btnRhtArr.setLayoutParams(params);
			rhtArrowText.setLayoutParams(params);
			btnRhtArr.setImageResource(R.drawable.delete);
			delete.setImageResource(R.drawable.delete_t2b);
			setMarked4Deletion(index, '1');
		}
	}
	
	private void takeActionOnRightArrowButtonClick(FontTextView RhtArrText, int index)  {
		if (RhtArrText.length() > 0) {
			refreshDeleteSchedulerPage(index);
		} else {
			editSchededuler(index);
		}
	}
	
	private void enableDeleteButton(ImageView btnRhtArr, FontTextView  rhtArrowText, ImageView delete, int index) {
		rhtArrowText.setText("Delete");
		MarginLayoutParams params = (MarginLayoutParams) btnRhtArr.getLayoutParams();
		params.rightMargin = 50;
		btnRhtArr.setLayoutParams(params);
		btnRhtArr.setImageResource(R.drawable.delete);
		delete.setImageResource(R.drawable.delete_t2b);
	}
	
	private void refreshDeleteSchedulerPage(int index) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::refreshDeleteSchedulerPage() method enter");
		((SchedulerActivity)getActivity()).deleteScheduler(index);
	}
	
	private void editSchededuler(int index) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::editSchededuler() method enter");
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
				ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::editSchededuler() method - exception caught while getting property");
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
	
	private void CreateEventList(String extras, View view) {
		ALog.i("Scheduler", "DeleteSchedulerFragment::CreateEventList() method enter");
		String sIsEnabled, sTime, sDays, sProduct, sPort, sSpeed, sCommand;
		String sEventSetting = "";
		
		JsonArray arrColl = new JsonParser().parse(extras).getAsJsonArray();
		for (int i=0;i<arrColl.size();i++) {
		    JsonObject json = arrColl.get(i).getAsJsonObject();
		    
		    sIsEnabled = getJsonPropertyAsString(json, "enabled");
		    sTime = getJsonPropertyAsString(json, "time");
		    
		    try {
		    sDays = getJsonPropertyAsString(json, "days");
		    } catch(Exception e) {
		    	ALog.i("Scheduler", "SDeleteSchedulerFragment::CreateEventList() method - Excpetion caught while getting days property");
		    	sDays = SchedulerConstants.ONE_TIME;
		    }
		    
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
		    
			switch(i) {
			case 0:
				CreateEvent(view, R.id.event1, R.id.scheduler1_outer, R.id.scheduler1_innerouter, R.id.scheduler1_text1, R.id.scheduler1_text2, R.id.RightArrow1, R.id.RightArrow1_text, R.id.divider1, R.id.delete1, 0, sTime, sEventSetting);
				break;
			case 1:
				CreateEvent(view, R.id.event2, R.id.scheduler2_outer, R.id.scheduler2_innerouter, R.id.scheduler2_text1, R.id.scheduler2_text2, R.id.RightArrow2, R.id.RightArrow2_text, R.id.divider2, R.id.delete2, 1, sTime, sEventSetting);
				break;
			case 2:
				CreateEvent(view, R.id.event3, R.id.scheduler3_outer, R.id.scheduler3_innerouter, R.id.scheduler3_text1, R.id.scheduler3_text2, R.id.RightArrow3, R.id.RightArrow3_text, R.id.divider3, R.id.delete3, 2, sTime, sEventSetting);
				break;
			case 3:
				CreateEvent(view, R.id.event4, R.id.scheduler4_outer, R.id.scheduler4_innerouter, R.id.scheduler4_text1, R.id.scheduler4_text2, R.id.RightArrow4, R.id.RightArrow4_text, R.id.divider4, R.id.delete4, 3, sTime, sEventSetting);
				break;
			case 4:
				CreateEvent(view, R.id.event5, R.id.scheduler5_outer, R.id.scheduler5_innerouter, R.id.scheduler5_text1, R.id.scheduler5_text2, R.id.RightArrow5, R.id.RightArrow5_text, R.id.divider5, R.id.delete5, 4, sTime, sEventSetting);
				break;
			default:
				break;
			}
		}
	}
	
	private void CreateEvent(View view, int layout, int iTxtOuterView, int iTxtInnerOuterView, int iTxtView1, int iTxtView2, int rightArr, int rightArrTxt, int iImgView, int delete, int index, String time, String event) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() method enter");
		LinearLayout lLayout;
		FontTextView txtOuterView, txtInnerOuterView, txtView1, txtView2, ivRightArrText;
		ImageView ivDelete, ivRightArr;
		
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
		txtView2.setVisibility(View.VISIBLE);
		
		ivRightArr = (ImageView) view.findViewById(rightArr);
		ivRightArr.setVisibility(View.VISIBLE);
		
		ivRightArrText = (FontTextView) view.findViewById(rightArrTxt);
		ivRightArrText.setVisibility(View.VISIBLE);
		
		ivDelete = (ImageView) view.findViewById(delete);
		ivDelete.setVisibility(View.VISIBLE);		
		
		if (!marked4Deletion.isEmpty()) {
			char[] tempArr = marked4Deletion.toCharArray();
			ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() marked4Deletion tempArr[index] is " + tempArr[index]);
			if (index == 0 && tempArr[index] == 1) {
				ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() marked4Deletion is index 1 is called");
				enableDeleteButton(ivRhtArr1, RightArrow1_text, delete1, 0);
			} else if (index == 1 && tempArr[index] == 1) {
				ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() marked4Deletion is index 2 is called");
				enableDeleteButton(ivRhtArr2, RightArrow2_text, delete2, 0);
			} else if (index == 2 && tempArr[index] == 1) {
				ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() marked4Deletion is index 3 is called");
				enableDeleteButton(ivRhtArr3, RightArrow3_text, delete3, 0);
			} else if (index == 3 && tempArr[index] == 1) {
				ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() marked4Deletion is index 4 is called");
				enableDeleteButton(ivRhtArr4, RightArrow4_text, delete4, 0);
			} else if (index == 4 && tempArr[index] == 1) {
				ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() marked4Deletion is index 5 is called");
				enableDeleteButton(ivRhtArr5, RightArrow5_text, delete5, 0);
			}
		}
		
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::CreateEvent() method exit");
	}
	
	private void setMarked4Deletion(int index, char flag) {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::setMarked4Deletion() method enter");
		if (!marked4Deletion.isEmpty()) {
			char[] tempArr = marked4Deletion.toCharArray();
			tempArr[index] = flag;
			marked4Deletion = String.valueOf(tempArr);
			//((SchedulerActivity)getActivity()).dispatchInfo4MarkedSchDeletion(marked4Deletion.toString());
		}
	}
	
	private void showSchedulerOverviewFragment() {
		ALog.i(ALog.SCHEDULER, "DeleteSchedulerFragment::showSchedulerOverviewFragment() method enter");
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();
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
		if( jsonObject == null ) return "" ;
		JsonElement progressElemt = jsonObject.get(property);
		if( progressElemt == null ) return  "" ;
 		return progressElemt.getAsString();
	}
}
