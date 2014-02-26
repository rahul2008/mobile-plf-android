package com.philips.cl.di.dev.pa.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.ResponseDto;
import com.philips.cl.di.dev.pa.utils.NetworkUtils;
import com.philips.cl.disecurity.DISecurity;

public class TaskGetDiagnosticData extends AsyncTask<String, Void, String[]> {
	Context context;
	ResponseDto responseObj;

	public TaskGetDiagnosticData(Context pContext) {
		context = pContext;
	}

	ProgressDialog pDialog;

	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Collecting diagnostics information...");
		pDialog.show();
	}

	@Override
	protected String[] doInBackground(String... urls) {
		String[] result = new String[urls.length];

		for (int i = 0; i < urls.length; i++) {
			responseObj = NetworkUtils.downloadUrl(urls[i]);
			if(responseObj!=null)
				result[i] = new DISecurity(null).decryptData(responseObj.getResponseData(), AppConstants.DEVICEID);
			// Escape early if cancel() is called
			if (isCancelled())
				break;
		}

		return result;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String[] response) {
		pDialog.dismiss();
		context = null;
	}

}
