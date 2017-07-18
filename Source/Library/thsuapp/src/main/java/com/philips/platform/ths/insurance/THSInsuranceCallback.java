package com.philips.platform.ths.insurance;

/**
 * Created by philips on 7/11/17.
 */

public interface THSInsuranceCallback {

    interface THSSDKCallBack <THSSubscription, THSSDKError>{
        void onResponse (THSSubscription tHSSubscription, THSSDKError tHSSDKError);
        void onFailure(Throwable throwable);

    }


}
