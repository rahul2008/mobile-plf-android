package com.philips.cl.di.dev.pa.scheduler;

import java.util.ArrayList;
import java.util.List;

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
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerActivity extends BaseActivity implements OnClickListener,
		OnTimeSetListener, SchedulerListener {

	private static boolean cancelled;
	private Button actionBarCancelBtn;
	private Button actionBarBackBtn;
	private FontTextView actionbarTitle;
	private String selectedDays = "";
	private String selectedTime;
	private String selectedFanspeed = SchedulerConstants.DEFAULT_FANSPEED_SCHEDULER;
	
	private SCHEDULE_TYPE scheduleType;
	private PurAirDevice purAirDevice ;
	private List<Integer> SchedulerMarked4Deletion = new ArrayList<Integer>();
	private SchedulerOverviewFragment schFragment;
	
	private List<SchedulePortInfo> schedulesList ;
	private ProgressDialog progressDialog ;
	private int schedulerNumberSelected ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler_container);
		SchedulerMarked4Deletion.clear();
		initActionBar();
		showSchedulerOverviewFragment();
		purAirDevice = PurifierManager.getInstance().getCurrentPurifier() ;
		if( purAirDevice != null)	schedulesList = purAirDevice.getmSchedulerPortInfoList() ;
		PurifierManager.getInstance().setSchedulerListener(this) ;
		
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
		if (scheduleType == SCHEDULE_TYPE.ADD) {
			addScheduler();
		} else if (scheduleType == SCHEDULE_TYPE.EDIT) {
			updateScheduler();
		}
	}
	
	/**
	 * Create the scheduler
	 */
	private void addScheduler() {
		scheduleType = SCHEDULE_TYPE.ADD ;
		ALog.i(ALog.SCHEDULER, "createScheduler") ;
		String addSchedulerJson = "" ;
		
		if(purAirDevice == null) return ;
		addSchedulerJson = JSONBuilder.getSchedulesJson(selectedTime, selectedFanspeed, selectedDays, true) ;
		PurifierManager.getInstance().sendScheduleDetailsToPurifier(addSchedulerJson, purAirDevice,scheduleType,-1) ;
		showProgressDialog() ;
		//TODO - Implement Add scheduler Via CPP
	}
	
	public void updateScheduler() {
		scheduleType = SCHEDULE_TYPE.EDIT ;
	}
	
	/**
	 * Delete the scheduler
	 * @param index
	 */
	public void deleteScheduler(int index) {
		ALog.i(ALog.SCHEDULER, "DELETE SCHEDULER: "+index) ;
		scheduleType = SCHEDULE_TYPE.DELETE ;
		schedulerNumberSelected = schedulesList.get(index).getScheduleNumber() ;
		if( purAirDevice == null || purAirDevice.getConnectionState() == ConnectionState.DISCONNECTED ) return ;
		PurifierManager.getInstance().sendScheduleDetailsToPurifier("", purAirDevice, scheduleType, schedulerNumberSelected) ;
		showProgressDialog() ;
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
	
	public List<Integer> getSchedulerMarked4Deletion() {
		return SchedulerMarked4Deletion;
	}

	SchedulerID event;
	public void setActionBar(SchedulerID id) {
		event = id;
		switch (id) {
		case OVERVIEW_EVENT:
			setActionBar(R.string.scheduler, View.INVISIBLE, View.INVISIBLE);
			break;
		case ADD_EVENT:
			if (scheduleType == SCHEDULE_TYPE.ADD) {
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
		
		selectedTime = time;
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::onTimeSet() method - SelectedTime is " + selectedTime);
		showAddSchedulerFragment();
	}
	

	public void dispatchInformations(String days) {
		updateCRUDOperationData(SchedulerConstants.EMPTY_STRING, days, SchedulerConstants.EMPTY_STRING, null);
	}

	public void dispatchInformations2(String fanspeed) {
		updateCRUDOperationData(SchedulerConstants.EMPTY_STRING, SchedulerConstants.EMPTY_STRING, fanspeed, null);
	}
	
	public void dispatchInformationsForCRUD(SCHEDULE_TYPE scheduleType) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::dispatchInformationsForCRUD() method - crud is " + scheduleType);
		this.scheduleType = scheduleType;
	}
	
	/**
	 * Retrieves the list of schedules from Purifier
	 */
	private void getSchedulesFromPurifier() {
		scheduleType = SCHEDULE_TYPE.GET ;
		showProgressDialog() ;
		if( purAirDevice == null || purAirDevice.getConnectionState() ==
				ConnectionState.DISCONNECTED) return ;
		String dataToSend = "";
		PurifierManager.getInstance().sendScheduleDetailsToPurifier(dataToSend, purAirDevice, scheduleType,-1) ;
	}
	
	private void updateCRUDOperationData(String time, String date, String speed, List<Integer> markedDelete) {
			if (!time.isEmpty())
				selectedTime = time;
			if (!date.isEmpty() && !date.equals(SchedulerConstants.ONE_TIME))
				selectedDays = date;
			if (!speed.isEmpty())
				selectedFanspeed = speed;
		
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
	}
	
	private void showAddSchedulerFragment() {
		Bundle bundle = new Bundle();
		
		if (scheduleType == SCHEDULE_TYPE.ADD || scheduleType == SCHEDULE_TYPE.EDIT) {
			ALog.i(ALog.SCHEDULER, "SchedulerActivity::onTimeSet() method - create SelectedTime is " + selectedTime);
			bundle.putString(SchedulerConstants.TIME, selectedTime);
			bundle.putString(SchedulerConstants.DAYS, selectedDays);
			bundle.putString(SchedulerConstants.SPEED, selectedFanspeed);
		} 
		
		AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
		fragAddSch.setArguments(bundle);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragAddSch, "AddSchedulerFragment").commit();		
	}
	
	public void onEditScheduler(int position) {
		scheduleType = SCHEDULE_TYPE.GET_SCHEDULE_DETAILS ;
		//updateFragment ;
		schedulerNumberSelected = schedulesList.get(position).getScheduleNumber() ;
		if( schedulesList.get(position).getMode() == null ) {
			String dataToSend = "";
			PurifierManager.getInstance().sendScheduleDetailsToPurifier(dataToSend, purAirDevice, scheduleType,schedulerNumberSelected) ;
		}
		else {
			showEditFragment(schedulesList.get(position)) ;
		}
		
		
//		Bundle bundle = new Bundle();
//		if (scheduleType == SCHEDULE_TYPE.EDIT){
//			bundle.putInt(SchedulerConstants.EXTRAT_EDIT, position);
//			bundle.putString(SchedulerConstants.TIME, name);
//		}
//		
//		AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
//		fragAddSch.setArguments(bundle);
//		getSupportFragmentManager()
//				.beginTransaction()
//				.replace(R.id.ll_scheduler_container, fragAddSch, "EditSchedulerFragment").commit();	
	}

	
	private void showEditFragment(SchedulePortInfo schedulePortInfo) {
		Bundle bundle = new Bundle();
		bundle.putString(SchedulerConstants.TIME, schedulePortInfo.getScheduleTime());
		bundle.putString(SchedulerConstants.DAYS, schedulePortInfo.getDays());
		bundle.putString(SchedulerConstants.SPEED, schedulePortInfo.getMode());
		AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
		fragAddSch.setArguments(bundle);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragAddSch, "EditSchedulerFragment").commit();	

	}
	
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
		if( schedulesList == null || schedulesList.size() == 0 ) {
			getSchedulesFromPurifier() ;
		}
		else {
			if (schFragment == null || event != SchedulerID.OVERVIEW_EVENT) return;
			schFragment.updateList();
		}
		setCancelled(false);
	}

	@Override
	public void onClick(View v) {
	}
	
	private void parseResponse(String response) {
		String decryptedResponse = new DISecurity(null).decryptData(response, purAirDevice);
		schedulesList = DataParser.parseSchedulerDto(decryptedResponse) ;
		//purAirDevice.setmSchedulerPortInfoList(schedulesList) ;
		
	}
	
	public List<SchedulePortInfo> getSchedulerList() {
		return schedulesList;
	}
		
	public static void setCancelled(boolean cancelled) {
	}

	public static boolean isCancelled() {
		return cancelled;
	}	
	
	@Override
	public void onSchedulesReceived(List<SchedulePortInfo> scheduleList) {
		ALog.i(ALog.SCHEDULER, "onSchedulers list response");
		cancelProgressDialog() ;
		if( scheduleList != null ) {
			this.schedulesList = scheduleList ;
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					if( scheduleType == SCHEDULE_TYPE.ADD) {
						showSchedulerOverviewFragment() ;
					}
					else {
						schFragment.updateList() ;
					}
				}
			}) ;
		}
	}

	@Override
	public void onScheduleReceived(SchedulePortInfo schedule) {
		for(SchedulePortInfo schedulerPortInfo: schedulesList) {
			if( schedulerPortInfo.getScheduleNumber() == schedulerNumberSelected) {
				schedulerPortInfo.setDays(schedule.getDays()) ;
				schedulerPortInfo.setMode(schedule.getMode()) ;
				schedulerPortInfo.setScheduleTime(schedule.getScheduleTime()) ;
				schedulerPortInfo.setEnabled(schedule.isEnabled()) ;
			}
		}
		showEditFragment(schedule) ;
		
	}
}
