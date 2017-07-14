package com.philips.platform.ths.appointment;

public interface THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError> {
    public void onResponse(THSAvailableProviderList availableProviders, THSSDKError sdkError);
    public void onFailure(Throwable throwable);
}
