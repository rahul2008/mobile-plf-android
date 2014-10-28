package com.philips.cl.di.dev.pa.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;


/*
 * This class will be designed to check the network availability
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @date : 22 Oct 2014
 */
public class URLExistAsyncTask extends AsyncTask<String, Void, Boolean> {
	private AsyncTaskCompleteListenere callback = null;;
	private static URLExistAsyncTask mTask = null;
	private static final String URL = "http://www.example.com";

	private URLExistAsyncTask(/* AsyncTaskCompleteListenere callback */) {

	}

	public static URLExistAsyncTask getInstance() {
		mTask = new URLExistAsyncTask();
		return mTask;
	}

	public void testConnection(AsyncTaskCompleteListenere callback) {
		this.callback = callback;
		Log.i("testing","testConnection URLExistAsyncTask");
		try {
			mTask.execute(new String[] { URL });
		}
		catch(IllegalStateException e){
			mTask.cancel(true);
			ALog.e("testing", "URLExistAsyncTask testConnection IllegalStateException");
			mTask = new URLExistAsyncTask();
			mTask.execute(new String[] { URL });
			Log.i("testing","testConnection URLExistAsyncTask IllegalExp instanciate again");
		}
	}

	protected Boolean doInBackground(String... params) {
		int code = 0;
		try {
			URL u = new URL(params[0]);
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("GET");
			huc.setReadTimeout(5000);
			huc.connect();
			code = huc.getResponseCode();
			ALog.i("testing",
					"testConnection Async Task doInBackground code : " + code);

		} catch (IOException e) {
			code = 0;
		} catch (Exception e) {
			code = 0;
		}
		ALog.i("testing", "testConnection Async Task doInBackground result : "
				+ (code == 200));
		return code == 200;
	}

	protected void onPostExecute(Boolean result) {
		ALog.i("testing", "URLExistAsyncTask AsyncTask onPostExecute result : "
				+ result);
		if(!result){
			Log.i("testing","No Internet");
		}
		else{
			Log.i("testing","Internet is working");
		}
		if (callback != null) {
			callback.onTaskComplete(result);
		}
	}
}
