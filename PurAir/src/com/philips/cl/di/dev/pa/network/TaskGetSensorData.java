package com.philips.cl.di.dev.pa.network;

import android.os.AsyncTask;

import com.philips.cl.di.dev.pa.dto.ResponseDto;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.utils.NetworkUtils;
/**
 * This class will call the sensor data. It gets the sensor data from the server
 * @author 310124914
 *
 */
public class TaskGetSensorData extends AsyncTask<String, Void, String> {
	private ServerResponseListener listener ;

	private ResponseDto responseObj;
	private int responseCode;

	public TaskGetSensorData(ServerResponseListener handler ) {
		this.listener = handler;
	}

	@Override
	protected String doInBackground(String... urls) {
		String result = null ;
		// params comes from the execute() call: params[0] is the url.
		//Log.i(TAG, urls[0]) ;

		responseObj = NetworkUtils.downloadUrl(urls[0], 5000);

		if(responseObj!=null)
		{
			result=responseObj.getResponseData();
			responseCode=responseObj.getResponseCode();
		}
		
		if (result == null || result.length() == 0) {
			return null;
		}
		return result ;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String response) {
		//Log.i(TAG, "onPOstExecute") ;
		if (response != null) {
			//Log.e(TAG, response);
		}

		if (listener!=null) {
			listener.receiveServerResponse(responseCode,response);
		}
	}
}