package com.philips.platform.core;

public interface ErrorHandlingInterface {
    void syncError(int error);
    void onServiceDiscoveryError(String error);
}
