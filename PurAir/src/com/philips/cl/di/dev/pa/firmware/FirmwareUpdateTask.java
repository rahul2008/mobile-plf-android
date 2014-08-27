package com.philips.cl.di.dev.pa.firmware;

import android.os.AsyncTask;

import com.philips.cl.di.dev.pa.datamodel.ResponseDto;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class FirmwareUpdateTask extends AsyncTask<String, Void, String> {

	private FirmwareResponseListener firmwareUpdatesListener;
	private ResponseDto responseObj;
	private PurAirDevice purifier;

	public interface FirmwareResponseListener {
		public void firmwareDataRecieved(String data);
	}

	public FirmwareUpdateTask(FirmwareResponseListener listener,
			PurAirDevice purifier) {
		firmwareUpdatesListener = listener;
		this.purifier = purifier;
	}

	// TODO : Remove, used for testing to see if request has been initiated.
	@Override
	protected void onPreExecute() {
		ALog.i(ALog.FIRMWARE, "FirmwareUpdateTask$onPreExecute");
	}

	@Override
	protected String doInBackground(String... firmwareUrl) {
		ALog.i(ALog.FIRMWARE, "doInBackground firmwareUrl " + firmwareUrl[0]);
		String result = "";
		responseObj = NetworkUtils.downloadUrl(firmwareUrl[0]);
		if (responseObj != null) {
			result = new DISecurity(null).decryptData(
					responseObj.getResponseData(), purifier);
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		firmwareUpdatesListener.firmwareDataRecieved(result);
	}
}
