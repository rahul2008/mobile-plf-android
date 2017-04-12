package com.philips.platform.datasync.blob;

import com.philips.platform.core.listeners.BlobUploadRequestListener;

import java.io.File;

public class BlobData {

    private BlobUploadRequestListener blobRequestListener;
    private String type;
    private File file;

    public void setBlobRequestListener(BlobUploadRequestListener blobRequestListener) {
        this.blobRequestListener = blobRequestListener;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    public BlobUploadRequestListener getBlobRequestListener() {
        return blobRequestListener;
    }
}
