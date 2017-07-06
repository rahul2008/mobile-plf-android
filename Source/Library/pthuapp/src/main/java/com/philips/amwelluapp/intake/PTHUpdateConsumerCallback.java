package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.sdkerrors.PTHSDKPasswordError;

import java.util.Map;

public interface PTHUpdateConsumerCallback <PTHConsumer, PTHSDKPasswordError> {
    void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1);
    void onUpdateConsumerResponse(PTHConsumer consumer, PTHSDKPasswordError sdkPasswordError);
    void onUpdateConsumerFailure(Throwable var1);
}