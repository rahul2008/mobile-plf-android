package com.philips.amwelluapp.providerdetails;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;

public interface PTHProviderDetailsCallback {
    void onProviderDetailsReceived(Provider provider, SDKError sdkError);
    void onProviderDetailsFetchError(Throwable throwable);
}
