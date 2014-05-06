package com.philips.cl.di.dev.pa.newpurifier;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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

	public NetworkChangedCallback mNetworkChangedCallback;
	private BroadcastReceiver mNetworkChangedReceiver;
	private static NetworkState mLastKnownState = NetworkState.NONE;

	public enum NetworkState {
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
		
		mNetworkChangedReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				ALog.d(ALog.NETWORKMONITOR, "onReceive connectivity action : " + intent.getAction());
				updateNetworkStateAsync();
			};
		};
	}

	public interface NetworkChangedCallback {
		void onNetworkChanged(NetworkState networkState);
	}

	public void startNetworkChangedReceiver(Context context) {
		mLooper = new LooperThread();
		mLooper.start();
		
		// Start connectivity changed receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(mNetworkChangedReceiver, filter);
		
		updateNetworkStateAsync();
	}

	public void stopNetworkChangedReceiver(Context context) {
		context.unregisterReceiver(mNetworkChangedReceiver);
		
		mLooper.mHandler.getLooper().quit();
	}

	public NetworkState getLastKnownNetworkState() {
		return mLastKnownState;
	}
	
	private void updateNetworkStateAsync() {
		if (mLooper == null || mLooper.mHandler == null) return;
		mLooper.mHandler.sendEmptyMessage(0);
	}

	private void updateNetworkState(NetworkState newState) {
		if (mLastKnownState == newState) {
			ALog.d(ALog.NETWORKMONITOR, "Detected same networkState - no need to update listener");
			return;
		}
		
		ALog.d(ALog.NETWORKMONITOR, "NetworkState Changed - updating listener");
		mLastKnownState = newState;
		mNetworkChangedCallback.onNetworkChanged(newState);
	}
	
	private class LooperThread extends Thread {
		public Handler mHandler;

		public void run() {
			Looper.prepare();

			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
					
					boolean isConnected = activeNetwork != null	&& activeNetwork.isConnectedOrConnecting();
					if (!isConnected) {
						ALog.d(ALog.NETWORKMONITOR, "Network update - No connection");
						updateNetworkState(NetworkState.NONE);
						return;
					}
					
					boolean isMobileData = activeNetwork.getType() != ConnectivityManager.TYPE_WIFI;
					if (isMobileData) {
						// Assume internet access - don't waste data bandwidth
						ALog.d(ALog.NETWORKMONITOR, "Network update - Mobile connection");
						updateNetworkState(NetworkState.MOBILE);
						return;
					}
					
					boolean isInternetAvailable = checkForInternetConnection();
					if (isInternetAvailable) {
						ALog.d(ALog.NETWORKMONITOR, "Network update - Wifi with internet");
						updateNetworkState(NetworkState.WIFI_WITH_INTERNET);
						return;
					}
					
					ALog.d(ALog.NETWORKMONITOR, "Network update - Wifi without internet");
					updateNetworkState(NetworkState.WIFI_NO_INTERNET);
				}
			};

			Looper.loop();
		}
	}
	
	private boolean checkForInternetConnection() {
		String uri = "http://www.google.com";
		boolean isInternetAvailable = false;

		final HttpClient httpClient = new DefaultHttpClient();
		final HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 2000);
		HttpConnectionParams.setSoTimeout(params, 2000);
		final HttpGet get = new HttpGet(uri);

		try {
			final HttpResponse response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ALog.d(ALog.NETWORKMONITOR, "Internet connection available");
				return true;
			}
		} catch (final UnsupportedEncodingException e) {
			// NOP
		} catch (final ClientProtocolException e) {
			// NOP
		} catch (final IOException e) {
			// NOP
		}
		return isInternetAvailable;
	}


}