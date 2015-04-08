package com.philips.cl.di.dev.pa.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.annotation.SuppressLint;
import android.os.Build;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class DemoModeTask extends Thread {

	private String urlString;
	private ServerResponseListener listener;
	private int responseCode;
	private String result = "";
	private boolean stop;
	private String requestType = "GET";
	private String putData;

	public DemoModeTask(ServerResponseListener listener, String url,
			String putData, String requestType) {
		ALog.i(ALog.DEMO_MODE, "Url: " + url);
		this.urlString = url;
		this.listener = listener;
		this.requestType = requestType;
		this.putData = putData;
	}

	@SuppressLint("NewApi")
	@Override
	public void run() {
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		OutputStreamWriter os = null;
		try {
			URL url = new URL(urlString);
			conn = NetworkUtils.getConnection(url, requestType, 10000,3000) ;
			if( conn == null ) return ;
			if (requestType.equals("PUT")) {
				if (putData == null)
					return;
				if (Build.VERSION.SDK_INT <= 10) {
					conn.setDoOutput(true);
				}
				os = new OutputStreamWriter(conn.getOutputStream(),
						Charset.defaultCharset());
				os.write(putData);
				os.flush();
			}
			conn.connect();
			responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = conn.getInputStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
			}

		} catch (IOException e) {
			ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
		} finally {

			if (listener != null && !stop) {
				listener.receiveServerResponse(responseCode, result, null);
			}

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
				}
				inputStream = null;
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					ALog.e(ALog.DEMO_MODE, "Error: " + e.getMessage());
				}
				os = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}

	public void stopTask() {
		stop = true;
	}
}
