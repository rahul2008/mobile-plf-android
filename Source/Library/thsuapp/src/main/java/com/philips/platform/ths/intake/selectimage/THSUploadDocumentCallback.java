package com.philips.platform.ths.intake.selectimage;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.DocumentRecord;
import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

public interface THSUploadDocumentCallback {

    void onUploadValidationFailure(Map<String, ValidationReason> map);
    void onUploadDocumentSuccess(DocumentRecord documentRecord, SDKError sdkError);
    void onError(Throwable throwable);
}
