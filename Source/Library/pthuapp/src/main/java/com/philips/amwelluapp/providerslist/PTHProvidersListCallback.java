package com.philips.amwelluapp.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.ProviderInfo;

import java.util.List;

public interface PTHProvidersListCallback {
    void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError);
    void onProvidersListFetchError(Throwable throwable);
}
