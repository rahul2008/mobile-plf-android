package com.philips.amwelluapp.login;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;

public interface PTHGetConsumerObjectCallBack {
    void onReceiveConsumerObject(Consumer consumer, SDKError sdkError);
    void onError(Throwable throwable);
}
