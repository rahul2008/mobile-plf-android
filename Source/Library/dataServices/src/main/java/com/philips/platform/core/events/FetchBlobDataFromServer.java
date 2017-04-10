package com.philips.platform.core.events;

import com.philips.platform.core.listeners.BlobDownloadRequestListener;
import com.philips.platform.core.listeners.BlobUploadRequestListener;

public class FetchBlobDataFromServer extends Event{

    private BlobDownloadRequestListener blobDownloadRequestListener;

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
}
