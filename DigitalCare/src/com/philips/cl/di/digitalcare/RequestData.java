package com.philips.cl.di.digitalcare;

/**
 * @author naveen@philips.com
 * 
 * <p> Common class to receive the response from the remote network URL. </p>
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Looper;

import com.philips.cl.di.digitalcare.util.DigiCareLogger;

public class RequestData {

	private final String TAG = RequestData.class.getSimpleName();

	private ResponseCallback mResponseCallback = null;
	private String mResponse = null;
	private String mUrl = null;
	private Handler mHandler = null;

	public RequestData(String url, ResponseCallback responseCallback) {
		mUrl = url;
		mResponseCallback = responseCallback;
		mHandler = new Handler(Looper.getMainLooper());
	}

	public void execute() {
		NetworkThread mNetworkThread = new NetworkThread();
		mNetworkThread.setPriority(Thread.MAX_PRIORITY);
		mNetworkThread.start();
	}

	class NetworkThread extends Thread {

		@Override
		public void run() {
			try {
				URL obj = new URL(mUrl);
				HttpURLConnection mHttpURLConnection = (HttpURLConnection) obj
						.openConnection();
				mHttpURLConnection.setRequestMethod("GET");
				InputStream mInputStream = mHttpURLConnection.getInputStream();
				Reader mReader = new InputStreamReader(mInputStream, "UTF-8");
				BufferedReader in = new BufferedReader(mReader);
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
				DigiCareLogger.d(TAG, "Response: [" + mResponse + "]");
				notifyResponseHandler();
			}
		}
	}

	protected void notifyResponseHandler() {
		if (mResponse != null) {

			mHandler.postAtFrontOfQueue(new Runnable() {
				@Override
				public void run() {
					mResponseCallback.onResponseReceived(mResponse);
				}
			});

		}
	}

}
