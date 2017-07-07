package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;

public interface THSUpdateConditionsCallback<Void,PTHSDKError> {
    public void onUpdateConditonResponse(Void aVoid, PTHSDKError sdkError);
    public void onUpdateConditionFailure(Throwable throwable);
}
