package com.philips.amwelluapp.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;

/**
 * Created by philips on 6/19/17.
 */

public interface PTHGetConsumerObjectCallBack {
    void onReceiveConsumerObject(Consumer consumer, SDKError sdkError);
    void onError(Throwable throwable);
}
