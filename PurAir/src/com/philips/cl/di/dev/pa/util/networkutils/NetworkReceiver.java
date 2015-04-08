package com.philips.cl.di.dev.pa.util.networkutils;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.util.ALog;

public class NetworkReceiver extends BroadcastReceiver {
	
	private static NetworkReceiver smInstance;
	private List<NetworkStateListener> networkStateListeners;
	private ConnectionState lastKnownNetworkState;
	private IntentFilter filter;
	
	public enum ConnectionState {
		CONNECTED, 
		DISCONNECTED
	}
	
	private NetworkReceiver() {
		networkStateListeners = new ArrayList<NetworkStateListener>();
		filter = new IntentFilter();
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction("android.location.PROVIDERS_CHANGED");
		
		lastKnownNetworkState = ConnectionState.DISCONNECTED;
	}
	
	public void registerNetworkReceiver() {
		PurAirApplication.getAppContext().registerReceiver(this, filter);
	}
	
	public void unregisterNetworkReceiver() {
		try {
			PurAirApplication.getAppContext().unregisterReceiver(this);
		} catch (Exception e) {
			ALog.e(ALog.CONNECTIVITY, "ERROR : CAN'T UNREGISTER RECEIVER");
		}
	}
	
	public static NetworkReceiver getInstance() {
		if(smInstance == null)  {
			smInstance = new NetworkReceiver();
		}
		return smInstance;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {

		ConnectivityManager conMan = (ConnectivityManager) PurAirApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)	
				|| intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			NetworkInfo netInfo = conMan.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				ALog.i(ALog.CONNECTIVITY, "NR$onReceive---CONNECTED");
				String ssid = EWSWifiManager.getSsidOfSupplicantNetwork();
				lastKnownNetworkState = ConnectionState.CONNECTED;
				notfiyListeners(lastKnownNetworkState, ssid);
			} else {
				ALog.i(ALog.CONNECTIVITY, "NR$onReceive---NOT CONNECTED");
				
				lastKnownNetworkState = ConnectionState.DISCONNECTED; 
				notfiyListeners(lastKnownNetworkState, "");
			}
		}
		
		if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
			OutdoorController.getInstance().setLocationProvider();
		}
	}

	private void notfiyListeners(ConnectionState state, String ssid) {
		ALog.i(ALog.CONNECTIVITY, "NR$notifyListeners networkStateListeners " + networkStateListeners.size());
		for(NetworkStateListener listener : networkStateListeners) {
			if(ConnectionState.CONNECTED == state) {
				ALog.i(ALog.CONNECTIVITY, "NR$notify onConnected");
				listener.onConnected(ssid);
			} else if (ConnectionState.DISCONNECTED == state) {
				ALog.i(ALog.CONNECTIVITY, "NR$notify onDisconnected");
				listener.onDisconnected();
			}
		}
	}
	
	public void addNetworkStateListener(NetworkStateListener listener) {
		ALog.i(ALog.CONNECTIVITY, "NR$addNetworkStateListener");
		networkStateListeners.add(listener);
	}
	
	public void removeNetworkStateListener(NetworkStateListener listener) {
		networkStateListeners.remove(listener);
	}
	
	public ConnectionState getLastKnownNetworkState() {
		return lastKnownNetworkState;
	}
}
