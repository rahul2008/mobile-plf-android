package com.philips.platform.ths.login;

import com.philips.platform.ths.sdkerrors.THSSDKError;

public interface THSLoginCallBack<T, E extends THSSDKError>{
    void onLoginResponse(T var1, E var2);
    void onLoginFailure(Throwable var1);
}
