package com.philips.cl.di.dev.pa.newpurifier;

import android.os.Handler.Callback;

import com.philips.cl.di.common.ssdp.lib.SsdpService;

/**
 * Discovery of the device is managed by Discovery Manager. It is the main
 * interface to the User Interface. The output of Discovery Manager is the list
 * of PurAirDevice which is further handled by User Interface and Purifier
 * Manager. In order to build this list, the Discovery Manager makes use of
 * input from SSDP, a pairing database and network changes.
 * 
 * @author Jeroen Mols
 * @date 30 Apr 2014
 */
public class DiscoveryManager {

	private static DiscoveryManager mInstance;

	public static DiscoveryManager getInstance() {
		if (mInstance == null) {
			mInstance = new DiscoveryManager();
		}
		return mInstance;
	}

	private DiscoveryManager() {
		// Enforce Singleton
	}

	public void start(Callback callback) {
		SsdpService.getInstance().startDeviceDiscovery(callback);
	}
	
	public void stop() {
		SsdpService.getInstance().stopDeviceDiscovery();
	}
}
