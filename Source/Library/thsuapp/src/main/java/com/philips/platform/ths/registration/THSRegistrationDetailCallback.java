package com.philips.platform.ths.registration;

import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;

public interface THSRegistrationDetailCallback<T, E extends THSSDKPasswordError> {
    void onResponse(T var1, E var2);
    void onFailure(Throwable var1);
}
