package com.philips.platform.ths.appointment;

import com.americanwell.sdk.entity.SDKError;
import com.philips.platform.ths.sdkerrors.THSSDKError;

import java.util.Date;
import java.util.List;

public interface THSAvailableProviderCallback<List,THSSDKError> {
    public void onResponse(List dates, THSSDKError sdkError);
    public void onFailure(Throwable throwable);
}
