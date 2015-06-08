package com.philips.cl.di.digitalcare.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.NetworkAlertView;

/**
 * Utils class contains common utility methods required across framework under
 * different scenario's.
 * 
 * @author naveen@philips.com
 * 
 * @since Feb 10, 2015
 * 
 */
public class Utils {

	public static boolean isNetworkConnected(Activity activity) {
		ConnectivityManager mConnectManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectManager != null) {
			NetworkInfo[] mNetworkInfo = mConnectManager.getAllNetworkInfo();
			for (int i = 0; i < mNetworkInfo.length; i++) {
				if (mNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
					return true;
			}
		}

		/*
		 * Toast toast = Toast.makeText(activity, activity.getResources()
		 * .getString(R.string.no_internet), Toast.LENGTH_SHORT);
		 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show();
		 */
		new NetworkAlertView().showNetworkAlert(activity);

		AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_KEY_SET_ERROR,
				AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
				AnalyticsConstants.TECHNICAL_ERROR_NETWORK_CONNECITON);

		return false;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isEmpty(String value) {
		if (value == null) {
			return true;
		}
		return false;
	}

	public static boolean isSimAvailable(Context mContext) {
		TelephonyManager mTelephonyService = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		// int state = mTelephonyService.getPhoneType();
		// switch (state) {
		// case TelephonyManager.PHONE_TYPE_GSM:
		// return true;
		// case TelephonyManager.PHONE_TYPE_CDMA:
		// return true;
		// case TelephonyManager.PHONE_TYPE_SIP:
		// return true;
		// case TelephonyManager.PHONE_TYPE_NONE:
		// return false;
		// }
		// return false;

		int simState = mTelephonyService.getSimState();
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return false;
		default:
			return true;
		}
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
