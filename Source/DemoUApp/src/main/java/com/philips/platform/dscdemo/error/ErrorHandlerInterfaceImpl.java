package com.philips.platform.dscdemo.error;

import com.philips.platform.core.ErrorHandlingInterface;

public class ErrorHandlerInterfaceImpl implements ErrorHandlingInterface {
    @Override
    public void syncError(int error) {
    }

    @Override
    public void onServiceDiscoveryError(String error) {

    }
}
