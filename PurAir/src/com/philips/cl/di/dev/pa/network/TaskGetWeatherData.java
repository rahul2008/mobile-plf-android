package com.philips.cl.di.dev.pa.network;


import java.io.IOException;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class TaskGetWeatherData extends Thread {

	private static final String TAG = TaskGetWeatherData.class.getSimpleName() ;

	private WeatherDataListener listener ;
	private String url ;

	public interface WeatherDataListener {
		public void weatherDataUpdated(String weatherData);
	}

	public TaskGetWeatherData(String url,WeatherDataListener listener) {
		this.listener = listener ;
		this.url = url ;
	}

	@Override
	public void run() {
		String result = "";
		try {
			result = downloadUrl(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result != null) {
			listener.weatherDataUpdated(result) ;
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
		Log.i(TAG, "Download: "+stringUrl) ;
		InputStream inputStream = null;
		try {
			URL url = new URL(stringUrl);

			//			Log.d(getClass().getSimpleName(), "Start download from URL : " + url);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");


			// Starts the query
			conn.connect();
			//			Log.d(getClass().getSimpleName(), "received response [" + response + "]");
			
			if( conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null ;
			}
			
			inputStream = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readFully(inputStream);
			return contentAsString;
		} finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				inputStream.close();
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
