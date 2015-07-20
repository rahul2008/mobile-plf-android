package com.philips.cdp.digitalcare;

/**
 * @author naveen@philips.com
 * 
 * <p> NetworkListener Callback method used to to listen network changes across the DigitalCare component fragments </p>
 */

public interface NetworkStateListener {

	void onNetworkStateChanged(boolean connectionStatus);

}
