package com.philips.amwelluapp.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import java.util.List;

//TODO: Review Comment - Spoorti - Use generics for type safety
public interface PTHProvidersListCallback {
    void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError);
    void onProvidersListFetchError(Throwable throwable);
}
