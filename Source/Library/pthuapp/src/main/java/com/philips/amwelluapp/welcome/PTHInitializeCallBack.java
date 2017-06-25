package com.philips.amwelluapp.welcome;

import com.philips.amwelluapp.sdkerrors.PTHSDKError;

public interface PTHInitializeCallBack<T, E extends PTHSDKError>{
    void onInitializationResponse(T var1, E var2);
    void onInitializationFailure(Throwable var1);
}
