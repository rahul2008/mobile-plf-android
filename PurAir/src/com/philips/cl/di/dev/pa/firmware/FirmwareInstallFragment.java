package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.firmware.FirmwareEventDto.FirmwareState;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareResponseListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareInstallFragment extends BaseFragment implements FirmwareResponseListener {
	
	private Thread timerThread;
	private static boolean installed = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.installing_firmware, null);
		initViews(view);
		FirmwareUpdateActivity.setCancelled(false);
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_INSTALL);
		
		((FirmwareUpdateActivity) getActivity()).setFirmwareUpdateJsonParams(FirmwareConstants.STATE, FirmwareConstants.GO);

		getProps();
		return view;
	}
	
	private void initViews(View view) {
		FontTextView tvInstallingFirmware = (FontTextView) view.findViewById(R.id.installing_firmware_for_purifier_msg);
		tvInstallingFirmware.setText(getString(R.string.installing_firmware_for_purifier_msg, ((FirmwareUpdateActivity) getActivity()).getPurifierName())) ;
		FontTextView currentVersion = (FontTextView) view.findViewById(R.id.current_version_number);
		currentVersion.setText(getString(R.string.firmware_current_version) + " " + ((FirmwareUpdateActivity) getActivity()).getCurrentVersion());
		FontTextView upgradeVersion = (FontTextView) view.findViewById(R.id.upgrade_version_number);
		upgradeVersion.setText(getString(R.string.firmware_new_version) + " " + ((FirmwareUpdateActivity) getActivity()).getUpgradeVersion());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getProps();
		timerThread = new Thread(timerRunnable);
		timerThread.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		timerThread = null;
	}
	
	private void getProps() {
		String firmwareUrl = Utils.getPortUrl(Port.FIRMWARE, Utils.getIPAddress());
		FirmwareUpdateTask task = new FirmwareUpdateTask(FirmwareInstallFragment.this);
		task.execute(firmwareUrl);
	}
	
	private static int counter = 0;
	public static void setCounter(int counter) {
		FirmwareInstallFragment.counter = counter;
	}
	
	Runnable timerRunnable = new Runnable() {
		public void run() {
			while(counter < 90 && !FirmwareUpdateActivity.isCancelled()) {
				ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$counter " + counter);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter++;
			}
			if(counter >= 90) {
				ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$COUNT > 60 call failed fragment counter " + counter );
				((FirmwareUpdateActivity) getActivity()).setFirmwareUpdateJsonParams(FirmwareConstants.STATE, FirmwareConstants.CANCEL);
				FirmwareUpdateActivity.setCancelled(true);
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), FirmwareFailedSupportFragment.class.getSimpleName())
				.commit();
			}
		}
	};
	
	@Override
	public void firmwareDataRecieved(String data) {
		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$firmwareDataRecieved data " + data);
		if(installed || FirmwareUpdateActivity.isCancelled()) {
			return;
		}
		
		FirmwareEventDto firmwareEventDto = DataParser.parseFirmwareEventData(data);
		
		if(firmwareEventDto == null) {
			getProps();
			return;
		}
		ALog.i(ALog.FIRMWARE, "(state == IDLE) " + (firmwareEventDto.getState() == FirmwareState.IDLE));
		counter = 0;
		if(firmwareEventDto.getState() == FirmwareState.IDLE) {
			ALog.i(   ALog.FIRMWARE, "Firmware install was successful");
			installed = true;
			FirmwareUpdateActivity.setCancelled(true);
			showNextFragment();
		}
		
		getProps();
	}
	
	public void showNextFragment() {
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.firmware_container, new FirmwareInstallSuccessFragment(), FirmwareInstallSuccessFragment.class.getSimpleName())
		.commit();
	}
}
