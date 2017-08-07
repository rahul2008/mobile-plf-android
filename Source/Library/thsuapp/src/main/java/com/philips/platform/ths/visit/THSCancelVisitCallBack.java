/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.visit;

/**
 * Created by philips on 7/27/17.
 */

public interface  THSCancelVisitCallBack   {
    interface SDKCallback<Void, SDKError> {
        void onResponse(Void aVoid, SDKError aSDKError);

        void onFailure(Throwable var1);
    }
}
