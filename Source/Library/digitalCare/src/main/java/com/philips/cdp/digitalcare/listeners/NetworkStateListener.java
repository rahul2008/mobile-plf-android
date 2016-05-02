/**
 * @author naveen@philips.com
 *
 * <p> NetworkListener Callback method used to to listen network changes across the DigitalCare component fragments </p>
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.listeners;



public interface NetworkStateListener {

	void onNetworkStateChanged(boolean connectionStatus);

}
