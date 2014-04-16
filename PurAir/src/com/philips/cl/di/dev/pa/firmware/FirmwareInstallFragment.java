package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareResponseListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareInstallFragment extends BaseFragment implements FirmwareResponseListener {
	
	private Thread timerThread;
	private static boolean installed = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.installing_firmware, null);
		FontTextView tvInstallingFirmware = (FontTextView) view.findViewById(R.id.installing_firmware_for_purifier_msg);
		tvInstallingFirmware.setText(getString(R.string.installing_firmware_for_purifier_msg, ((FirmwareUpdateActivity) getActivity()).getPurifierName())) ;
		FirmwareUpdateActivity.setCancelled(false);
		getProps();
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_INSTALL);
		return view;
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
			while(counter < 60 && !FirmwareUpdateActivity.isCancelled()) {
				ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$counter " + counter);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter++;
			}
			if(counter >= 60) {
				ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$COUNT > 60 call failed fragment counter " + counter );
				((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally(FirmwareConstants.STATE, FirmwareConstants.CANCEL);
				FirmwareUpdateActivity.setCancelled(true);
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), FirmwareConstants.FIRMWARE_FAILED_SUPPORT_FRAGMENT)
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
		if(data == null || data.isEmpty() || data.length() <= 0) {
			getProps();
			return;
		}
		
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$jsonObject " + jsonObject);
		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$jsonObject.get(upgrade) " + jsonObject.get("upgrade"));
		processFirmwareData(jsonObject);
		getProps();
	}
	
	public void processFirmwareData(JsonObject jsonObject) {
		String upgradeString = getUpgrade(jsonObject);
		String stateString = getState(jsonObject);
		
		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$upgradeString " + upgradeString);
		counter = 0;
		if((stateString.equals(FirmwareConstants.IDLE)) && (upgradeString.trim().length() <= 0)) {
			installed = true;
			FirmwareUpdateActivity.setCancelled(true);
			showNextFragment();
		}
	}
	
	public String getUpgrade(JsonObject jsonObject) {
		JsonElement progressElemt = jsonObject.get(FirmwareConstants.UPGRADE);
		return progressElemt.getAsString();
	}

	public String getState(JsonObject jsonObject) {
		JsonElement stateElemt = jsonObject.get(FirmwareConstants.STATE);
		return stateElemt.getAsString();
	}
	
	public void showNextFragment() {
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.firmware_container, new FirmwareInstallSuccessFragment(), FirmwareConstants.FIRMWARE_INSTALL_SUCCESS_FRAGMENT)
		.commit();
	}
}
