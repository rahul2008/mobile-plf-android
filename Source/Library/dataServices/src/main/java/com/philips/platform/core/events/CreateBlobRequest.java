package com.philips.platform.core.events;

import com.philips.platform.core.listeners.BlobUploadRequestListener;

import java.io.File;

/**
 * Created by philips on 4/5/17.
 */

public class CreateBlobRequest extends Event{
    private File inputStream;
    private String type;
    private BlobUploadRequestListener blobRequestListener;

    public BlobUploadRequestListener getBlobRequestListener() {
        return blobRequestListener;
    }

    public void setBlobRequestListener(BlobUploadRequestListener blobRequestListener) {
        this.blobRequestListener = blobRequestListener;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CreateBlobRequest(File file, String type, BlobUploadRequestListener blobRequestListener){
        this.inputStream = file;
        this.type = type;
        this.blobRequestListener = blobRequestListener;
    }

    public File getInputStream() {
        return inputStream;
    }

    public void setInputStream(File inputStream) {
        this.inputStream = inputStream;
    }
}
