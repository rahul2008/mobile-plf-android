package com.philips.cl.di.dev.pa.purifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.datamodel.ResponseDto;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class TaskGetDiagnosticData extends AsyncTask<String, Void, String[]> {
	
	private Context context;
	private ResponseDto responseObj;
	private ProgressDialog pDialog;
	private DiagnosticsDataListener listener;
	private PurAirDevice purifier;

	public interface DiagnosticsDataListener {
		void diagnosticsDataUpdated(String[] data);
	}

	public TaskGetDiagnosticData(Context pContext,
			DiagnosticsDataListener pListener, PurAirDevice purifier) {
		context = pContext;
		listener = pListener;
		this.purifier = purifier;
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
			responseObj = NetworkUtils.downloadUrl(urls[i], 10000);
			if (responseObj != null)
				result[i] = new DISecurity(null).decryptData(responseObj.getResponseData(), purifier);
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
		PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier();
		if(!NetworkUtils.isNetworkConnected(context))
		{
			Toast.makeText(
					context,
					"Could not fetch details, please check your data connection.",
					Toast.LENGTH_LONG).show();
		}
		else if (purifier == null || purifier.getConnectionState().equals(ConnectionState.DISCONNECTED)) {
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
