package com.philips.cl.di.reg.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class NetworkStateReceiver extends BroadcastReceiver {

	@SuppressLint("InlinedApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();

		if (isConnected && !NetworkUtility.getInstance().isOnline()) {
			RLog.d(RLog.NETWORK_STATE, "Network state : " + isConnected);
			NetworkUtility.getInstance().setOnline(true);
			EventHelper.getInstance().notifyEventOccurred(
					RegConstants.IS_ONLINE);
		} else if (!isConnected && NetworkUtility.getInstance().isOnline()) {
			RLog.d(RLog.NETWORK_STATE, "Network state : " + isConnected);
			NetworkUtility.getInstance().setOnline(false);
			EventHelper.getInstance().notifyEventOccurred(
					RegConstants.IS_ONLINE);
		}
	}
}
