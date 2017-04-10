package com.philips.platform.core.listeners;

import com.philips.platform.datasync.blob.UcoreBlobMetaData;

public interface BlodMetaDataRequestListener {
    void onFetchMetaDataSuccess(UcoreBlobMetaData uCoreFetchMetaData);
    void onBlobRequestFailure(Exception exception);
}
