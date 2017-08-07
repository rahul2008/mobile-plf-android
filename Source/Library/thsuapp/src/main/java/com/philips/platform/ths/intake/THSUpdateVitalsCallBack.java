package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

public interface THSUpdateVitalsCallBack {

    void onUpdateVitalsValidationFailure(Map<String, ValidationReason> map);
    void onUpdateVitalsResponse(SDKError sdkError);
    void onUpdateVitalsFailure(Throwable throwable);
}