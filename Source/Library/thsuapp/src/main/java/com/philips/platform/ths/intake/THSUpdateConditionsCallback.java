package com.philips.platform.ths.intake;

public interface THSUpdateConditionsCallback<Void,PTHSDKError> {
    public void onUpdateConditonResponse(Void aVoid, PTHSDKError sdkError);
    public void onUpdateConditionFailure(Throwable throwable);
}
