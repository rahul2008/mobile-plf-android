/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;

public interface THSPreferredPharmacyCallback {
    void onPharmacyReceived(Pharmacy pharmacy, SDKError sdkError);
    void onFailure(Throwable throwable);

}