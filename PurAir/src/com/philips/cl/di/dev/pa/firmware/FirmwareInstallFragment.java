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
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateTask.FirmwareUpdatesListener;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;

public class FirmwareInstallFragment extends BaseFragment implements FirmwareUpdatesListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.installing_firmware, null);
		getProps();
		return view;
	}

	private void getProps() {
		String firmwareUrl = String.format(AppConstants.URL_FIRMWARE_PORT, Utils.getIPAddress(getActivity()));
		FirmwareUpdateTask task = new FirmwareUpdateTask(FirmwareInstallFragment.this);
		task.execute(firmwareUrl);
	}

	@Override
	public void firmwareDataRecieved(String data) {

		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$firmwareDataRecieved data " + data);
		if(data == null || data.isEmpty() || data.length() <= 0) {
			getProps();
			return;
		}
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$jsonObject " + jsonObject);
		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$jsonObject.get(upgrade) " + jsonObject.get("upgrade"));
		JsonElement progressElemt = jsonObject.get("upgrade");
		JsonElement stateElemt = jsonObject.get("state");
		String upgradeString = progressElemt.getAsString();
		String stateString = stateElemt.getAsString();
		ALog.i(ALog.FIRMWARE, "FirmwareInstallFragment$upgradeString " + upgradeString);
		if((stateString.equals("idle")) && upgradeString.equals("")) {
			getFragmentManager()
			.beginTransaction()
			.replace(R.id.firmware_container, new FirmwareInstallSuccessFragment(), "FirmwareInstallSuccessFragment")
			.commit();
		}
		getProps();
	}
}
