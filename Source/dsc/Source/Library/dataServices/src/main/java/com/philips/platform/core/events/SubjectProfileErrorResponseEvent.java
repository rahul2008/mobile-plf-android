package com.philips.platform.core.events;

import com.philips.platform.core.utils.DataServicesError;

public class SubjectProfileErrorResponseEvent extends Event {
    private DataServicesError mDataServicesError;

    public SubjectProfileErrorResponseEvent(DataServicesError dataServicesError) {
        mDataServicesError = dataServicesError;
    }

    public DataServicesError getDataServicesError() {
        return mDataServicesError;
    }

}
