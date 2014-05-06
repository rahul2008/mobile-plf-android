package com.philips.cl.di.dev.pa.newpurifier;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.philips.cl.di.dev.pa.util.ALog;

public class NetworkMonitor {

	private ConnectivityManager mConnectivityManager;
	private boolean mWifiEnabled = false;
	private boolean mConnectivityEnabled = false;
	public NetworkChangedCallback mNetworkChangedCallback;
	private BroadcastReceiver mNetworkChangedReceiver;
	private static NetworkStates mCurrentNetworkState = NetworkStates.NONE;
	public enum NetworkStates {
		MOBILE,
		WIFI_NO_INTERNET,
		WIFI_WITH_INTERNET,
		NONE
	}
	private LooperThread mLooper;

	public NetworkMonitor(Context context, NetworkChangedCallback listener) {
		mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		mNetworkChangedCallback = listener;
		mLooper = new LooperThread();
		mLooper.start();

		mNetworkChangedReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("FRANK", "onReceive called : " + intent.getAction());
				// Check connectivity type

				getCurrentNetworkState();
				sendNetworkChanged(mCurrentNetworkState);
			};
		};
	}

	public interface NetworkChangedCallback {
		void onNetworkChanged(NetworkStates networkState);
	}

	public void startNetworkChangedReceiver(Context context) {
		// Start connectivity changed receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(mNetworkChangedReceiver, filter);
	}

	public void stopNetworkChangedReceiver(Context context) {
		context.unregisterReceiver(mNetworkChangedReceiver);
	}

	public NetworkStates getCurrentNetworkState() {
		getConnectivityType();
		return mCurrentNetworkState;
	}

	private NetworkStates getConnectivityType() {
		// Check connectivity type
		NetworkInfo currentNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

		if (currentNetworkInfo != null) {
			if ((currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
					&& currentNetworkInfo.isConnected()) {
				mWifiEnabled = true;

				// Set default network state on wifi with connectivity
				mCurrentNetworkState = NetworkStates.WIFI_WITH_INTERNET;

				// Start checking the current internet state
				checkForDataConnection();
			} else if ((currentNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
					&& currentNetworkInfo.isConnected()) {
				mWifiEnabled = false;
				// Notify listeners that the network state is changed
				determineNetworkState(mWifiEnabled, true);
			} else {
				mWifiEnabled = false;
				// Notify listeners that the network state is changed
				determineNetworkState(mWifiEnabled, false);
			}
		}
		return mCurrentNetworkState;
	}

	private void checkForDataConnection() {
		// TODO determine way to buffer events
		mLooper.mHandler.sendEmptyMessage(0);
	}

	private void determineNetworkState(boolean wifiEnabled,	boolean dataConnection) {
		if (dataConnection) {
			if (wifiEnabled) {
				// SSDP + CPP possible
				Log.d("FRANK", "ssdp +cpp (wifi + internet)");
				mCurrentNetworkState = NetworkStates.WIFI_WITH_INTERNET;
			} else {
				// CPP possible
				Log.d("FRANK", "cpp only (mobile)");
				mCurrentNetworkState = NetworkStates.MOBILE;
			}
		} else {
			if (wifiEnabled) {
				// SSDP possible
				Log.d("FRANK", "ssdp only (wifi no internet)");
				mCurrentNetworkState = NetworkStates.WIFI_NO_INTERNET;
			} else {
				// No possibilities to connect to the purifier
				Log.d("FRANK", "no mobile or wifi");
				mCurrentNetworkState = NetworkStates.NONE;
			}
		}
	}

	private void sendNetworkChanged(NetworkStates state) {
		switch (state) {
		case MOBILE: ALog.d("FRANK", "Mobile data connection"); break;
		case WIFI_WITH_INTERNET: ALog.d("FRANK", "Wifi with internet"); break;
		case WIFI_NO_INTERNET: ALog.d("FRANK", "Wifi without internet"); break;
		case NONE: ALog.d("FRANK", "No connection"); break;
		}

		mNetworkChangedCallback.onNetworkChanged(state);

	}
	
	class LooperThread extends Thread {
	      public Handler mHandler;

	      public void run() {
	          Looper.prepare();

	          mHandler = new Handler() {
	              public void handleMessage(Message msg) {
	            	  HttpURLConnection urlConnection = null;
	  				try {
	  					URL url = new URL("http://www.google.com/"); // TODO: CPP
	  																	// address???
	  					urlConnection = (HttpURLConnection) url.openConnection();

	  					Timer timer = new Timer();
	  					TimerTask task = new TimerTask() {
	  						@Override
	  						public void run() {
	  							try {
	  								mConnectivityEnabled = false;
	  								Log.d("FRANK", "callback called wifienabled: "
	  										+ mWifiEnabled + " connectivity: "
	  										+ mConnectivityEnabled);
	  								// Notify listeners that the network state is
	  								// changed
	  								determineNetworkState(mWifiEnabled,
	  										mConnectivityEnabled);
	  								this.finalize();
	  							} catch (Throwable e) {
	  								e.printStackTrace();
	  							}
	  						}
	  					};

	  					// Start timeout since IOException takes too long to receive
	  					// (100 sec)
	  					timer.schedule(task, 5000);
	  					mConnectivityEnabled = (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK);

	  					// Received server response so timeout not needed anymore
	  					timer.cancel();

	  					// Notify listeners that the network state is changed
	  					determineNetworkState(mWifiEnabled, mConnectivityEnabled);
	  				} catch (MalformedURLException e) {
	  					e.printStackTrace();
	  				} catch (IOException e) {
	  					e.printStackTrace();
	  				} finally {
	  					urlConnection.disconnect();
	  				}
	              }
	          };

	          Looper.loop();
	      }
	  }

}