package com.philips.platform.ths.intake.selectimage;


import com.americanwell.sdk.entity.SDKError;

public interface THSDeleteDocumentCallback {

    void onDeleteSuccess(SDKError sdkError);
    void onError(Throwable throwable);
}
