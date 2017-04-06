package com.philips.platform.core.events;

import com.philips.platform.core.utils.DataServicesError;

public class PushNotificationErrorResponse extends Event {
    private DataServicesError dataServicesError;

    public PushNotificationErrorResponse(DataServicesError dataServicesError) {
        this.dataServicesError = dataServicesError;
    }

    public DataServicesError getDataServicesError() {
        return dataServicesError;
    }
}
