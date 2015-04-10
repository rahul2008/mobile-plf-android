package com.philips.cl.di.reg.ui.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Vinayak Udikeri
 */
public class NetworkUtility {

	private static NetworkUtility mNetworkUtility;

	private boolean mIsOnline;

	/** Network Utility */
	private NetworkUtility() {
	}

	/**
	 * @return NetworkUtility sigle ton object.
	 */
	public static NetworkUtility getInstance() {
		if (mNetworkUtility == null) {
			synchronized (NetworkUtility.class) {
				if (mNetworkUtility == null) {
					mNetworkUtility = new NetworkUtility();
				}
			}
		}
		return mNetworkUtility;
	}

	/**
	 * @return the isOnline
	 */
	public boolean isOnline() {
		return mIsOnline;
	}

	/**
	 * @param isOnline
	 *            the isOnline to set
	 */
	public void setOnline(boolean isOnline) {
		this.mIsOnline = isOnline;
	}

	/**
	 * Called on start of app.
	 * 
	 * @param context
	 *            {@link Context}
	 */
	public void checkIsOnline(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						setOnline(true);
						return;
					}
				}
			}
		}
		setOnline(false);
	}
}
