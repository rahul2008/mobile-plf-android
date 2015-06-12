package com.philips.cl.di.digitalcare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Looper;
import android.util.Log;

import com.philips.cl.di.digitalcare.util.DigiCareLogger;

public class RequestData {

	private final String TAG = RequestData.class.getSimpleName();

	private ResponseCallback mResponseCallback = null;
	private String mResponse = null;
	private String mUrl = null;

	public RequestData(String url, ResponseCallback responseCallback) {
		mUrl = url;
		mResponseCallback = responseCallback;
	}

	public void getReponse() {
		NetworkThread mNetworkThread = new NetworkThread();
		mNetworkThread.setPriority(Thread.MAX_PRIORITY);
		mNetworkThread.start();
	}

	class NetworkThread extends Thread {

		@Override
		public void run() {
			Looper.prepare();
			try {
				URL obj = new URL(mUrl);
				HttpURLConnection mHttpURLConnection = (HttpURLConnection) obj
						.openConnection();
				mHttpURLConnection.setRequestMethod("GET");
				BufferedReader in = new BufferedReader(new InputStreamReader(
						mHttpURLConnection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				mResponse = response.toString();
			} catch (Exception e) {
				DigiCareLogger.e(
						TAG,
						"Failed to fetch Response Data : "
								+ e.getLocalizedMessage());
			} finally {
				Log.d(TAG, "Response: [" + mResponse + "]");
				notifyResponseHandler();
			}
			Looper.loop();
		}
	}

	protected void notifyResponseHandler() {
		if (mResponse != null) {

			mResponseCallback.onResponseReceived(mResponse);
		}
	}
}
