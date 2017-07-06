package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;


public interface PTHSDKCallback<T, E extends SDKError> {
    void onResponse(T var1, E var2);

    void onFailure(Throwable var1);
}