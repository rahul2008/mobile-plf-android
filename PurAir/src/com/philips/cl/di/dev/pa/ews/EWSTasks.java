package com.philips.cl.di.dev.pa.ews;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class EWSTasks extends AsyncTask<String, Void, String>{

	private String postData ;
	private String requestType = "GET"  ; 
	private int responseCode ;
	private EWSTaskListener ewsTaskListener ;
	
	public EWSTasks(int taskType, EWSTaskListener ewsTaskListener) {
		this.ewsTaskListener = ewsTaskListener ;
	}
	
	public EWSTasks(int taskType, String postData, String requestType,EWSTaskListener ewsTaskListener) {
		this(taskType,ewsTaskListener) ;
		this.postData = postData ;
		this.requestType = requestType ;
	}
	
	
	@Override
	protected String doInBackground(String... url) {
		String response = downloadUrl(url[0]) ;
		return response;
	}
	
	@Override
	protected void onPostExecute(String response) {
		Log.i("ews", "onPOstExecute:" +response) ;
			if (ewsTaskListener != null) {
				ewsTaskListener.onTaskCompleted(responseCode,response);
			}		
	}
	
	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves the web page content as an InputStream,
	 *  which it returns as a string.  
	 *
	 * @param  stringUrl	The given URL 	
	 * @return	Returns 	web page as a string 
	 */
	private String downloadUrl(String stringUrl)  {
		Log.i("ews", stringUrl) ;
		InputStream inputStream = null;
		HttpURLConnection conn = null ;
		String data = null ;
		OutputStreamWriter os = null ;
		try {
			URL url ;
			url = new URL(stringUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestType);			
			conn.setRequestProperty("Content-Type", "application/json") ;
			
			// Starts the query
			
			if(! requestType.equals("GET")) {
				os = new OutputStreamWriter(
						conn.getOutputStream());
				os.write(postData);
				os.flush() ;
			}
			conn.connect();
			responseCode = conn.getResponseCode();
			if( responseCode == 200) {
				inputStream = conn.getInputStream();
				// Convert the InputStream into a string
				data = readFully(inputStream);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				try {					
					inputStream.close();
					inputStream = null ;
				} catch (IOException e) {
					
				}				
			} 
			if( conn != null ) {
				conn.disconnect() ;
				conn = null ;
			}
		}
		return data;
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
