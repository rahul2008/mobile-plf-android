package com.philips.amwelluapp.login;

import com.philips.amwelluapp.sdkerrors.PTHSDKError;

public interface PTHLoginCallBack<T, E extends PTHSDKError>{
    void onLoginResponse(T var1, E var2);
    void onLoginFailure(Throwable var1);
}
