package com.philips.cl.di.common.ssdp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.philips.cl.di.common.ssdp.lib.SsdpService;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class WifiStatusReceiver extends BroadcastReceiver {

	/**
	 * Method onReceive.
	 * 
	 * @param context
	 *            Context
	 * @param intent
	 *            Intent
	 */
	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (null != intent) {
			final String action = intent.getAction();
			if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
				final SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
				if (SupplicantState.isValidState(state)) {
					if (state == SupplicantState.COMPLETED) {
						// SSDP.getInstance().startDeviceDiscovery();
						Log.v("UI", "############ WIFI CONNECTED  ############## ");
					} else if (SupplicantState.DISCONNECTED == state) {
						Log.v("UI", "############ WIFI DISCONNECTED ############ ");
						SsdpService.getInstance().stopDeviceDiscovery();
					}
				}
			}
		}

	}

}
