package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareUpdatesListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareDownloadFragment extends BaseFragment implements FirmwareUpdatesListener{

	private ProgressBar progressBar;
	private FontTextView progressPercent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.downloading_firmware, null);
		progressBar = (ProgressBar) view.findViewById(R.id.downloading_progressbar);
		counter = 0;
		Thread timerThread = new Thread(timerRunnable);
		timerThread.start();
		getProps();
		progressPercent = (FontTextView) view.findViewById(R.id.progressbar_increasestatus);
		progressPercent.setText("0%");
		return view;
	}

	private void getProps() {
		ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$getProps");
		String firmwareUrl = String.format(AppConstants.URL_FIRMWARE_PORT, Utils.getIPAddress(getActivity()));
		FirmwareUpdateTask task = new FirmwareUpdateTask(FirmwareDownloadFragment.this);
		task.execute(firmwareUrl);
	}
	
	private static int counter = 0;
	
	Runnable timerRunnable = new Runnable() {
		public void run() {
			while(counter < 60) {
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
				((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally("state", "cancel");
				if(((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount() > 3) {
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), "FirmwareFailedSupportFragment")
					.commit();
				} else {
					int failCount = ((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount();
					ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$failCount " + failCount);
					((FirmwareUpdateActivity) getActivity()).setDownloadFailedCount(++failCount);
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareDownloadFailedFragment(), "FirmwareDownloadFailedFragment")
					.commit();
				}
			}
		}
	};
	
	
	@Override
	public void firmwareDataRecieved(String data) {
		ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$firmwareDataRecieved data " + data + " emptyDataResponseCount ");
		if(data == null || data.isEmpty() || data.length() <= 0) {
			getProps();
			return;
		}
		counter = 0;
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
		ALog.i(ALog.FIRMWARE, "jsonObject " + jsonObject);
		ALog.i(ALog.FIRMWARE, "jsonObject.get(upgrade) " + jsonObject.get("progress"));
		
		String progressString = getProgress(jsonObject);
		String stateString = getState(jsonObject);

		ALog.i(ALog.FIRMWARE, "upgradeString " + progressString);
		if(!(progressString.equals(""))) {
			progressPercent.setText(progressString + "%");
			progressBar.setProgress(Integer.parseInt(progressString));
			
			if(progressString.equals("100") && stateString.equals("ready")) {
				((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally("state", "go");
				showNextFragment();
			}
		}
		getProps();
	}
	
	public String getProgress(JsonObject jsonObject) {
		JsonElement progressElemt = jsonObject.get("progress");
		return progressElemt.getAsString();
	}
	
	public String getState(JsonObject jsonObject) {
		JsonElement stateElemt = jsonObject.get("state");
		return stateElemt.getAsString();
	}
	
	public void showNextFragment() {
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.firmware_container, new FirmwareInstallFragment(), "FirmwareInstallFragment")
		.commit();
	}
}
