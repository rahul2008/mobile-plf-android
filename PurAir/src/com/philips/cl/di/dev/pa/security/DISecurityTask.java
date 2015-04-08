package com.philips.cl.di.dev.pa.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.philips.cl.di.dev.pa.ews.WifiNetworkCallback;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class DISecurityTask extends AsyncTask<String, Void, String> {

	private ServerResponseListener listener;
	private int responseCode;
	private String deviceId;
	private String urlString;
	private String randomValue;

	public DISecurityTask(ServerResponseListener listener) {
		this.listener = listener;
	}

	@SuppressLint("NewApi")
	@Override
	protected String doInBackground(String... params) {
		urlString = params[0]; 
		deviceId = params[1];
		String data = params[2];
		randomValue = params[3];
		ALog.i(ALog.SECURITY, "Started DISecurity task with url: " + urlString + "   and data: " + data);
		OutputStreamWriter out = null;
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		String result = "";
		try {
			URL url = new URL(urlString);
			conn = NetworkUtils.getConnection(url, "PUT", 20000, 5000) ;
			if( conn == null ) return "";
			if (Build.VERSION.SDK_INT <= 10) {
				conn.setDoOutput(true);
			}
			out = new OutputStreamWriter(conn.getOutputStream(),
					Charset.defaultCharset());
			out.write(data);
			out.flush();
			conn.connect();

			responseCode = conn.getResponseCode();
			ALog.i(ALog.SECURITY, "DISecurity task returned with responseCode: " +responseCode);
			if (responseCode == 200) {
				inputStream = conn.getInputStream();
				result = readFully(inputStream);
			}
			// TODO detect decryption failed response

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				try {
					inputStream.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inputStream = null;
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if (listener != null) {
			listener.receiveServerResponse(responseCode, result, deviceId, urlString, randomValue);
		}
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 * 
	 * @param inputStream
	 *            Input stream to convert to string
	 * @return Returns converted string
	 */
	//
	public String readFully(InputStream inputStream) throws IOException,
	UnsupportedEncodingException {
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

	@SuppressLint("NewApi")
	private Network getWifiNetworkForSocket(Context context) {
		ConnectivityManager connectionManager = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkRequest.Builder request = new NetworkRequest.Builder();
		request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

		final Object lock = new Object();
		WifiNetworkCallback networkCallback = new WifiNetworkCallback(lock);

		synchronized (lock) {
			connectionManager.registerNetworkCallback(request.build(),	networkCallback);
			try {
				lock.wait(3000);
				Log.e(ALog.WIFI, "Timeout error occurred");
			} catch (InterruptedException e) {
			}
		}
		connectionManager.unregisterNetworkCallback(networkCallback);
		return networkCallback.getNetwork();
	}

}
