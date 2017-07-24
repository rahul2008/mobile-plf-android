package com.philips.platform.ths.intake.selectimage;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.DocumentRecord;

import java.util.List;

public interface THSDocumentRecordCallback {

    void onDocumentRecordFetchSuccess(List<DocumentRecord> documentRecordList, SDKError sdkError);
    void onError(Throwable throwable);
}
