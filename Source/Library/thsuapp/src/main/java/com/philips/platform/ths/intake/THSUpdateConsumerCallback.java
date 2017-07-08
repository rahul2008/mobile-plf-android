package com.philips.platform.ths.intake;

import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

public interface THSUpdateConsumerCallback<PTHConsumer, PTHSDKPasswordError> {
    void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1);
    void onUpdateConsumerResponse(PTHConsumer consumer, PTHSDKPasswordError sdkPasswordError);
    void onUpdateConsumerFailure(Throwable var1);
}