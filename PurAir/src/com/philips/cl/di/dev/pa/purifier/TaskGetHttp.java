package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class TaskGetHttp extends Thread {

	private String url ;
	private ServerResponseListener listener ;
	private String areaID ;
	private int responseCode;
	private String result = "" ;

	public TaskGetHttp(String url,Context context, ServerResponseListener listener) {
		ALog.i(ALog.TASK_GET, "Url: " + url);
		this.url = url ;
		this.listener = listener ;
	}
	
	public TaskGetHttp(String url,String areaID, Context context, ServerResponseListener listener) {
		this(url,context,listener) ;
		this.areaID = areaID ;
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
			responseCode = conn.getResponseCode() ;
			result = "" ;
			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();					
				result = NetworkUtils.convertInputStreamToString(inputStream) ;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if ( listener != null ) {
				listener.receiveServerResponse(responseCode, result, areaID) ;
			}
			NetworkUtils.closeAllConnections(inputStream, null, conn);
		}
	}

}
