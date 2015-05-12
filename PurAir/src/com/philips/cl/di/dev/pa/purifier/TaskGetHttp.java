package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class TaskGetHttp extends Thread {

	private String url ;
	private ServerResponseListener listener ;
	private String type ;
	private String areaId;
	private int responseCode;
	private String result = "" ;

	public TaskGetHttp(String url, Context context, ServerResponseListener listener) {
		ALog.i(ALog.TASK_GET, "Url: " + url);
		this.url = url ;
		this.listener = listener ;
	}

	public TaskGetHttp(String url, String type, Context context, ServerResponseListener listener) {
		this(url,context,listener) ;
		this.type = type ;
	}

	public TaskGetHttp(String url, String type, String areaId, Context context, ServerResponseListener listener) {
		this(url,context,listener) ;
		this.type = type ;
		this.areaId = areaId;
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
				result = convertInputStreamToString(inputStream) ;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if ( listener != null ) {
				if(areaId == null || areaId.isEmpty()) {
					listener.receiveServerResponse(responseCode, result, type);
				} else {
					listener.receiveServerResponse(responseCode, result, type, areaId) ;
				}
			}
			closeAllConnections(inputStream, null, conn);
		}
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 *
	 * @param  inputStream	Input stream to convert to string
	 * @return	Returns 	converted string
	 */
	private static String convertInputStreamToString(InputStream inputStream) throws IOException, UnsupportedEncodingException {
		if (inputStream == null) return "";
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

	private static final void closeAllConnections(InputStream is, OutputStreamWriter out, HttpURLConnection conn) {
		if(is != null ) {
			try {
				is.close() ;
				is = null ;
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
	}

}
