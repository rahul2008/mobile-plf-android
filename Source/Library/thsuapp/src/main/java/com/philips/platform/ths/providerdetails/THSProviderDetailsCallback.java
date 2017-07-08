package com.philips.platform.ths.providerdetails;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;

public interface THSProviderDetailsCallback {
    void onProviderDetailsReceived(Provider provider, SDKError sdkError);
    void onProviderDetailsFetchError(Throwable throwable);
}
