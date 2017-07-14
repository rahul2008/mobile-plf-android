package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.practice.OnDemandSpecialty;
import com.philips.platform.ths.sdkerrors.THSSDKError;

import java.util.List;

public interface THSOnDemandSpecialtyCallback<List,THSSDKError> {
    public void onResponse(List onDemandSpecialties, THSSDKError sdkError);
    public void onFailure(Throwable throwable);
}
