package com.philips.cl.di.dev.pa.firmware;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class FirmwarePutPropsTask implements Runnable {

	private String dataToUpload ;
	private ServerResponseListener responseListener = null;

	private int responseCode ;
	private String url;

	public FirmwarePutPropsTask(String dataToUpload,String url, ServerResponseListener responseListener) {
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
			ALog.i(ALog.FIRMWARE, "FirmwarePutPropsTask$run url " + url) ;
			URL urlConn = new URL(url);
			conn = (HttpURLConnection) urlConn.openConnection();
			conn.setRequestProperty("content-type", "application/json") ;
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setRequestMethod("PUT");
			out = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
			out.write(dataToUpload);
			out.flush() ;
			ALog.i(ALog.FIRMWARE, "FirmwarePutPropsTask$run conn " + conn + " dataToUpload " + dataToUpload) ;
			conn.connect();
			int responseCode = conn.getResponseCode() ;

			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();	
				result = NetworkUtils.readFully(inputStream);
				ALog.i(ALog.FIRMWARE, "PutProps result " + result) ;
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
