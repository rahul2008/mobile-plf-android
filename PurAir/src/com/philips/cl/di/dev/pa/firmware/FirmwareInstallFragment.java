package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.firmware.FirmwareEventDto.FirmwareState;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareInstallFragment extends BaseFragment implements AirPurifierEventListener {
	
	private Thread timerThread;
	private boolean installed = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.installing_firmware, null);
		FontTextView tvInstallingFirmware = (FontTextView) view.findViewById(R.id.installing_firmware_for_purifier_msg);
		tvInstallingFirmware.setText(getString(R.string.installing_firmware_for_purifier_msg, ((FirmwareUpdateActivity) getActivity()).getPurifierName())) ;
		FirmwareUpdateActivity.setCancelled(false);
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_INSTALL);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		timerThread = new Thread(timerRunnable);
//		timerThread.start();
		
		AirPurifierController.getInstance().addAirPurifierEventListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		timerThread = null;
		
		AirPurifierController.getInstance().removeAirPurifierEventListener(this);
	}
	
	private static int counter = 0;
	public static void setCounter(int counter) {
		FirmwareInstallFragment.counter = counter;
	}
	
	Runnable timerRunnable = new Runnable() {
		public void run() {
			while(counter < 60 && !FirmwareUpdateActivity.isCancelled() && !installed) {
				ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$counter " + counter);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter++;
			}
			if(counter >= 60) {
				ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$COUNT > 60 call failed fragment counter " + counter );
				((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally(FirmwareConstants.STATE, FirmwareConstants.CANCEL);
				FirmwareUpdateActivity.setCancelled(true);
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), FirmwareConstants.FIRMWARE_FAILED_SUPPORT_FRAGMENT)
				.commit();
			}
		}
	};
	
//	@Override
//	public void firmwareDataRecieved(String data) {
//		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$firmwareDataRecieved data " + data + " installed " + installed + " FirmwareUpdateActivity.isCancelled() " + FirmwareUpdateActivity.isCancelled());
//		if(installed || FirmwareUpdateActivity.isCancelled()) {
//			return;
//		}
//		if(data == null || data.isEmpty() || data.length() <= 0) {
//			getProps();
//			return;
//		}
//		
//		JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
//		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$jsonObject " + jsonObject);
//		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$jsonObject.get(upgrade) " + jsonObject.get("upgrade"));
//		processFirmwareData(jsonObject);
//		getProps();
//	}
	
//	public void processFirmwareData(JsonObject jsonObject) {
//		String upgradeString = getUpgrade(jsonObject);
//		String stateString = getState(jsonObject);
//		
//		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$upgradeString " + upgradeString);
//		counter = 0;
//		if((stateString.equals(FirmwareConstants.IDLE)) && (upgradeString.isEmpty())) {
//			installed = true;
//			FirmwareUpdateActivity.setCancelled(true);
//			showNextFragment();
//		}
//	}
	
	public void showNextFragment() {
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.firmware_container, new FirmwareInstallSuccessFragment(), FirmwareConstants.FIRMWARE_INSTALL_SUCCESS_FRAGMENT)
		.commit();
	}

	@Override
	public void airPurifierEventReceived(AirPurifierEventDto airPurifierEvent) {
		// NOP
		
	}

	@Override
	public void firmwareEventReceived(FirmwareEventDto firmwareEventDto) {
		ALog.d(ALog.FIRMWARE, "FirmwareInstallFragment$firmwareEventReceived state " + firmwareEventDto.getState() + " Upgrade " + firmwareEventDto.getUpgrade());
		counter = 0;

		if(firmwareEventDto.getUpgrade().isEmpty() && firmwareEventDto.getState() == FirmwareState.IDLE) {
			showNextFragment();
		}
	}
}
