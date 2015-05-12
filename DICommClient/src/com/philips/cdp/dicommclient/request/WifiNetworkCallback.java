package com.philips.cdp.dicommclient.request;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;

import com.philips.cdp.dicomm.util.ALog;

@SuppressLint("NewApi")
class WifiNetworkCallback extends NetworkCallback {

		private Object lock;
		private Network wifiNetwork = null;

		public WifiNetworkCallback(Object lock) {
			this.lock = lock;
		}

		public Network getNetwork() {
			return wifiNetwork;
		}

		@Override
		public void onAvailable(final Network network) {
			ALog.i(ALog.WIFI, "WifiNetwork available");
			wifiNetwork = network;
			synchronized (lock) {
				lock.notify();
			}
		}

		@Override
		public void onLost(Network network) {
			ALog.i(ALog.WIFI, "WifiNetwork lost");
			ConnectivityManager.setProcessDefaultNetwork(null);
			super.onLost(network);
		}
	}