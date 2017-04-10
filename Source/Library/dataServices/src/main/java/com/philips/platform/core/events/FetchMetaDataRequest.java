package com.philips.platform.core.events;

import com.philips.platform.core.listeners.BlobRequestListener;

import java.io.File;

/**
 * Created by philips on 4/5/17.
 */

public class FetchMetaDataRequest extends Event{

    private String blobID;
    private BlobRequestListener blobRequestListener;


    public FetchMetaDataRequest(String blobID,BlobRequestListener blobRequestListener){
        this.blobID=blobID;
        this.blobRequestListener = blobRequestListener;
    }

    public String getBlobID() {
        return blobID;
    }

    public BlobRequestListener getBlobRequestListener() {
        return blobRequestListener;
    }
}
