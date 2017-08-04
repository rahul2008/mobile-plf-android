/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

public interface THSInsuranceCallback {

    interface THSSDKCallBack <THSSubscription, THSSDKError>{
        void onResponse (THSSubscription tHSSubscription, THSSDKError tHSSDKError);
        void onFailure(Throwable throwable);

    }


}
