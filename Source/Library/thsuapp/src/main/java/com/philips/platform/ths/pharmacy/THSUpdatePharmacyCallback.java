package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.SDKError;

public interface THSUpdatePharmacyCallback {

    void onUpdateSuccess(SDKError sdkError);
    void onUpdateFailure(Throwable throwable);
}
