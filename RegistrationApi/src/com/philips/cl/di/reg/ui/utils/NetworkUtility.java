
package com.philips.cl.di.reg.ui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.cl.di.reg.settings.RegistrationHelper;

public class NetworkUtility {
	
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			int size = info.length;
			if (info != null) {
				for (int i = 0; i < size; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static class NetworkStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (NetworkUtility.isNetworkAvailable(context)) {
				RLog.d(RLog.NETWORK_STATE, "Network state : true");
				if(null!=RegistrationHelper.getInstance().getNetworkStateListener()){
					RegistrationHelper.getInstance().getNetworkStateListener()
			        .onNetWorkStateReceived(true);
				}
				
			} else {
				RLog.d(RLog.NETWORK_STATE, "Network state : false");
				if(null!=RegistrationHelper.getInstance().getNetworkStateListener()){
					RegistrationHelper.getInstance().getNetworkStateListener()
			        .onNetWorkStateReceived(false);
				}
			}
		}
	}
}
