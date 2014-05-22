package com.philips.cl.di.dev.pa.scheduler;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
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

public class SchedulerOverviewFragment extends BaseFragment {
	
	private ImageView ivEdit, ivDelete1, ivDelete2, ivDelete3, ivDelete4, ivDelete5;
	private ImageView ivRhtArr1, ivRhtArr2, ivRhtArr3, ivRhtArr4, ivRhtArr5;
	private FontTextView tvRgtArrTxt1, tvRgtArrTxt2, tvRgtArrTxt3, tvRgtArrTxt4, tvRgtArrTxt5;
	private TextView tvEditTxt;
	private FontTextView tvScheduler1_text2, tvScheduler2_text2, tvScheduler3_text2, tvScheduler4_text2, tvScheduler5_text2;
	private FontTextView tvScheduler1_text1;
	private JsonArray arrColl;
	private int iSchedulersCount;
	private JSONArray schedulersList;
	private List<Integer> lstSchedulerMarkedDel = new ArrayList<Integer>();
	
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
			
			lstSchedulerMarkedDel = ((SchedulerActivity) getActivity()).getSchedulerMarked4Deletion();
			ALog.i("ALog.SCHEDULER", "SchedulerOverview::onCreateView() method - - lstSchedulerMarkedDel count is " + lstSchedulerMarkedDel.size());
			ALog.i("ALog.SCHEDULER", "SchedulerOverview::onCreateView() method - - lstSchedulerMarkedDel value is " + lstSchedulerMarkedDel.toString());
					
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
		tvScheduler1_text2 = (FontTextView) view.findViewById(R.id.scheduler1_text2);
		tvScheduler2_text2 = (FontTextView) view.findViewById(R.id.scheduler2_text2);
		tvScheduler3_text2 = (FontTextView) view.findViewById(R.id.scheduler3_text2);
		tvScheduler4_text2 = (FontTextView) view.findViewById(R.id.scheduler4_text2);
		tvScheduler5_text2 = (FontTextView) view.findViewById(R.id.scheduler5_text2);
		
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::initViews() method exit");
	}
	
	private void CreateEventList(String extras, View view) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method enter  ");
		String sProduct = "";
		String sPort = "";
		String sSpeed = "";
		String sCommand = "";
		String sEventSetting = "";
		String sTime = "";
		String sDays = "";
		String sEnabled = "";

		//TODO : Change exception being caught to JSONException.
		try{
			JSONArray arrColl = new JSONArray(extras);

			for (int i=0;i<arrColl.length();i++) {
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method - printing Json object  " + i);
				JSONObject json = arrColl.getJSONObject(i);

				try {
					sEnabled = json.getString(SchedulerConstants.ENABLED);
				}
				catch(Exception e) {
					ALog.e(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting enabled property");
				}

				try {
					sTime = json.getString(SchedulerConstants.TIME);
				}
				catch(Exception e) {
					ALog.e(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - Excpetion caught while getting time property");
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

				switch(i) {
				case 0:
					CreateEvent(view, R.id.event1, R.id.scheduler1_outer, R.id.scheduler1_innerouter, R.id.scheduler1_text1, R.id.scheduler1_text2, sTime, sEventSetting);
					break;
				case 1:
					CreateEvent(view, R.id.event2, R.id.scheduler2_outer, R.id.scheduler2_innerouter, R.id.scheduler2_text1, R.id.scheduler2_text2, sTime, sEventSetting);
					break;
				case 2:
					CreateEvent(view, R.id.event3, R.id.scheduler3_outer, R.id.scheduler3_innerouter, R.id.scheduler3_text1, R.id.scheduler3_text2, sTime, sEventSetting);
					break;
				case 3:
					CreateEvent(view, R.id.event4, R.id.scheduler4_outer, R.id.scheduler4_innerouter, R.id.scheduler4_text1, R.id.scheduler4_text2, sTime, sEventSetting);
					break;
				case 4:
					CreateEvent(view, R.id.event5, R.id.scheduler5_outer, R.id.scheduler5_innerouter, R.id.scheduler5_text1, R.id.scheduler5_text2, sTime, sEventSetting);
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
				
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - lstSchedulerMarkedDel size is " + lstSchedulerMarkedDel.size());
				if (lstSchedulerMarkedDel.size() > 0) {
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - lstSchedulerMarkedDel value is " + lstSchedulerMarkedDel.toString());
					tvEditTxt.setText("Done");
					toggleVisibility4SchedulerList(View.VISIBLE);
				} 
			}
		} catch(JSONException e) {
			ALog.e(ALog.SCHEDULER, "SchedulerOverview::CreateEventList method - exception caught while retrieving property  ");	
		}

		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEventList() method exit  ");
	}
	
	private void CreateEvent(View view, int layout, int iTxtOuterView, int iTxtInnerOuterView, int iTxtView1, int iTxtView2, String time, String event) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::CreateEvent() method enter");
		FontTextView txtOuterView, txtInnerOuterView;
		FontTextView txtView1, txtView2;
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
	

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method enter");
			
			switch (v.getId()) {
				case R.id.edit:
					((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.UPDATE_EVENT);
					toggleLayout("Edit");
					break;
				case R.id.add:
					((SchedulerActivity)getActivity()).dispatchInformationsForCRUD(SchedulerConstants.CREATE_EVENT);
					DialogFragment newFragment = new TimePickerFragment();
					newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
					break;
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
					showRightArrowOrDeleteButton(ivRhtArr1, tvRgtArrTxt1, ivDelete1, 0);
					break;
				case R.id.delete2:
					showRightArrowOrDeleteButton(ivRhtArr2, tvRgtArrTxt2, ivDelete2, 1);
					break;
				case R.id.delete3:
					showRightArrowOrDeleteButton(ivRhtArr3, tvRgtArrTxt3, ivDelete3, 2);
					break;
				case R.id.delete4:
					showRightArrowOrDeleteButton(ivRhtArr4, tvRgtArrTxt4, ivDelete4, 3);
					break;
				case R.id.delete5:
					showRightArrowOrDeleteButton(ivRhtArr5, tvRgtArrTxt5, ivDelete5, 4);
					break;
				default:
					break;
			}
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::onClick() method exit");
		}
	};
	
	private void toggleLayout(String layoutType) {
		if (tvEditTxt.getText().equals(layoutType)) {
			tvEditTxt.setText("Done");
			toggleVisibility4SchedulerList(View.VISIBLE);
		} else {
			tvEditTxt.setText("Edit");
			lstSchedulerMarkedDel.clear();
			toggleVisibility4SchedulerList(View.INVISIBLE);
		}	
	}
	
	private void toggleVisibility4SchedulerList(int iVisible) {
		
		if (iSchedulersCount >= 1) {
			if (lstSchedulerMarkedDel.contains(0)) {
				toggleVisibility4SchedulerItem(ivDelete1, ivRhtArr1, tvRgtArrTxt1, tvScheduler1_text2, View.VISIBLE, true);
			} else {
				toggleVisibility4SchedulerItem(ivDelete1, ivRhtArr1, tvRgtArrTxt1, tvScheduler1_text2, iVisible, false);
				MarginLayoutParams params = (MarginLayoutParams) tvScheduler1_text1.getLayoutParams();
				params.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT;			
				tvScheduler1_text1.setLayoutParams(params);
			}
		}
		
		if (iSchedulersCount >= 2) {
			if (lstSchedulerMarkedDel.contains(1)) {
				toggleVisibility4SchedulerItem(ivDelete2, ivRhtArr2, tvRgtArrTxt2, tvScheduler2_text2, View.VISIBLE, true);
			} else {
				toggleVisibility4SchedulerItem(ivDelete2, ivRhtArr2, tvRgtArrTxt2, tvScheduler2_text2, iVisible, false);
			}
		}
		
		if (iSchedulersCount >= 3) {
			if (lstSchedulerMarkedDel.contains(2)) {
				toggleVisibility4SchedulerItem(ivDelete3, ivRhtArr3, tvRgtArrTxt3, tvScheduler3_text2, View.VISIBLE, true);
			} else {
				toggleVisibility4SchedulerItem(ivDelete3, ivRhtArr3, tvRgtArrTxt3, tvScheduler3_text2, iVisible, false);
			}
		}
		
		if (iSchedulersCount >= 4) {
			if (lstSchedulerMarkedDel.contains(3)) {
				toggleVisibility4SchedulerItem(ivDelete4, ivRhtArr4, tvRgtArrTxt4, tvScheduler4_text2, View.VISIBLE, true);
			} else {
				toggleVisibility4SchedulerItem(ivDelete4, ivRhtArr4, tvRgtArrTxt4, tvScheduler4_text2, iVisible, false);
			}
		}
		
		if (iSchedulersCount >= 5) {
			if (lstSchedulerMarkedDel.contains(4)) {
				toggleVisibility4SchedulerItem(ivDelete5, ivRhtArr5, tvRgtArrTxt5, tvScheduler5_text2, View.VISIBLE, true);
			} else {
				toggleVisibility4SchedulerItem(ivDelete5, ivRhtArr5, tvRgtArrTxt5, tvScheduler5_text2, iVisible, false);
			}
		}
	}
	
	private void toggleVisibility4SchedulerItem(ImageView delete, ImageView rhtArrow, FontTextView rhtArrText, FontTextView inner_text2, int iVisible, boolean bIsMarked4Del) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleVisibility4SchedulerItem() method enter");
		delete.setVisibility(iVisible);
		rhtArrow.setVisibility(iVisible);
		rhtArrText.setVisibility(iVisible);
		inner_text2.setVisibility(iVisible);
		if (bIsMarked4Del == true) {
			showDeleteButton(rhtArrow, rhtArrText, delete);
		} else {
			showRightArrowButton(rhtArrow, rhtArrText, delete);
		}
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleVisibility4SchedulerItem() method exit");
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
		if (lstSchedulerMarkedDel.contains((Integer)index)) {
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() item removed is " + index);
			lstSchedulerMarkedDel.remove((Integer)index);
			
			ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() - lstSchedulerMarkedDel before is " + lstSchedulerMarkedDel.toString());
			
			for(int i=0; i<lstSchedulerMarkedDel.size(); i++) {
				if (i>=index) {
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() - lstSchedulerMarkedDel inside is " + lstSchedulerMarkedDel.toString());
					Integer newVal = lstSchedulerMarkedDel.get(i) - 1;
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() item # to be modified : " + i);
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() old value for item # : " + lstSchedulerMarkedDel.get(i));
					ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() new value for item # : " + newVal);
					//lstSchedulerMarkedDel.remove(i);
					//lstSchedulerMarkedDel.add(newVal);
					lstSchedulerMarkedDel.set(i, newVal);
				}
			}
		}
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::refreshDeleteSchedulerPage() - lstSchedulerMarkedDel after is " + lstSchedulerMarkedDel.toString());
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
	
	private void showRightArrowOrDeleteButton(ImageView btnRhtArr, FontTextView  rhtArrowText, ImageView delete, int index) {
		if (rhtArrowText.getText().length() > 0) {
			showRightArrowButton(btnRhtArr, rhtArrowText, delete);
			lstSchedulerMarkedDel.remove((Integer)index);
		} else {
			showDeleteButton(btnRhtArr, rhtArrowText, delete);
			if (!lstSchedulerMarkedDel.contains(index)) {
				lstSchedulerMarkedDel.add(index);
				ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleRightArrowButton() method - item added  " + index);
			}
		}
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::toggleRightArrowButton() method - items marked 4 deletion " + lstSchedulerMarkedDel.toString());
	}
	
	private void showRightArrowButton(ImageView btnRhtArr, FontTextView  rhtArrowText, ImageView delete) {
		rhtArrowText.setText("");
		MarginLayoutParams params = (MarginLayoutParams) btnRhtArr.getLayoutParams();
		params.rightMargin = 1;
		btnRhtArr.setLayoutParams(params);
		delete.setBackgroundResource(R.drawable.delete_l2r);
		btnRhtArr.setImageResource(R.drawable.about_air_quality_goto);
		delete.setImageResource(R.drawable.delete_l2r);
	}
	
	private void showDeleteButton(ImageView btnRhtArr, FontTextView  rhtArrowText, ImageView delete) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::showDeleteButton() method enter");
		rhtArrowText.setText("Delete");
		MarginLayoutParams params = (MarginLayoutParams) btnRhtArr.getLayoutParams();
		params.rightMargin = 50;
		btnRhtArr.setLayoutParams(params);
		rhtArrowText.setLayoutParams(params);
		btnRhtArr.setImageResource(R.drawable.delete);
		delete.setImageResource(R.drawable.delete_t2b);
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::showDeleteButton() method exit");
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

}
