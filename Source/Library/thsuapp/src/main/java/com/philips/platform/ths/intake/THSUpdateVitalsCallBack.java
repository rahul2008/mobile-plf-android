package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

public interface THSUpdateVitalsCallBack {

    public void onUpdateVitalsValidationFailure(Map<String, ValidationReason> map);
    public void onUpdateVitalsResponse(SDKError sdkError);
    public void onUpdateVitalsFailure(Throwable throwable);
}