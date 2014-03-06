package com.philips.cl.di.dev.pa.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.utils.NetworkUtils;

public class TaskGetHttp extends Thread {

	private String url ;
	private ServerResponseListener listener ;

	public TaskGetHttp(String url,Context context, ServerResponseListener listener) {
		this.url = url ;
		this.listener = listener ;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		HttpURLConnection conn = null ;
		try {
			URL urlConn = new URL(url);
			conn = (HttpURLConnection) urlConn.openConnection() ;
			conn.setRequestMethod("GET");
			// Starts the query
			conn.connect();
			int responseCode = conn.getResponseCode() ;
			String result = "" ;
			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();					
				result = NetworkUtils.readFully(inputStream) ;
			}				


			if ( listener != null ) {
				listener.receiveServerResponse(responseCode, result) ;
			}
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inputStream = null ;
			} 
			if( conn != null ) {
				conn.disconnect() ;
				conn = null ;
			}
		}
	}

}
