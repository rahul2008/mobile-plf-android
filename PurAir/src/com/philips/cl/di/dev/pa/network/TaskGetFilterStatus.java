package com.philips.cl.di.dev.pa.network;


import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.philips.cl.di.dev.pa.dto.FilterStatusDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.Utils;

import android.os.AsyncTask;
import android.util.Log;

public class TaskGetFilterStatus extends AsyncTask<String, Void, String> {
	
	private static final String TAG = TaskGetFilterStatus.class.getSimpleName() ;

	public interface FilterStatusInterface {
		public void filterStatusUpdated(FilterStatusDto filterStatusDto);
	}

	public TaskGetFilterStatus() {
	}

	@Override
	protected String doInBackground(String... urls) {
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
			Log.i(TAG, response) ;
			if ( response != null && Utils.isJson(response)) {
				FilterStatusDto filterStatusDto = new DataParser(response).parseFilterStatusData() ;
				SessionDto.getInstance().setFilterStatusDto(filterStatusDto) ;
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
