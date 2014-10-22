package com.philips.cl.di.dev.pa.dashboard;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AsyncTaskCompleteListenere;

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
		//if (mTask == null) {
	//	}
		return mTask;
	}

	public void setCallback(AsyncTaskCompleteListenere callback) {
		this.callback = callback;
	}

	public void testConnection() {
		Log.i("testing","testConnection URLExistAsyncTask");
		try{
			mTask = new URLExistAsyncTask();
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
//		if (callback != null) {
//			callback.onTaskComplete(result);
//		}
	}
}
