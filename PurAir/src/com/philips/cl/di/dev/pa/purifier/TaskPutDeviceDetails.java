package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.util.Log;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class TaskPutDeviceDetails implements Runnable {
	private static final String TAG = TaskPutDeviceDetails.class.getSimpleName();

	private String dataToUpload ;
	private ServerResponseListener responseListener = null;

	private int responseCode ;
	private String url;
	
	private String requestMethod = AppConstants.REQUEST_METHOD_PUT ;

	public TaskPutDeviceDetails(String dataToUpload,String url, ServerResponseListener responseListener) {
		this.dataToUpload = dataToUpload;
		this.responseListener = responseListener;
		this.url = url ;
	}

	public TaskPutDeviceDetails(String dataToUpload,String url, ServerResponseListener responseListener, String requestMethod) {
		this.dataToUpload = dataToUpload;
		this.responseListener = responseListener;
		this.url = url ;
		this.requestMethod = requestMethod ;
	}

	@Override
	public void run() {
		String result = "";
		String targetIpAddress = null;
		InputStream inputStream = null;
		OutputStreamWriter out = null ;
		HttpURLConnection conn = null ;
		try {
			ALog.i(ALog.SUBSCRIPTION, "TastPutDeviceDetails$run requestMethod " + requestMethod + " url " + url) ;
			URL urlConn = new URL(url) ;
			conn = NetworkUtils.getConnection(urlConn, requestMethod, -1) ;
			if(dataToUpload != null && !dataToUpload.isEmpty()) {
				conn.setDoOutput(true) ;
				out = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
				out.write(dataToUpload);
				out.flush() ;				
			}			
			targetIpAddress = urlConn.getHost();
			conn.connect();
			responseCode = conn.getResponseCode() ;

			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();
				result = NetworkUtils.readFully(inputStream);
				Log.i(TAG, result) ;				
			}
			else {
				inputStream = conn.getErrorStream();
				result = NetworkUtils.readFully(inputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			ALog.i(ALog.SCHEDULER, "Finally: "+result) ;
			NetworkUtils.closeAllConnections(inputStream, out, conn) ;
			if(responseListener != null )
				responseListener.receiveServerResponse(responseCode, result, targetIpAddress);
		}
	}
}
