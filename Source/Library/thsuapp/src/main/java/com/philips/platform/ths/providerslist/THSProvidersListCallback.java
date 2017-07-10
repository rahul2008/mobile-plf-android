package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import java.util.List;

//TODO: Review Comment - Spoorti - Use generics for type safety
public interface THSProvidersListCallback {
    void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError);
    void onProvidersListFetchError(Throwable throwable);
}
