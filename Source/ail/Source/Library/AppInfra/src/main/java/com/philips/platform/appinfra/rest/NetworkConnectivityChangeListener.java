package com.philips.platform.appinfra.rest;

/**
 * Created by abhishek on 5/22/18.
 */

public interface NetworkConnectivityChangeListener {
    void onConnectivityStateChange(boolean isConnected);
}
