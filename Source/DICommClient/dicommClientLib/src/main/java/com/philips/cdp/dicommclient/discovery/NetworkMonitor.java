/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.philips.cdp.dicommclient.util.DICommLog;

public class NetworkMonitor {

	private final Context mContext;
	private ConnectivityManager mConnectivityManager;
	private WifiManager mWifiManager;

	public NetworkChangedCallback mNetworkChangedCallback;
	private BroadcastReceiver mNetworkChangedReceiver;

	private static NetworkState mLastKnownState = NetworkState.NONE;
	private static String mLastKnownSsid = null;

	public enum NetworkState {
		MOBILE,
		WIFI_WITH_INTERNET,
		NONE
	}
	
	private LooperThread mLooper;

	public NetworkMonitor(Context context) {
		mContext = context;
		mConnectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);

		mNetworkChangedReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				DICommLog.d(DICommLog.NETWORKMONITOR, "onReceive connectivity action : " + intent.getAction());
				updateNetworkStateAsync();
			};
		};
	}
	
	public void setListener(NetworkChangedCallback listener) {
		mNetworkChangedCallback = listener;
	}

	public void startNetworkChangedReceiver() {
		mLooper = new LooperThread();
		mLooper.start();

		// Start connectivity changed receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(mNetworkChangedReceiver, filter);

		updateNetworkStateAsync();
	}

	public void stopNetworkChangedReceiver() {
		try {
			mContext.unregisterReceiver(mNetworkChangedReceiver);
		} catch (IllegalArgumentException e) {
			DICommLog.e(DICommLog.NETWORKMONITOR, "Error: " + e.getMessage());
		}
		if (mLooper ==  null || mLooper.mHandler == null || mLooper.mHandler.getLooper() == null) return;
		mLooper.mHandler.getLooper().quit();
	}

	/*
	 * Synchronized to make mLastKnowState Threadsafe
	 */
	public synchronized NetworkState getLastKnownNetworkState() {
		return mLastKnownState;
	}

	public synchronized String getLastKnownNetworkSsid() {
		return mLastKnownSsid;
	}

	private void updateNetworkStateAsync() {
		if (mLooper == null || mLooper.mHandler == null) return;
		mLooper.mHandler.sendEmptyMessage(0);
	}

	/*
	 * Synchronized to make mLastKnowState Threadsafe
	 */
	private synchronized void updateNetworkState(NetworkState newState, String newSsid) {
		if (mLastKnownState == newState && isLastKnowSsid(newSsid)) {
			DICommLog.d(DICommLog.NETWORKMONITOR, "Detected same networkState - no need to update listener");
			return;
		}

		if (mLastKnownState == newState && !isLastKnowSsid(newSsid)) {
			DICommLog.d(DICommLog.NETWORKMONITOR, "Detected rapid change of Wifi networks - sending intermediate disconnect event");
			notifyListener(NetworkState.NONE, null);
		}

		if (mLastKnownState == NetworkState.MOBILE && newState == NetworkState.WIFI_WITH_INTERNET
				|| mLastKnownState == NetworkState.WIFI_WITH_INTERNET && newState == NetworkState.MOBILE) {
			DICommLog.d(DICommLog.NETWORKMONITOR, "Detected rapid change between wifi and data - sending intermediate disconnect event");
			notifyListener(NetworkState.NONE, null);
		}

		DICommLog.d(DICommLog.NETWORKMONITOR, "NetworkState Changed");
		mLastKnownState = newState;
		mLastKnownSsid = newSsid;
		notifyListener(newState, newSsid);
	}

	private void notifyListener(NetworkState state, String ssid) {
		if (mNetworkChangedCallback == null) return;
		
		DICommLog.v(DICommLog.NETWORKMONITOR, "Updating listener");
		mNetworkChangedCallback.onNetworkChanged(state, ssid);
	}

	private void loadNetworkStateSynchronous() {
		NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();

		boolean isConnected = activeNetwork != null	&& activeNetwork.isConnectedOrConnecting();
		if (!isConnected) {
			DICommLog.d(DICommLog.NETWORKMONITOR, "Network update - No connection");
			updateNetworkState(NetworkState.NONE, null);
			return;
		}

		boolean isMobileData = activeNetwork.getType() != ConnectivityManager.TYPE_WIFI;
		if (isMobileData) {
			WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE) ;
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if(wifiInfo == null ||
					wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
				// Assume internet access - don't waste data bandwidth
				DICommLog.d(DICommLog.NETWORKMONITOR, "Network update - Mobile connection");
				updateNetworkState(NetworkState.MOBILE, null);
				return;
			}
		}

		String ssid = getCurrentSsid();
		// Assume internet access - checking for internet technically difficult (slow DNS timeout)
		DICommLog.d(DICommLog.NETWORKMONITOR, "Network update - Wifi with internet (" + (ssid == null ? "< unknown >" : ssid) + ")");
		updateNetworkState(NetworkState.WIFI_WITH_INTERNET, ssid);
		return;
	}

	@SuppressLint("HandlerLeak")
	private class LooperThread extends Thread {
		public Handler mHandler;

		public void run() {
			Looper.prepare();

			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					loadNetworkStateSynchronous();
				}
			};

			Looper.loop();
		}
	}

	private boolean isLastKnowSsid(String ssid) {
		if (ssid == null && mLastKnownSsid == null) {
			return true;
		}
		if (ssid != null && ssid.equals(mLastKnownSsid)) {
			return true;
		}
		return false;
	}

	private String getCurrentSsid() {
		String ssid  = null;
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		if (wifiInfo != null) {
			ssid = wifiInfo.getSSID();
		}

		if (ssid == null || ssid.isEmpty()) {
			return null;
		}
		return ssid;
	}


	public interface NetworkChangedCallback {
		void onNetworkChanged(NetworkState networkState, String networkSsid);
	}

}
