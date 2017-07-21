package com.philips.platform.ths.payment;

/**
 * Created by philips on 7/21/17.
 */

public interface THSPaymentCallback {

    interface THSSDKCallBack <THSPaymentMethod, THSSDKError>{
        void onResponse (THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError);
        void onFailure(Throwable throwable);

    }

}
