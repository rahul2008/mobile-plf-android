package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;

public interface THSPreferredPharmacyCallback {
    void onPharmacyReceived(Pharmacy pharmacy, SDKError sdkError);
    void onFailure(Throwable throwable);

}
