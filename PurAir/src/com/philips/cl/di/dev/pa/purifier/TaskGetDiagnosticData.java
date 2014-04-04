package com.philips.cl.di.dev.pa.purifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.ResponseDto;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class TaskGetDiagnosticData extends AsyncTask<String, Void, String[]> {
	private Context context;
	private ResponseDto responseObj;
	private ProgressDialog pDialog;
	private DiagnosticsDataListener listener;

	public interface DiagnosticsDataListener {
		public void diagnosticsDataUpdated(String[] data);
	}

	public TaskGetDiagnosticData(Context pContext,
			DiagnosticsDataListener pListener, MainActivity pActivity) {
		context = pContext;
		listener = pListener;
	}

	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Collecting diagnostics information...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected String[] doInBackground(String... urls) {
		String[] result = new String[urls.length];

		for (int i = 0; i < urls.length; i++) {
			responseObj = NetworkUtils.downloadUrl(urls[i]);
			if (responseObj != null)
				result[i] = new DISecurity(null).decryptData(
						responseObj.getResponseData(), AppConstants.deviceId);
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
		if(!NetworkUtils.isNetworkConnected(context))
		{
			Toast.makeText(
					context,
					"Could not fetch details, please check your data connection.",
					Toast.LENGTH_LONG).show();
		}
		else if (MainActivity.airPurifierEventDto==null || MainActivity.airPurifierEventDto.getConnectionStatus()== AppConstants.NOT_CONNECTED) {
			Toast.makeText(
					context,
					"Could not fetch Airpurifier details, please sign on, to fetch complete information.",
					Toast.LENGTH_LONG).show();
			listener.diagnosticsDataUpdated(response);
		}		
		else
		{
			listener.diagnosticsDataUpdated(response);
		}
		context = null;
	}

}
