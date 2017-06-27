package com.philips.amwelluapp.login;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;

//TODO: Review Comment - Spoorti - Use generics for type safety. Eg: PTHGetConsumerObjectCallBack<T, E extends PTHSDKError>
//TODO: Review Comment - Spoorti - Wrap the SDk objects to PTH Objects
public interface PTHGetConsumerObjectCallBack {
    void onReceiveConsumerObject(Consumer consumer, SDKError sdkError);
    void onError(Throwable throwable);
}
