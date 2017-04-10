package com.philips.platform.core.listeners;

import com.philips.platform.datasync.blob.UcoreBlobMetaData;
import com.philips.platform.datasync.blob.UcoreBlobResponse;

import java.io.File;

public interface BlobRequestListener {
    void onBlobRequestSuccess(String itemID);
    void onBlobRequestFailure(Exception exception);
    void onFetchMetaDataSuccess(UcoreBlobMetaData uCoreFetchMetaData);
}
