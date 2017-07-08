package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;


public interface THSSDKCallback<T, E extends SDKError> {
    void onResponse(T var1, E var2);

    void onFailure(Throwable var1);
}