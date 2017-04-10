package com.philips.platform.datasync.blob;

import com.philips.platform.core.listeners.BlobRequestListener;

import java.io.File;

/**
 * Created by philips on 4/6/17.
 */

public class BlobData {

    BlobRequestListener blobRequestListener;
    String type;
    File file;

    public BlobRequestListener getBlobRequestListener() {
        return blobRequestListener;
    }

    public void setBlobRequestListener(BlobRequestListener blobRequestListener) {
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
}
