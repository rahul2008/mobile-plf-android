/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

public interface THSInsuranceCallback {

    interface THSgetInsuranceCallBack<THSSubscription, THSSDKError> {
        void onGetInsuranceResponse(THSSubscription tHSSubscription, THSSDKError tHSSDKError);

        void onGetInsuranceFailure(Throwable throwable);

    }


}