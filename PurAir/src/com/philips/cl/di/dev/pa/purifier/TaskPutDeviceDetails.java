package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.util.Log;

import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class TaskPutDeviceDetails implements Runnable {
	private static final String TAG = TaskPutDeviceDetails.class.getSimpleName();

	private String dataToUpload ;
	private ServerResponseListener responseListener = null;

	private int responseCode ;
	private String url;

	public TaskPutDeviceDetails(String dataToUpload,String url, ServerResponseListener responseListener) {
		this.dataToUpload = dataToUpload;
		this.responseListener = responseListener;
		this.url = url ;
	}

	

	@Override
	public void run() {
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null ;
		HttpURLConnection conn = null ;
		try {
			Log.i(TAG, url) ;
			URL urlConn = new URL(url);
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
			e.printStackTrace();
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
			if(responseListener != null )
				responseListener.receiveServerResponse(responseCode, result);
		}
	}
}
