package com.philips.amwelluapp.login;

import com.americanwell.sdk.entity.SDKError;

public interface PTHLoginCallBack<T, E extends SDKError>{
    void onLoginResponse(T var1, E var2);
    void onLoginFailure(Throwable var1);
}
