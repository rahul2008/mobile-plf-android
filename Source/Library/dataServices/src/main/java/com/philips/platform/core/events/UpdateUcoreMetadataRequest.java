/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.datasync.blob.BlobMetaData;


public class UpdateUcoreMetadataRequest extends Event {

    private final BlobMetaData blobMetaData;
    private final BlobRequestListener blobRequestListener;

    public UpdateUcoreMetadataRequest(BlobMetaData blobMetaData, BlobRequestListener blobRequestListener) {
        this.blobMetaData = blobMetaData;
        this.blobRequestListener = blobRequestListener;
    }

    public BlobMetaData getBlobMetaData() {
        return blobMetaData;
    }

    public BlobRequestListener getBlobRequestListener() {
        return blobRequestListener;
    }
}
