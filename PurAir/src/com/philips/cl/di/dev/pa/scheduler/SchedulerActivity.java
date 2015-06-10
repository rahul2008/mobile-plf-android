package com.philips.cl.di.dev.pa.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.port.common.ScheduleListPort;
import com.philips.cdp.dicommclient.port.common.ScheduleListPortInfo;
import com.philips.cdp.dicommclient.port.common.SchedulePortListener;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.ews.WifiNetworkCallback;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dicomm.port.AirPort;

public class SchedulerActivity extends BaseActivity implements SchedulePortListener, DiscoveryEventListener {

    private static boolean cancelled;
	private String selectedDays = "";
	private String selectedTime;
	private String selectedFanspeed = SchedulerConstants.DEFAULT_FANSPEED_SCHEDULER;
	private boolean enabled = true;

	private SCHEDULE_TYPE scheduleType;
	private AirPurifier purAirDevice;
	private List<Integer> SchedulerMarked4Deletion = new ArrayList<Integer>();
	private SchedulerOverviewFragment schFragment;

	private List<ScheduleListPortInfo> schedulesList;
	private ProgressDialog progressDialog;
	private int schedulerNumberSelected;
	private int indexSelected;
	private SchedulerID event;
	private WifiNetworkCallback networkCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler_container);
		SchedulerMarked4Deletion.clear();
		showSchedulerOverviewFragment();
		purAirDevice = AirPurifierManager.getInstance().getCurrentPurifier();
		if (purAirDevice != null) {
			schedulesList = purAirDevice.getScheduleListPort().getSchedulePortInfoList();
			purAirDevice.getScheduleListPort().setSchedulePortListener(this);
		}
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
		
		if (purAirDevice == null) return;
		AirPort airPort = purAirDevice.getAirPort();
		purAirDevice.getScheduleListPort().addSchedule(airPort.getDICommPortName(), airPort.getDICommProductId(), selectedTime, selectedDays, enabled, createCommandMap());
		showProgressDialog();
	}

	private Map<String, Object> createCommandMap() {
		int pwr = 1;
		if (selectedFanspeed.equals("0")) {
			pwr = 0;
		}
		
		Map<String, Object> commandMap = new HashMap<String, Object>();
		commandMap.put(ParserConstants.POWER_MODE, pwr);
		commandMap.put(ParserConstants.MACHINE_MODE, selectedFanspeed);
		return commandMap;
	}

	/**
	 * This method is used to update the current scheduler.
	 * 
	 */
	public void updateScheduler() {

		scheduleType = SCHEDULE_TYPE.EDIT;
		if (!selectedDays.equals(schedulesList.get(indexSelected).getDays())
				|| !selectedFanspeed.equals(schedulesList.get(indexSelected).getMode())
				|| !selectedTime.equals(schedulesList.get(indexSelected).getScheduleTime())
				|| enabled != schedulesList.get(indexSelected).isEnabled()) {
			showProgressDialog();
			if(null!=purAirDevice) {
				AirPort airPort = purAirDevice.getAirPort();
			    purAirDevice.getScheduleListPort().updateSchedule(schedulerNumberSelected, airPort.getDICommPortName(), airPort.getDICommProductId(), selectedTime, selectedDays, enabled, createCommandMap());			
			}
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
		if (purAirDevice == null || purAirDevice.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED) {
			return;
		}

		purAirDevice.getScheduleListPort().deleteSchedule(schedulerNumberSelected);
		showProgressDialog();
	}

	/**
	 * This is called from
	 */
	public void save() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::Save() method enter");
		if (selectedDays.equals(getString(R.string.onetime))) selectedDays = "";
			
		if (scheduleType == SCHEDULE_TYPE.ADD) {
			addScheduler();
		} else if (scheduleType == SCHEDULE_TYPE.GET_SCHEDULE_DETAILS) {
			updateScheduler();
		}
	}

	public List<Integer> getSchedulerMarked4Deletion() {
		return SchedulerMarked4Deletion;
	}

	public void setTime(int hourOfDay, int minute) {
		selectedTime = String.format("%2d:%02d", hourOfDay, minute);;
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
		if (purAirDevice == null || purAirDevice.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED) {
			return;
		}

		purAirDevice.getScheduleListPort().getSchedules();
	}

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

	public void showAddSchedulerFragment() {
		Bundle bundle = new Bundle();
		if (scheduleType == SCHEDULE_TYPE.ADD
				|| scheduleType == SCHEDULE_TYPE.EDIT
				|| scheduleType == SCHEDULE_TYPE.GET_SCHEDULE_DETAILS) {
			bundle.putString(SchedulerConstants.TIME, selectedTime);
			bundle.putString(SchedulerConstants.DAYS, selectedDays);
			bundle.putString(SchedulerConstants.SPEED, selectedFanspeed);
			bundle.putBoolean(SchedulerConstants.ENABLED, enabled);
			bundle.putString(SchedulerConstants.HEADING, getString(R.string.set_schedule));
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
		schedulerNumberSelected = schedulesList.get(position).getScheduleNumber();
		indexSelected = position;
		if (schedulesList.get(position).getMode() == null) {
			showProgressDialog();
			if(null!=purAirDevice){
			    purAirDevice.getScheduleListPort().getScheduleDetails(schedulerNumberSelected);
			}
		} else {
			setFanSpeed(schedulesList.get(position).getMode());
			setDays(schedulesList.get(position).getDays());
			setTime(schedulesList.get(position).getScheduleTime());
			showEditFragment(schedulesList.get(position));
		}
	}

	private void showEditFragment(ScheduleListPortInfo schedulePortInfo) {
		Bundle bundle = new Bundle();
		bundle.putString(SchedulerConstants.TIME, schedulePortInfo.getScheduleTime());
		bundle.putString(SchedulerConstants.DAYS, schedulePortInfo.getDays());
		bundle.putString(SchedulerConstants.SPEED, schedulePortInfo.getMode());
		bundle.putBoolean(SchedulerConstants.ENABLED, schedulePortInfo.isEnabled());
		bundle.putString(SchedulerConstants.HEADING, getString(R.string.edit_schedule));
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
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.ll_scheduler_container);


		if (fragment instanceof AddSchedulerFragment) {
			showSchedulerOverviewFragment();
		} else if (fragment instanceof RepeatFragment || fragment instanceof FanspeedFragment) {
			showAddSchedulerFragment();
		} else {
			finish();
		}
	}

	// Add new schedule
	public void initializeDayAndFanspeedTime() {
		selectedTime = "";
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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			unregisterWifiNetworkForSocket();
		}
		DiscoveryManager.getInstance().stop();
		setCancelled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.SCHEDULE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			registerWifiNetworkForSocket();
		} else {
			DiscoveryManager.getInstance().start(this);
		}
		
		if (schedulesList == null || schedulesList.size() == 0) {
			getSchedulesFromPurifier();
		} else {
			if (schFragment == null || event != SchedulerID.OVERVIEW_EVENT)
				return;
			schFragment.updateList();
		}
		setCancelled(false);
	}

	public List<ScheduleListPortInfo> getSchedulerList() {
		return schedulesList;
	}

	public static void setCancelled(boolean cancelled) {
	}

	public static boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void onSchedulesReceived(List<ScheduleListPortInfo> scheduleList) {
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
	public void onScheduleReceived(ScheduleListPortInfo schedule) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cancelProgressDialog();
			}
		});

		for (ScheduleListPortInfo schedulerPortInfo : schedulesList) {
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
	public void onError(final int errorType) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cancelProgressDialog();
				if (errorType == ScheduleListPort.MAX_SCHEDULES_REACHED) {
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
	public void onDiscoveredAppliancesListChanged() {

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
	
	@SuppressLint("NewApi")
	private void registerWifiNetworkForSocket() {
		NetworkRequest.Builder request = new NetworkRequest.Builder();
		request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

		final Object lock = new Object();
		networkCallback = new WifiNetworkCallback(lock);

		synchronized (lock) {
			connectivityManager.registerNetworkCallback(request.build(),	networkCallback);
			try {
				lock.wait(2000);
				Log.e(ALog.WIFI, "Timeout error occurred");
			} catch (InterruptedException e) {
			}
		}
		if(networkCallback.getNetwork() != null) {
			ConnectivityManager.setProcessDefaultNetwork(networkCallback.getNetwork());
		}
		DiscoveryManager.getInstance().start(this);
	}
	
	@SuppressLint("NewApi")
	private void  unregisterWifiNetworkForSocket() {
//		if (networkCallback != null) {
//			connectivityManager.unregisterNetworkCallback(networkCallback);
//			networkCallback = null ;
//		}
	}

}
