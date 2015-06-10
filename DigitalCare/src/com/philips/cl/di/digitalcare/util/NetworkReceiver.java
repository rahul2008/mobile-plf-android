package com.philips.cl.di.digitalcare.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.cl.di.digitalcare.NetworkStateListener;

public class NetworkReceiver extends BroadcastReceiver {

	private final String TAG = NetworkReceiver.class.getSimpleName();
	private NetworkStateListener mNetworkCallback = null;

	public NetworkReceiver() {
	}

	public NetworkReceiver(NetworkStateListener callback) {
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
					mNetworkCallback.onNetworkStateChanged(true);
			} else {
				DLog.v(TAG, "Connection Not Available");

				if (mNetworkCallback != null)
					mNetworkCallback.onNetworkStateChanged(false);

			}
		}

	}

}
