package com.philips.cl.di.digitalcare;

public interface NetworkStateListener {

	abstract void onNetworkStateChanged(boolean connectionStatus);

}
