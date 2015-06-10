package com.philips.cl.di.digitalcare.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.cl.di.digitalcare.NetworkCallback;

public class NetworkUtility extends BroadcastReceiver {

	private final String TAG = NetworkUtility.class.getSimpleName();
	private NetworkCallback mNetworkCallback = null;

	public NetworkUtility() {
	}

	public NetworkUtility(NetworkCallback callback) {
		this();
		mNetworkCallback = callback;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		ConnectivityManager mConnectionManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectionManager != null) {
			NetworkInfo mActiveNetwork = mConnectionManager
					.getActiveNetworkInfo();
			if (mActiveNetwork != null) {
				DLog.v(TAG, "Connection Available");
				if (mNetworkCallback != null)
					mNetworkCallback.onConnectionChanged(true);
			} else {
				DLog.v(TAG, "Connection Not Available");

				if (mNetworkCallback != null)
					mNetworkCallback.onConnectionChanged(false);

			}
		}

	}

}
