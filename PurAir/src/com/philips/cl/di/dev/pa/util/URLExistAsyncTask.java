package com.philips.cl.di.dev.pa.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import com.philips.cl.di.dev.pa.constant.AppConstants;

/*
 * This class will be designed to check the network availability
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @date : 22 Oct 2014
 */
public class URLExistAsyncTask extends AsyncTask<String, Void, Boolean> {
	private AsyncTaskCompleteListenere callback ;
	private static URLExistAsyncTask mTask;
	private static final String URL = "http://www.example.com";
	private static final int CONNECTION_TIMEOUT = 5000 ;

	private URLExistAsyncTask(/* AsyncTaskCompleteListenere callback */) {

	}

	public static URLExistAsyncTask getInstance() {
		mTask = new URLExistAsyncTask();
		return mTask;
	}

	public void testConnection(AsyncTaskCompleteListenere callback) {
		this.callback = callback;
		try {
			mTask.execute(new String[] { URL });
		}
		catch(IllegalStateException e){
			mTask.cancel(true);
			mTask = new URLExistAsyncTask();
			mTask.execute(new String[] { URL });
		}
	}

	protected Boolean doInBackground(String... params) {
		boolean isInternetAvailable  = false;
		String response = AppConstants.EMPTY_STRING ;
		int code = 0;
		try {
			URL url = new URL(params[0]);
			HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
			hConn.setRequestMethod("GET");
			hConn.setReadTimeout(CONNECTION_TIMEOUT);
			hConn.connect();
			code = hConn.getResponseCode();
			
			response = NetworkUtils.convertInputStreamToString(hConn.getInputStream()) ;

		} catch (IOException e) {
			
		} catch (Exception e) {
		}
		if( code == HttpURLConnection.HTTP_OK && response.contains("Example Domain")) {
			isInternetAvailable = true;
		}
		return isInternetAvailable;
	}

	protected void onPostExecute(Boolean result) {
		if (callback != null) {
			callback.onTaskComplete(result);
		}
	}
}
