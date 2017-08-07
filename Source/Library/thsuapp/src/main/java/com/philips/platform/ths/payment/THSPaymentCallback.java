/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;


public interface THSPaymentCallback {

    interface THSSDKCallBack<THSPaymentMethod, THSSDKError> {
        void onResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError);

        void onFailure(Throwable throwable);

    }

    interface THSSDKValidatedCallback<THSPaymentMethod, THSSDKError> extends THSSDKCallBack<THSPaymentMethod, THSSDKError> {
        void onValidationFailure(Map<String, ValidationReason> map);

    }

}
