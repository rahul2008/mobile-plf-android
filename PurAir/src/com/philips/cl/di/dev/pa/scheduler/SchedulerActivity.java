package com.philips.cl.di.dev.pa.scheduler;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.purifier.TaskPutDeviceDetails;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerActivity extends BaseActivity implements OnClickListener,
		ServerResponseListener, OnTimeSetListener {

	private static boolean cancelled;
	private Button actionBarCancelBtn;
	private Button actionBarBackBtn;
	private FontTextView actionbarTitle;
	private String SelectedDays;
	private String SelectedTime;
	private String SelectedFanspeed;
	private String updateSelectedDays;
	private String updateSelectedTime;
	private String updateSelectedFanspeed;
	private String CRUDOperation = "";
	private int UDOperationIndex = -1;
	private JSONArray arrSchedulers;
	private PurAirDevice purAirDevice ;
	private int scheduleType ;
	private List<Integer> SchedulerMarked4Deletion = new ArrayList<Integer>();
	private SchedulerOverviewFragment schFragment;
	
	private static final int ADD_SCHEDULE = 1 ;
	private static final int DELETE_SCHEDULE = 2 ;
	private static final int EDIT_SCHEDULE = 3 ;
	private static final int GET_SCHEDULES = 4 ;
	
	private List<ScheduleDto> schedulesList ;
	private ProgressDialog progressDialog ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler_container);
		SchedulerMarked4Deletion.clear();
		initActionBar();
		arrSchedulers = new JSONArray();
		showSchedulerOverviewFragment();
		purAirDevice = PurifierManager.getInstance().getCurrentPurifier() ;
	}
	
	private void initActionBar() {
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

		Drawable d = getResources().getDrawable(R.drawable.ews_nav_bar_2x);
		actionBar.setBackgroundDrawable(d);

		View view = getLayoutInflater().inflate(R.layout.scheduler_actionbar, null);
		actionbarTitle = (FontTextView) view.findViewById(R.id.scheduler_actionbar_title);
		actionbarTitle.setText(getString(R.string.scheduler));

		actionBarCancelBtn = (Button) view.findViewById(R.id.scheduler_actionbar_add_btn);
		actionBarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionBarCancelBtn.setOnClickListener(this);
		actionBarCancelBtn.setOnClickListener(onClickListener);

		actionBarBackBtn = (Button) view.findViewById(R.id.larrow);
		actionBarBackBtn.setOnClickListener(onClickListener);

		actionBar.setCustomView(view);
	}
	
	private void showProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.show();
	}
	
	private void cancelProgressDialog() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog != null && progressDialog.isShowing()) {
					progressDialog.cancel() ;
				}				
			}			
		});	
	}
	
	/**
	 * 
	 */
	private void save() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::Save() method enter");		
		if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			addScheduler();
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			updateScheduler();
		} else if (CRUDOperation.isEmpty()) {
			showSchedulerOverviewFragment();
		}
	}
	
	/**
	 * Create the scheduler
	 */
	private void addScheduler() {
		scheduleType = ADD_SCHEDULE ;
		ALog.i(ALog.SCHEDULER, "createScheduler") ;
		String addSchedulerJson = JSONBuilder.getSchedulesJson(SelectedTime, SelectedFanspeed, SelectedDays, true) ;
		
		if( purAirDevice != null && purAirDevice.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
			TaskPutDeviceDetails addSchedulerTask =
					new TaskPutDeviceDetails(new DISecurity(null).encryptData(addSchedulerJson, purAirDevice), Utils.getPortUrl(Port.SCHEDULES, purAirDevice.getIpAddress()), this,"POST") ;
			Thread addSchedulerThread = new Thread(addSchedulerTask) ;
			addSchedulerThread.start() ;
			
			showProgressDialog() ;
			showSchedulerOverviewFragment();
		}
		//TODO - Implement Add scheduler Via CPP
	}
	
	private void updateScheduler() {
		scheduleType = EDIT_SCHEDULE ;
		try {
			JSONObject jo = arrSchedulers.getJSONObject(UDOperationIndex);
			jo.put(SchedulerConstants.TIME, updateSelectedTime);
			jo.put(SchedulerConstants.DAYS, updateSelectedDays);
			jo.put(SchedulerConstants.SPEED, updateSelectedFanspeed);
			
			Bundle b = new Bundle();
			b.putString("events", arrSchedulers.toString());
			getIntent().putExtras(b);
			//showDeleteSchedulerFragment();
		}
		catch(Exception e) {
			ALog.d(ALog.SCHEDULER, "Error in updateScheduler: " + e.getMessage());
		}
	}
	
	
	public void deleteScheduler(int index) {
		ALog.i(ALog.SCHEDULER, "DELETE SCHEDULER: "+index) ;
		scheduleType = DELETE_SCHEDULE ;
		int scheduleNumber = schedulesList.get(index).getScheduleNumber() ;
		if( purAirDevice != null && purAirDevice.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
			String url = Utils.getPortUrl(Port.SCHEDULES, purAirDevice.getIpAddress())+"/"+scheduleNumber ;
			ALog.i(ALog.SCHEDULER, url) ;
			TaskPutDeviceDetails deleteScheduleRunnable = new TaskPutDeviceDetails("", url, this,"DELETE") ;
			Thread deleteScheduleThread = new Thread(deleteScheduleRunnable) ;
			deleteScheduleThread.start() ;
			showProgressDialog() ;
		}		
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			switch (v.getId()) {
			case R.id.scheduler_actionbar_add_btn:
				save();
				break;
				
			case R.id.larrow:
				showPreviousScreen4BackPressed();
				break;
			default:
				break;
			}
		}
	};
	
	public JSONArray getSchedulerList() {
		return arrSchedulers;
	}
	
	public List<Integer> getSchedulerMarked4Deletion() {
		return SchedulerMarked4Deletion;
	}

	public void setActionBar(SchedulerID id) {
		switch (id) {
		case OVERVIEW_EVENT:
			setActionBar(R.string.scheduler, View.INVISIBLE, View.INVISIBLE);
			break;
		case ADD_EVENT:
			if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
				setActionBar(R.string.set_schedule, View.VISIBLE, View.VISIBLE);
			} else {
				setActionBar(R.string.edit_schedule, View.VISIBLE, View.VISIBLE);
			}
			break;
		case DELETE_EVENT:
			setActionBar(R.string.set_schedule, View.INVISIBLE, View.VISIBLE);
			break;
		case REPEAT:
			setActionBar(R.string.repeat_text, View.INVISIBLE, View.VISIBLE);
			break;
		case FAN_SPEED:
			setActionBar(R.string.fanspeed, View.INVISIBLE, View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void setActionBar(int textId, int cancelButton, int backButton) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::setActionBar() method enter");
		actionbarTitle.setText(textId);
		actionBarCancelBtn.setVisibility(cancelButton);
		actionBarBackBtn.setVisibility(backButton);
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::setActionBar() method exit");
	}


	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String time;
		time = String.format("%2d:%02d", hourOfDay, minute);
		// String time = hourOfDay + ":" + minute;
		
		SelectedTime = time;
		SelectedDays = null;
		SelectedFanspeed = null;
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::onTimeSet() method - SelectedTime is " + SelectedTime);
		showAddSchedulerFragment();
	}
	

	public void dispatchInformations(String days) {
		updateCRUDOperationData(SchedulerConstants.EMPTY_STRING, days, SchedulerConstants.EMPTY_STRING, null);
	}

	public void dispatchInformations2(String fanspeed) {
		updateCRUDOperationData(SchedulerConstants.EMPTY_STRING, SchedulerConstants.EMPTY_STRING, fanspeed, null);
	}
	
	public void dispatchInfo4MarkedSchDeletion(List<Integer> markedDelete) {
		updateCRUDOperationData(SchedulerConstants.EMPTY_STRING, SchedulerConstants.EMPTY_STRING, SchedulerConstants.EMPTY_STRING, markedDelete);
	}
	
	public void dispatchInformationsForCRUD(String crud) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::dispatchInformationsForCRUD() method - crud is " + crud);
		CRUDOperation = crud;
	}
	
	public void dispatchInformationsForCRUDIndex(int index) {
		UDOperationIndex = index;
		try {
			JSONObject jo = arrSchedulers.getJSONObject(UDOperationIndex);
			updateSelectedTime = jo.getString(SchedulerConstants.TIME);
			updateSelectedDays = jo.getString(SchedulerConstants.DAYS);
			updateSelectedFanspeed = jo.getString(SchedulerConstants.SPEED);
		} 
		catch (Exception e) {
			ALog.d(ALog.DISCOVERY, "Error in dispatchInformationsForCRUDIndex: " + e.getMessage());
		}
	}
	
	/**
	 * Retrieves the list of schedules from Purifier
	 */
	private void getSchedulesFromPurifier() {
		scheduleType = GET_SCHEDULES ;
		if( purAirDevice != null && 
				purAirDevice.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
			ALog.i(ALog.SCHEDULER, "getAllSchedules: "+purAirDevice.getIpAddress()) ;
			TaskGetHttp getScheduleListRunnable = new TaskGetHttp(Utils.getPortUrl(Port.SCHEDULES, purAirDevice.getIpAddress()
					), this, this) ;
			Thread thread = new Thread(getScheduleListRunnable) ;
			thread.start() ;
			showProgressDialog() ;
		}
	}
	
	private void updateCRUDOperationData(String time, String date, String speed, List<Integer> markedDelete) {
		if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			if (!time.isEmpty())
				SelectedTime = time;
			if (!date.isEmpty())
				SelectedDays = date;
			if (!speed.isEmpty())
				SelectedFanspeed = speed;
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			if (!time.isEmpty())
				updateSelectedTime = time;
			if (!date.isEmpty())
				updateSelectedDays = date;
			if (!speed.isEmpty())
				updateSelectedFanspeed = speed;
		}
		
		if (markedDelete != null) {
			SchedulerMarked4Deletion = markedDelete;
		}
		ALog.d(ALog.DISCOVERY, "SchedulerMarked4Deletion in updateCRUDOperationData: " + SchedulerMarked4Deletion.toString());
	}
	
	private void showSchedulerOverviewFragment() {
		schFragment =  new SchedulerOverviewFragment();
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::showSchedulerOverviewFragment() method enter");
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, schFragment, "SchedulerOverviewFragment").commit();
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::showSchedulerOverviewFragment() method exit");
	}
	
	private void showAddSchedulerFragment() {
		Bundle bundle = new Bundle();
		
		if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			ALog.i(ALog.SCHEDULER, "SchedulerActivity::onTimeSet() method - create SelectedTime is " + SelectedTime);
			bundle.putString(SchedulerConstants.TIME, SelectedTime);
			bundle.putString(SchedulerConstants.DAYS, SelectedDays);
			bundle.putString(SchedulerConstants.SPEED, SelectedFanspeed);
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			ALog.i(ALog.SCHEDULER, "SchedulerActivity::onTimeSet() method - udpate SelectedTime is " + updateSelectedTime);
			bundle.putString(SchedulerConstants.TIME, updateSelectedTime);
			bundle.putString(SchedulerConstants.DAYS, updateSelectedDays);
			bundle.putString(SchedulerConstants.SPEED, updateSelectedFanspeed);
		}
		
		AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
		fragAddSch.setArguments(bundle);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragAddSch, "AddSchedulerFragment").commit();		
	}
	
	/*private void showDeleteSchedulerFragment() {
		DeleteSchedulerFragment fragDeleteSch = new DeleteSchedulerFragment();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragDeleteSch, "DeleteSchedulerFragment").commit();
	}*/
	
	private void showPreviousScreen4BackPressed() {
		
		if (actionbarTitle.getText().equals(SchedulerConstants.SET_SCHEDULE) || actionbarTitle.getText().equals(SchedulerConstants.EDIT_SCHEDULE)) {
			showSchedulerOverviewFragment(); 
		} else if (actionbarTitle.getText().equals(SchedulerConstants.REPEAT) || actionbarTitle.getText().equals(SchedulerConstants.FANSPEED)) {
			showAddSchedulerFragment();
		} else {
			finish();
		}
	}


	@Override
	public void onBackPressed() {
		showPreviousScreen4BackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		setCancelled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSchedulesFromPurifier() ;
		setCancelled(false);
	}

	@Override
	public void onClick(View v) {
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String fromIp) {
		cancelProgressDialog() ;
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
			parseResponse(responseData) ;
			break;

		default:
			break;
		}
	}
	
	private void parseResponse(String response) {
		String decryptedResponse = new DISecurity(null).decryptData(response, purAirDevice);
		schedulesList = DataParser.parseSchedulerDto(decryptedResponse) ;
		ALog.i(ALog.SCHEDULER, "List of schedules: "+schedulesList.size()) ;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (schFragment == null) return;
				schFragment.updateList();
			}
		});
//		getSchedulersDataFromDevice(schedulesList);
	}
	
	public List<ScheduleDto> getSchedulerList1() {
		return schedulesList;
	}
	
	private void getSchedulersDataFromDevice(List<ScheduleDto> scheduleList) {
		arrSchedulers = new JSONArray() ;
		for (ScheduleDto obj : scheduleList) {
			try {
				JSONObject jo = new JSONObject();
				jo.put(SchedulerConstants.TIME, obj.getName());
				arrSchedulers.put(jo);
			}
			catch(Exception e) {
				ALog.i(ALog.SCHEDULER, "SchedulerActivity::getSchedulersDataFromDevice() - getting data failed");
			}
		}
		
		Bundle b = new Bundle();
		b.putString("events", arrSchedulers.toString());
		getIntent().putExtras(b);
		
		if (scheduleType == ADD_SCHEDULE || scheduleType == GET_SCHEDULES ||  scheduleType == DELETE_SCHEDULE) {
			showSchedulerOverviewFragment();
		}
	}
		
	public static void setCancelled(boolean cancelled) {
	}

	public static boolean isCancelled() {
		return cancelled;
	}
}
