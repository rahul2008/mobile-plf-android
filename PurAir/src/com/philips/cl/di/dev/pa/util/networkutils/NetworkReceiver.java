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
import com.philips.cl.di.dev.pa.util.ALog;

public class NetworkReceiver extends BroadcastReceiver implements InternetConnectionlistener {
	
	private static NetworkReceiver smInstance;
	private List<NetworkStateListener> networkStateListeners;
	private ConnectionState lastKnownNetworkState;
	private IntentFilter filter;
	private boolean internetConnected;
	
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
				
				lastKnownNetworkState = ConnectionState.CONNECTED;
				notfiyListeners(lastKnownNetworkState);
				VerifyInternetConnectionThread vThread = new VerifyInternetConnectionThread(this);
				vThread.start();
			} else {
				ALog.i(ALog.CONNECTIVITY, "NR$onReceive---NOT CONNECTED");
				
				lastKnownNetworkState = ConnectionState.DISCONNECTED; 
				notfiyListeners(lastKnownNetworkState);
			}
		}
	}

	private void notfiyListeners(ConnectionState state) {
		ALog.i(ALog.CONNECTIVITY, "NR$notifyListeners networkStateListeners " + networkStateListeners.size());
		for(NetworkStateListener listener : networkStateListeners) {
			if(ConnectionState.CONNECTED == state) {
				ALog.i(ALog.CONNECTIVITY, "NR$notify onConnected");
				listener.onConnected();
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
	
	public boolean isInternetConnected() {
		return internetConnected;
	}

	@Override
	public void updateInternetState(boolean internetConnected) {
		this.internetConnected = internetConnected;
		
	}
}
