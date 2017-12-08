/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

public interface THSUpdateShippingAddressCallback {
    void onAddressValidationFailure(Map<String, ValidationReason> map);
    void onUpdateSuccess(Address address, SDKError sdkErro);
    void onUpdateFailure(Throwable throwable);
}
