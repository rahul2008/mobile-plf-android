package com.philips.cl.di.dev.pa.firmware;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.ResponseDto;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

import android.os.AsyncTask;

public class FirmwareUpdateTask extends AsyncTask<String, Void, String> {
	
	private FirmwareUpdatesListener firmwareUpdatesListener;
	private ResponseDto responseObj;
	
	public interface FirmwareUpdatesListener {
		public void firmwareDataRecieved(String data);
	}
	
	public FirmwareUpdateTask(FirmwareUpdatesListener listener) {
		firmwareUpdatesListener = listener;
	}

	//TODO : Remove before release.
	@Override
	protected void onPreExecute() {
		ALog.i(ALog.FIRMWARE, "FirmwareUpdateTask$onPreExecute");
	}
	@Override
	protected String doInBackground(String... firmwareUrl) {
		String result = "";
		responseObj = NetworkUtils.downloadUrl(firmwareUrl[0]);
		if (responseObj != null) {
			result = new DISecurity(null).decryptData(responseObj.getResponseData(), AppConstants.deviceId);
		}
		
		ALog.i(ALog.FIRMWARE, "doInBackground " + result);
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		ALog.i(ALog.FIRMWARE, "onPostExecute result " + result);
		firmwareUpdatesListener.firmwareDataRecieved(result);
	}
}
