package com.philips.cl.di.dev.pa.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import android.os.AsyncTask;
import android.util.Log;

import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.utils.NetworkUtils;

public class TaskPutDeviceDetails extends AsyncTask<String, Void, String> {
	private static final String TAG = TaskPutDeviceDetails.class.getSimpleName();

	private String dataToUpload ;
	private ServerResponseListener responseListener = null;

	private int responseCode ;

	public TaskPutDeviceDetails(String dataToUpload, ServerResponseListener responseListener) {
		this.dataToUpload = dataToUpload;
		this.responseListener = responseListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... url) {
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null ;
		HttpURLConnection conn = null ;
		try {
			Log.i(TAG, url[0]) ;
			URL urlConn = new URL(url[0]);
			conn = (HttpURLConnection) urlConn.openConnection();
			conn.setRequestProperty("content-type", "application/json") ;
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			out = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
			out.write(dataToUpload);
			out.flush() ;

			conn.connect();
			int responseCode = conn.getResponseCode() ;

			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();	
				result = NetworkUtils.readFully(inputStream);
				Log.i(TAG, result) ;
			}

		} catch (IOException e) {   
		}

		finally {
			if(inputStream != null ) {
				try {
					inputStream.close() ;
					inputStream = null ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			if( out != null ) {
				try {
					out.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}
				out = null ;
			}
			if ( conn != null ) {
				conn.disconnect() ;
				conn = null ;
			}
		}
		return result; 
	}

	@Override
	protected void onPostExecute(String result) {
		responseListener.receiveServerResponse(responseCode, result);
		Log.d(TAG, ""+new Date(System.currentTimeMillis())) ;
	}
}
