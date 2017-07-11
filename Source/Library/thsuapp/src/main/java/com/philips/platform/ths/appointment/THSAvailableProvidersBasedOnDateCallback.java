package com.philips.platform.ths.appointment;

public interface THSAvailableProvidersBasedOnDateCallback<List, THSSDKError> {
    public void onResponse(List availableProviders, THSSDKError sdkError);
    public void onFailure(Throwable throwable);
}
