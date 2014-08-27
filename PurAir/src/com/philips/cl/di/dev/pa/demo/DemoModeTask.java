package com.philips.cl.di.dev.pa.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.os.Build;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class DemoModeTask extends Thread {

	private String url;
	private ServerResponseListener listener;
	private int responseCode;
	private String result = "";
	private boolean stop;
	private String requestType = "GET";
	private String putData;

	public DemoModeTask(ServerResponseListener listener, String url,
			String putData, String requestType) {
		ALog.i(ALog.DEMO_MODE, "Url: " + url);
		this.url = url;
		this.listener = listener;
		this.requestType = requestType;
		this.putData = putData;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		OutputStreamWriter os = null;
		try {
			URL urlConn = new URL(url);
			conn = (HttpURLConnection) urlConn.openConnection();
			conn.setRequestMethod(requestType);
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
				result = NetworkUtils.readFully(inputStream);
			}

		} catch (IOException e) {
			ALog.e(ALog.DEMO_MODE, e.getMessage());
		} finally {

			if (listener != null && !stop) {
				listener.receiveServerResponse(responseCode, result, null);
			}

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					ALog.e(ALog.DEMO_MODE, e.getMessage());
				}
				inputStream = null;
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					ALog.e(ALog.DEMO_MODE, e.getMessage());
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
