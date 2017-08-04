/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.SDKError;

public interface THSUpdatePharmacyCallback {

    void onUpdateSuccess(SDKError sdkError);
    void onUpdateFailure(Throwable throwable);
}
