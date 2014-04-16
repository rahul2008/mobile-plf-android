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
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareResponseListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareDownloadFragment extends BaseFragment implements FirmwareResponseListener{

	private ProgressBar progressBar;
	private FontTextView progressPercent;
	private Thread timerThread;
	private int downloadProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.downloading_firmware, null);
		progressBar = (ProgressBar) view.findViewById(R.id.downloading_progressbar);
		FontTextView downloadFirmwareTv = (FontTextView) view.findViewById(R.id.downloading_firmware_for_purifier_msg);
		downloadFirmwareTv.setText(getString(R.string.downloading_firmware_for_purifier_msg, ((FirmwareUpdateActivity) getActivity()).getPurifierName())) ;

		getProps();
		progressPercent = (FontTextView) view.findViewById(R.id.progressbar_increasestatus);
		progressPercent.setText("0%");
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_DOWNLOAD);
		FirmwareUpdateActivity.setCancelled(false);
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
		ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$getProps");
		String firmwareUrl = Utils.getPortUrl(Port.FIRMWARE, Utils.getIPAddress());
		FirmwareUpdateTask task = new FirmwareUpdateTask(FirmwareDownloadFragment.this);
		task.execute(firmwareUrl);
	}

	private static int counter = 0;

	public static void setCounter(int counter) {
		FirmwareDownloadFragment.counter = counter;
	}

	Runnable timerRunnable = new Runnable() {
		public void run() {
			while(counter < 60 && !FirmwareUpdateActivity.isCancelled()) {
				ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$counter " + counter + " FirmwareUpdateActivity.isCancelled() " +FirmwareUpdateActivity.isCancelled());
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
				if(((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount() >= 3) {
					FirmwareUpdateActivity.setCancelled(true);
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), FirmwareConstants.FIRMWARE_FAILED_SUPPORT_FRAGMENT)
					.commit();
				} else {
					FirmwareUpdateActivity.setCancelled(true);
					int failCount = ((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount();
					ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$failCount " + failCount);
					((FirmwareUpdateActivity) getActivity()).setDownloadFailedCount(++failCount);
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareDownloadFailedFragment(), FirmwareConstants.FIRMWARE_DOWNLOAD_FAILED_FRAGMENT)
					.commit();
				}
			}
		}
	};

	@Override
	public void firmwareDataRecieved(String data) {
		ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$firmwareDataRecieved data " + data);
		if(FirmwareUpdateActivity.isCancelled()) {
			return;
		}
		if(data == null || data.isEmpty() || data.length() <= 0) {
			getProps();
			return;
		}

		JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
		ALog.i(ALog.FIRMWARE, "jsonObject " + jsonObject);
		ALog.i(ALog.FIRMWARE, "jsonObject.get(progress) " + jsonObject.get("progress"));
		processFirmwareData(jsonObject);
		getProps();
	}

	public void processFirmwareData(JsonObject jsonObject) {
		String progressString = getJsonPropertyAsString(jsonObject, FirmwareConstants.PROGRESS); //getProgress(jsonObject);
		String stateString = getJsonPropertyAsString(jsonObject, FirmwareConstants.STATE);

		ALog.i(ALog.FIRMWARE, "FDF$processFirmwareData progress " + progressString + " downloadProgress " + downloadProgress);

		if(!(progressString.equals(""))) {
			int progress = Integer.parseInt(progressString);
			//			if(progress <= downloadProgress) {
			////				getProps();
			//				return;
			//			}
			counter = 0;
			progressPercent.setText(progressString + "%");
			progressBar.setProgress(progress);
			downloadProgress = progress;
			if(progressString.equals("100") && stateString.equals("ready")) {
				FirmwareUpdateActivity.setCancelled(true);
				((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally(FirmwareConstants.STATE, FirmwareConstants.GO);
				showNextFragment();
			}

			if(stateString.equals("idle")) {
				return;
			}
		}
	}
	
	public String getJsonPropertyAsString(JsonObject jsonObject, String property) {
		JsonElement progressElemt = jsonObject.get(property);
		return progressElemt.getAsString();
	}
	
	//TODO : Remove methods getProgress getState
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
		.replace(R.id.firmware_container, new FirmwareInstallFragment(), FirmwareConstants.FIRMWARE_INSTALL_FRAGMENT)
		.commit();
	}
}
