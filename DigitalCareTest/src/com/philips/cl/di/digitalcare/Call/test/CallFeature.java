package com.philips.cl.di.digitalcare.Call.test;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 
 * @author naveen@philips.com
 * @description This Class checks whether the device is running with
 *                active SIM.
 * @Since Mar 5, 2015
 */
public class CallFeature {

	public boolean isSimAvailable(Activity mContext) {
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
