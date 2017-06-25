package com.philips.amwelluapp.registration;

import com.philips.amwelluapp.sdkerrors.PTHSDKPasswordError;

public interface PTHRegistrationDetailCallback<T, E extends PTHSDKPasswordError> {
    void onResponse(T var1, E var2);
    void onFailure(Throwable var1);
}
