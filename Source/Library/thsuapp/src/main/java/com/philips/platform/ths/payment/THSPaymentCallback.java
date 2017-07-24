package com.philips.platform.ths.payment;

import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

/**
 * Created by philips on 7/21/17.
 */

public interface THSPaymentCallback {

    interface THSSDKCallBack <THSPaymentMethod, THSSDKError>{
        void onResponse (THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError);
        void onFailure(Throwable throwable);

    }

    interface THSSDKValidatedCallback <THSPaymentMethod, THSSDKError> extends THSSDKCallBack <THSPaymentMethod, THSSDKError>{
        void onValidationFailure(Map<String, ValidationReason> map);

    }

}
