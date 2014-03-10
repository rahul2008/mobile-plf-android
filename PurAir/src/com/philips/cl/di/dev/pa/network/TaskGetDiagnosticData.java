package com.philips.cl.di.dev.pa.network;

import java.net.HttpURLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.ResponseDto;
import com.philips.cl.di.dev.pa.utils.NetworkUtils;
import com.philips.cl.disecurity.DISecurity;

public class TaskGetDiagnosticData extends AsyncTask<String, Void, String[]>{
	private Context context;
	private ResponseDto responseObj;
	private ProgressDialog pDialog;
	private DiagnosticsDataListener listener ;

	public interface DiagnosticsDataListener {
		public void diagnosticsDataUpdated(String[] data);
	}

	public TaskGetDiagnosticData(Context pContext, DiagnosticsDataListener pListener) {
		context = pContext;
		listener = pListener ;
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
		if(responseObj==null || responseObj.getResponseCode()!=HttpURLConnection.HTTP_OK && response==null)
		{
			Toast.makeText(context,"Could not fetch all details, please check your data connection or sign on, to fetch complete information.", Toast.LENGTH_LONG).show();			
		}			
		listener.diagnosticsDataUpdated(response);
		context = null;
	}

}
