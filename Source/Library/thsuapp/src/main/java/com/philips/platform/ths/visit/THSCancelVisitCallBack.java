package com.philips.platform.ths.visit;

import com.americanwell.sdk.entity.SDKError;

/**
 * Created by philips on 7/27/17.
 */

public interface  THSCancelVisitCallBack   {
    public interface SDKCallback<Void, SDKError> {
        void onResponse(Void aVoid, SDKError aSDKError);

        void onFailure(Throwable var1);
    }
}
