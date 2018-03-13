/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;

import java.util.Map;

public interface THSUpdateShippingAddressCallback {
    void onAddressValidationFailure(@NonNull Map<String, String> map);
    void onUpdateSuccess(Address address, SDKError sdkErro);
    void onUpdateFailure(Throwable throwable);
}
