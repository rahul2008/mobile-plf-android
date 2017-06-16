package com.philips.amwelluapp.welcome;

import com.americanwell.sdk.entity.SDKError;

public interface PTHInitializeCallBack<T, E extends SDKError>{
    void onInitializationResponse(T var1, E var2);
    void onInitializationFailure(Throwable var1);
}
