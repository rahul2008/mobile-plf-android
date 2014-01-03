package com.philips.cl.di.dev.pa.network;

import java.io.IOException;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;

import android.os.AsyncTask;
import android.util.Log;
/**
 * This class will call the sensor data. It gets the sensor data from the server
 * @author 310124914
 *
 */
public class TaskGetSensorData extends AsyncTask<String, Void, String> {
	private static final String TAG = "TaskGetSensorData";
	private ServerResponseListener listener ;
	
	private int responseCode ;

	public TaskGetSensorData(ServerResponseListener handler ) {
		this.listener = handler;
	}

	@Override
	protected String doInBackground(String... urls) {
		// params comes from the execute() call: params[0] is the url.
		Log.i(TAG, urls[0]) ;
		try {
			String result = downloadUrl(urls[0]);
			if (result == null || result.length() == 0) {
				return null;
			}
			return result ;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String response) {
		if (response != null) {
			//Log.e(TAG, response);
		}
		
		if (listener!=null) {
			listener.receiveServerResponse(responseCode,response);
		}
	}

	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves the web page content as an InputStream,
	 *  which it returns as a string.  
	 *
	 * @param  stringUrl	The given URL 	
	 * @return	Returns 	web page as a string 
	 */
	private String downloadUrl(String stringUrl) throws IOException {
		InputStream inputStream = null;
		HttpURLConnection conn = null ;
		try {
			URL url = new URL(stringUrl);
			
//			Log.d(getClass().getSimpleName(), "Start download from URL : " + url);

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			

			// Starts the query
			conn.connect();
			responseCode = conn.getResponseCode();
//			Log.d(getClass().getSimpleName(), "received response [" + response + "]");
			inputStream = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readFully(inputStream);
			return contentAsString;
		} finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				inputStream.close();
				inputStream = null ;
			} 
			if( conn != null ) {
				conn.disconnect() ;
				conn = null ;
			}
		}
	}

	/**
	 * Reads an InputStream and converts it to a String.  
	 *
	 * @param  inputStream	Input stream to convert to string 	
	 * @return	Returns 	converted string
	 */
	// 
	public String readFully(InputStream inputStream) throws IOException, UnsupportedEncodingException {
		Reader reader = new InputStreamReader(inputStream, "UTF-8");

		int len = 1024;
		char[] buffer = new char[len];
		StringBuilder sb = new StringBuilder(len);
		int count;

		while ((count = reader.read(buffer)) > 0) {
			sb.append(buffer, 0, count);
		}

		return sb.toString();
	}
}