/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.datasync.blob.BlobMetaData;


public class SavingBlobMetaDataRequest extends Event {

   private final BlobMetaData blobMetaData;

    public SavingBlobMetaDataRequest(BlobMetaData blobMetaData) {
        this.blobMetaData = blobMetaData;
    }

    public BlobMetaData getBlobMetaData() {
        return blobMetaData;
    }
}
