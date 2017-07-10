package com.philips.platform.ths.welcome;

import com.philips.platform.ths.sdkerrors.THSSDKError;

public interface THSInitializeCallBack<T, E extends THSSDKError>{
    void onInitializationResponse(T var1, E var2);
    void onInitializationFailure(Throwable var1);
}
