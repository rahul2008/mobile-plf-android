package com.philips.platform.ths.registration;

import com.philips.platform.ths.sdkerrors.THSSDKError;

public interface THSCheckConsumerExistsCallback<Boolean, THSSDkError> {
    void onResponse(Boolean aBoolean, THSSDKError sdkError);
    void onFailure(Throwable throwable);
}
