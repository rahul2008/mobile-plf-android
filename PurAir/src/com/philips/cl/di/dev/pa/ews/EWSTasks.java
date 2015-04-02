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
import java.nio.charset.Charset;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

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
		if(isCancelled()) {
			cancel(true) ;
			return null ;
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String response) {
		ALog.i(ALog.EWS, "onPOstExecute:" +response + " responsecode: " + responseCode) ;
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
	@SuppressLint("NewApi")
	private String downloadUrl(String stringUrl)  {
		ALog.i(ALog.EWS, "stringUrl: "+stringUrl) ;
		InputStream inputStream = null;
		HttpURLConnection conn = null ;
		String data = null ;
		OutputStreamWriter os = null ;
		try {
			URL url = new URL(stringUrl);
			conn = (HttpURLConnection) NetworkUtils.getConnection(url,requestType, 20000,3000);
			if( conn == null ) return "";
			conn.setRequestProperty("connection", "close");
			conn.setConnectTimeout(20000);
			if(! requestType.equals("GET")) {
				if (Build.VERSION.SDK_INT <= 10) {
					conn.setDoOutput(true);
				}
				os = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
				os.write(postData);
				os.flush() ;
			}
			conn.connect();
			try {
				responseCode = conn.getResponseCode();
			} catch (Exception e) {
				responseCode = HttpURLConnection.HTTP_BAD_GATEWAY;
				ALog.e(ALog.EWS, "EWSTask: " + "Error: " + e.getMessage());
			}
			ALog.i(ALog.EWS, "Response code: " + responseCode);
			if( responseCode == 200 ) {
				inputStream = conn.getInputStream();
				// Convert the InputStream into a string
				data = readFully(inputStream);
			}
			else if( responseCode == 400 ) {
				inputStream = conn.getErrorStream();
				// Convert the InputStream into a string
				data = readFully(inputStream);
			}
		} catch (MalformedURLException e) {
			ALog.e(ALog.EWS, "EWSTask: " + "Error: " + e.getMessage());
		} catch (ProtocolException e) {
			ALog.e(ALog.EWS, "EWSTask: " + "Error: " + e.getMessage());
		} catch (IOException e) {
			ALog.e(ALog.EWS, "EWSTask: " + "Error: " + e.getMessage());
		}
		finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				try {					
					inputStream.close();
					inputStream = null ;
				} catch (IOException e) {
					e.printStackTrace();
				}				
			} 
			
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
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
		if( inputStream != null ) {
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
	    return null ;
	}
}
