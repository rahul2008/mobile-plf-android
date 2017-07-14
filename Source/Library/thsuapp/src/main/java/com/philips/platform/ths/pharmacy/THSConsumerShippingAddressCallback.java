package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;

public interface THSConsumerShippingAddressCallback {
    void onSuccessfulFetch(Address address, SDKError sdkError);
    void onFailure(Throwable throwable);

}
