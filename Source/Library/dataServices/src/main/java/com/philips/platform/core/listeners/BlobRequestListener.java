package com.philips.platform.core.listeners;

import com.philips.platform.datasync.blob.BlobMetaData;

public interface BlobRequestListener {
    void onBlobRequestSuccess(String itemID);
    void onBlobRequestFailure(Exception exception);
    void onFetchMetaDataSuccess(BlobMetaData uCoreFetchMetaData);
}
