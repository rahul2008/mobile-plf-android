package com.philips.cl.di.dev.pa.firmware;

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

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class FirmwareDownloadFragment extends BaseFragment implements FirmwareUpdatesListener{

	private ProgressBar progressBar;
	private FontTextView progressPercent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.downloading_firmware, null);
		progressBar = (ProgressBar) view.findViewById(R.id.downloading_progressbar);
		getProps();
		progressPercent = (FontTextView) view.findViewById(R.id.progressbar_increasestatus);
		return view;
	}

	private void getProps() {
		ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$getProps");
		String firmwareUrl = String.format(AppConstants.URL_FIRMWARE_PORT, Utils.getIPAddress(getActivity()));
		FirmwareUpdateTask task = new FirmwareUpdateTask(FirmwareDownloadFragment.this);
		task.execute(firmwareUrl);
	}

	private int emptyDataResponseCount = 0;
	
	@Override
	public void firmwareDataRecieved(String data) {
		ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$firmwareDataRecieved data " + data + " emptyDataResponseCount " + emptyDataResponseCount);
		if(data == null || data.isEmpty() || data.length() <= 0) {
			emptyDataResponseCount++;
			getProps();
			return;
		}
		emptyDataResponseCount = 0;
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
		ALog.i(ALog.FIRMWARE, "jsonObject " + jsonObject);
		ALog.i(ALog.FIRMWARE, "jsonObject.get(upgrade) " + jsonObject.get("progress"));
		JsonElement progressElemt = jsonObject.get("progress");
		JsonElement stateElemt = jsonObject.get("state");
		String progressString = progressElemt.getAsString();
		String stateString = stateElemt.getAsString();
		ALog.i(ALog.FIRMWARE, "upgradeString " + progressString);
		if(!(progressString.equals(""))) {
			progressPercent.setText(progressString + "%");
			progressBar.setProgress(Integer.parseInt(progressString));
			
			if(progressString.equals("100") && stateString.equals("ready")) {
				((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally("state", "go");
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.firmware_container, new FirmwareInstallFragment(), "FirmwareInstallFragment")
				.commit();
			}
		}
		getProps();
	}
}
