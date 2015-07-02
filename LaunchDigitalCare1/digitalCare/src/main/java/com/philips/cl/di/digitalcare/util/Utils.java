package com.philips.cl.di.digitalcare.util;

import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;

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

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isSimAvailable(Context mContext) {
		TelephonyManager mTelephonyService = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = mTelephonyService.getSimState();
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return false;
		default:
			return true;
		}
	}
}
