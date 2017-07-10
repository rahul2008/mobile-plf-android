package com.philips.platform.ths.practice;

import com.americanwell.sdk.entity.SDKError;

public interface THSPracticesListCallback {
    //TODO: Review Comment - Spoorti - wrap SDKError to PTHError
    void onPracticesListReceived(THSPractice practices, SDKError sdkError);
    void onPracticesListFetchError(Throwable throwable);
}
