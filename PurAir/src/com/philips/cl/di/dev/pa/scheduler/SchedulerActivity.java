package com.philips.cl.di.dev.pa.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerActivity extends BaseActivity implements OnTimeSetListener, SchedulerListener, DiscoveryEventListener {

	private static boolean cancelled;
	private Button actionBarCancelBtn;
	private ImageButton actionBarBackBtn;
	private FontTextView actionbarTitle;
	private String selectedDays = "";
	private String selectedTime;
	private String selectedFanspeed = SchedulerConstants.DEFAULT_FANSPEED_SCHEDULER;
	private boolean enabled = true;

	private SCHEDULE_TYPE scheduleType;
	private PurAirDevice purAirDevice;
	private List<Integer> SchedulerMarked4Deletion = new ArrayList<Integer>();
	private SchedulerOverviewFragment schFragment;

	private List<SchedulePortInfo> schedulesList;
	private ProgressDialog progressDialog;
	private int schedulerNumberSelected;
	private int indexSelected;
	private SchedulerID event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler_container);
		SchedulerMarked4Deletion.clear();
		initActionBar();
		showSchedulerOverviewFragment();
		purAirDevice = PurifierManager.getInstance().getCurrentPurifier();
		if (purAirDevice != null)
			schedulesList = purAirDevice.getmSchedulerPortInfoList();
		PurifierManager.getInstance().setSchedulerListener(this);

	}

	private void initActionBar() {
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		Drawable d = getResources().getDrawable(R.drawable.ews_nav_bar_2x);
		actionBar.setBackgroundDrawable(d);

		View view = getLayoutInflater().inflate(R.layout.scheduler_actionbar,
				null);
		actionbarTitle = (FontTextView) view
				.findViewById(R.id.scheduler_actionbar_title);
		actionbarTitle.setText(getString(R.string.scheduler));
		// If Chinese language selected set font-type-face normal
		if (LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains(
				"ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault())
						.contains("ZH-HANT")) {
			actionbarTitle.setTypeface(Typeface.DEFAULT);
		}

		actionBarCancelBtn = (Button) view
				.findViewById(R.id.scheduler_actionbar_add_btn);
		actionBarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionBarCancelBtn.setOnClickListener(onClickListener);

		actionBarBackBtn = (ImageButton) view.findViewById(R.id.larrow);
		actionBarBackBtn.setOnClickListener(onClickListener);

		actionBar.setCustomView(view);
	}

	public void setActionBar(SchedulerID id) {
		event = id;
		switch (id) {
		case OVERVIEW_EVENT:
			setActionBar(R.string.scheduler, View.INVISIBLE, View.VISIBLE);
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

	public void setSchedulerType(SCHEDULE_TYPE scheduleType) {
		this.scheduleType = scheduleType;
	}

	public void setDays(String days) {
		ALog.i(ALog.SCHEDULER, "Selected days: " + days);
		selectedDays = days;
	}

	public void setFanSpeed(String fanspeed) {
		selectedFanspeed = fanspeed;
	}

	public void setTime(String time) {
		selectedTime = time;
	}

	public void isEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * This method is used to add a new scheduler to the Purifier
	 */
	private void addScheduler() {
		scheduleType = SCHEDULE_TYPE.ADD;
		ALog.i(ALog.SCHEDULER, "createScheduler");
		String addSchedulerJson = "";

		if (purAirDevice == null)
			return;
		addSchedulerJson = JSONBuilder.getSchedulesJson(selectedTime,
				selectedFanspeed, selectedDays, enabled);
		PurifierManager.getInstance().sendScheduleDetailsToPurifier(
				addSchedulerJson, purAirDevice, scheduleType, -1);
		showProgressDialog();
		// TODO - Implement Add scheduler Via CPP
	}

	/**
	 * This method is used to update the current scheduler.
	 * 
	 */
	public void updateScheduler() {

		scheduleType = SCHEDULE_TYPE.EDIT;
		String editSchedulerJson = "";
		if (!selectedDays.equals(schedulesList.get(indexSelected).getDays())
				|| !selectedFanspeed.equals(schedulesList.get(indexSelected)
						.getMode())
				|| !selectedTime.equals(schedulesList.get(indexSelected)
						.getScheduleTime())
				|| enabled != schedulesList.get(indexSelected).isEnabled()) {
			showProgressDialog();
			editSchedulerJson = JSONBuilder.getSchedulesJson(selectedTime,
					selectedFanspeed, selectedDays, enabled);
			PurifierManager.getInstance().sendScheduleDetailsToPurifier(
					editSchedulerJson, purAirDevice, scheduleType,
					schedulerNumberSelected);
		} else {
			showSchedulerOverviewFragment();
		}
	}

	/**
	 * Delete the scheduler
	 * 
	 * @param index
	 */
	public void deleteScheduler(int index) {
		ALog.i(ALog.SCHEDULER, "DELETE SCHEDULER: " + index);
		scheduleType = SCHEDULE_TYPE.DELETE;
		schedulerNumberSelected = schedulesList.get(index).getScheduleNumber();
		indexSelected = index;
		if (purAirDevice == null
				|| purAirDevice.getConnectionState() == ConnectionState.DISCONNECTED)
			return;
		PurifierManager.getInstance().sendScheduleDetailsToPurifier("",
				purAirDevice, scheduleType, schedulerNumberSelected);
		showProgressDialog();
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			switch (v.getId()) {
			case R.id.scheduler_actionbar_add_btn:
				save();
				break;
			case R.id.larrow:
				showPreviousScreenOnBackPressed();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * This is called from
	 */
	private void save() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::Save() method enter");
		if (selectedDays.equals(getString(R.string.onetime)))
			selectedDays = "";

		if (scheduleType == SCHEDULE_TYPE.ADD) {
			addScheduler();
		} else if (scheduleType == SCHEDULE_TYPE.GET_SCHEDULE_DETAILS) {
			updateScheduler();
		}
	}

	public List<Integer> getSchedulerMarked4Deletion() {
		return SchedulerMarked4Deletion;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String time;
		time = String.format("%2d:%02d", hourOfDay, minute);
		// String time = hourOfDay + ":" + minute;

		selectedTime = time;
		ALog.i(ALog.SCHEDULER,
				"SchedulerActivity::onTimeSet() method - SelectedTime is "
						+ selectedTime);
		showAddSchedulerFragment();
	}

	private void showProgressDialog() {
		try {
			cppTimer.start();
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(getString(R.string.please_wait));
			progressDialog.setCancelable(false);
			progressDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cancelProgressDialog() {
		try {
			cppTimer.cancel();
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the list of schedules from Purifier
	 */
	private void getSchedulesFromPurifier() {
		scheduleType = SCHEDULE_TYPE.GET;
		showProgressDialog();
		if (purAirDevice == null
				|| purAirDevice.getConnectionState() == ConnectionState.DISCONNECTED)
			return;
		String dataToSend = "";
		PurifierManager.getInstance().sendScheduleDetailsToPurifier(dataToSend,
				purAirDevice, scheduleType, -1);
	}

	/**
	 * 
	 * @param time
	 * @param date
	 * @param speed
	 * @param markedDelete
	 */
	// private void setDetails(List<Integer> markedDelete) {
	// if (markedDelete != null) {
	// SchedulerMarked4Deletion = markedDelete;
	// }
	// ALog.d(ALog.DISCOVERY,
	// "SchedulerMarked4Deletion in updateCRUDOperationData: " +
	// SchedulerMarked4Deletion.toString());
	// }

	private void showSchedulerOverviewFragment() {
		try {
			schFragment = new SchedulerOverviewFragment();
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.ll_scheduler_container, schFragment,
							"SchedulerOverviewFragment").commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.SCHEDULER, "Error: " + e.getMessage());
		}
	}

	private void showAddSchedulerFragment() {
		Bundle bundle = new Bundle();
		if (scheduleType == SCHEDULE_TYPE.ADD
				|| scheduleType == SCHEDULE_TYPE.EDIT
				|| scheduleType == SCHEDULE_TYPE.GET_SCHEDULE_DETAILS) {
			bundle.putString(SchedulerConstants.TIME, selectedTime);
			bundle.putString(SchedulerConstants.DAYS, selectedDays);
			bundle.putString(SchedulerConstants.SPEED, selectedFanspeed);
			bundle.putBoolean(SchedulerConstants.ENABLED, enabled);
		}

		try {
			AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
			fragAddSch.setArguments(bundle);
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.ll_scheduler_container, fragAddSch,
							"AddSchedulerFragment").commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.SCHEDULER, "Error: " + e.getMessage());
		}
	}

	public void onEditScheduler(int position) {
		scheduleType = SCHEDULE_TYPE.GET_SCHEDULE_DETAILS;
		// updateFragment ;
		schedulerNumberSelected = schedulesList.get(position)
				.getScheduleNumber();
		indexSelected = position;
		if (schedulesList.get(position).getMode() == null) {
			showProgressDialog();
			String dataToSend = "";
			PurifierManager.getInstance().sendScheduleDetailsToPurifier(
					dataToSend, purAirDevice, scheduleType,
					schedulerNumberSelected);
		} else {
			setFanSpeed(schedulesList.get(position).getMode());
			setDays(schedulesList.get(position).getDays());
			setTime(schedulesList.get(position).getScheduleTime());
			showEditFragment(schedulesList.get(position));
		}
	}

	private void showEditFragment(SchedulePortInfo schedulePortInfo) {
		Bundle bundle = new Bundle();
		bundle.putString(SchedulerConstants.TIME,
				schedulePortInfo.getScheduleTime());
		bundle.putString(SchedulerConstants.DAYS, schedulePortInfo.getDays());
		bundle.putString(SchedulerConstants.SPEED, schedulePortInfo.getMode());
		bundle.putBoolean(SchedulerConstants.ENABLED,
				schedulePortInfo.isEnabled());
		AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
		fragAddSch.setArguments(bundle);
		try {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.ll_scheduler_container, fragAddSch,
							"EditSchedulerFragment").commit();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}

	private void showPreviousScreenOnBackPressed() {

		if (actionbarTitle.getText().equals(getString(R.string.set_schedule))
				|| actionbarTitle.getText().equals(
						getString(R.string.edit_schedule))) {
			showSchedulerOverviewFragment();
		} else if (actionbarTitle.getText().equals(
				getString(R.string.repeat_text))
				|| actionbarTitle.getText()
						.equals(getString(R.string.fanspeed))) {
			showAddSchedulerFragment();
		} else {
			finish();
		}
	}

	// Add new schedule
	public void initializeDayAndFanspeed() {
		selectedDays = "";
		selectedFanspeed = "";
	}

	@Override
	public void onBackPressed() {
		showPreviousScreenOnBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		DiscoveryManager.getInstance().stop();
		setCancelled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage("schedule");
		DiscoveryManager.getInstance().start(this);
		if (schedulesList == null || schedulesList.size() == 0) {
			getSchedulesFromPurifier();
		} else {
			if (schFragment == null || event != SchedulerID.OVERVIEW_EVENT)
				return;
			schFragment.updateList();
		}
		setCancelled(false);
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
		if (scheduleList != null) {
			Collections.sort(scheduleList);
			this.schedulesList = scheduleList;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					cancelProgressDialog();
					if (scheduleType == SCHEDULE_TYPE.ADD) {
						MetricsTracker.trackActionAddSchedule();
						showSchedulerOverviewFragment();
					} else {
						schFragment.updateList();
					}
				}
			});
		}
	}

	@Override
	public void onScheduleReceived(SchedulePortInfo schedule) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cancelProgressDialog();
			}
		});

		for (SchedulePortInfo schedulerPortInfo : schedulesList) {
			if (schedulerPortInfo.getScheduleNumber() == schedulerNumberSelected) {
				selectedDays = schedule.getDays();
				schedulerPortInfo.setDays(selectedDays);
				selectedFanspeed = schedule.getMode();
				schedulerPortInfo.setMode(selectedFanspeed);
				selectedTime = schedule.getScheduleTime();
				schedulerPortInfo.setScheduleTime(selectedTime);
				schedulerPortInfo.setEnabled(schedule.isEnabled());
				schedulerPortInfo.setName(schedule.getName());
			}
		}

		if (scheduleType == SCHEDULE_TYPE.GET_SCHEDULE_DETAILS) {
			showEditFragment(schedule);
		} else if (scheduleType == SCHEDULE_TYPE.EDIT) {
			showSchedulerOverviewFragment();
		}

	}

	@Override
	public void onErrorOccurred(final int errorType) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cancelProgressDialog();
				if (errorType == SchedulerHandler.MAX_SCHEDULES_REACHED) {
					showErrorDialog(getString(R.string.error_title),
							getString(R.string.max_schedules_reached));
				}
				if (scheduleType == SCHEDULE_TYPE.DELETE) {
					schFragment.updateList();
				}
			}
		});

	}

	/**
	 * Show alert dialog AQI historic data download failed
	 */
	private void showErrorDialog(String title, String message) {
		try {
			FragmentTransaction fragTransaction = getSupportFragmentManager()
					.beginTransaction();

			Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(
					"error_schedules");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}

			fragTransaction.add(
					DownloadAlerDialogFragement.newInstance(title, message),
					"error_schedules").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.SCHEDULER, "Error: " + e.getMessage());
		}
	}

	@Override
	public void onDiscoveredDevicesListChanged() {

	}

	private CountDownTimer cppTimer = new CountDownTimer(30000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			cancelProgressDialog();
			if (scheduleType == SCHEDULE_TYPE.DELETE) {
				schFragment.updateList();
			}
		}
	};
}
