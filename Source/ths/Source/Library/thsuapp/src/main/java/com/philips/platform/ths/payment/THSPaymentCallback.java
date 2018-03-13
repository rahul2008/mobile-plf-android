/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import java.util.Map;


public interface THSPaymentCallback {

    interface THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError> {
        void onGetPaymentMethodResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError);

        void onGetPaymentFailure(Throwable throwable);

    }

    interface THSgetPaymentMethodValidatedCallback<THSPaymentMethod, THSSDKError> extends THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError> {
        void onValidationFailure(Map<String, String> map);

    }

}
