package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.manager.ValidationReason;

import java.util.List;
import java.util.Map;

public interface THSGetPharmaciesCallback {

    void onValidationFailure(Map<String, ValidationReason> map);
    void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError);
    void onFailure(Throwable throwable);
}
