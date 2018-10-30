/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;

import java.util.List;
import java.util.Map;

public interface THSGetPharmaciesCallback {

    void onValidationFailure(@NonNull Map<String, String> map);
    void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError);
    void onFailure(Throwable throwable);
}
