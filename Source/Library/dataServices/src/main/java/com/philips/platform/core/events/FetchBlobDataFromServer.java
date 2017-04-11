package com.philips.platform.core.events;

import com.philips.platform.core.listeners.BlobDownloadRequestListener;
import com.philips.platform.core.listeners.BlobUploadRequestListener;
import com.philips.platform.datasync.blob.BlobMetaData;

public class FetchBlobDataFromServer extends Event{

    private BlobDownloadRequestListener blobDownloadRequestListener;

    BlobMetaData blobMetaData;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    private String itemId;

    public BlobDownloadRequestListener getBlobDownloadRequestListener() {
        return blobDownloadRequestListener;
    }



    public void setBlobDownloadRequestListener(BlobDownloadRequestListener blobDownloadRequestListener) {
        this.blobDownloadRequestListener = blobDownloadRequestListener;
    }

    public FetchBlobDataFromServer(String itemId, BlobDownloadRequestListener blobDownloadRequestListener){
        this.itemId = itemId;
        this.blobDownloadRequestListener = blobDownloadRequestListener;
    }

    public FetchBlobDataFromServer(BlobMetaData blobMetaData, BlobDownloadRequestListener blobDownloadRequestListener){
        this.blobMetaData = blobMetaData;
        this.blobDownloadRequestListener = blobDownloadRequestListener;
    }

    public BlobMetaData getBlobMetaData() {
        return blobMetaData;
    }
}
