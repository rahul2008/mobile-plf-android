package com.philips.platform.core.events;

import com.philips.platform.core.utils.DataServicesError;

public class DevicePairingErrorResponseEvent extends Event {

    private DataServicesError mDataServicesError;

    public DevicePairingErrorResponseEvent(DataServicesError dataServicesError) {
        mDataServicesError = dataServicesError;
    }

    public DataServicesError getDataServicesError() {
        return mDataServicesError;
    }
}
