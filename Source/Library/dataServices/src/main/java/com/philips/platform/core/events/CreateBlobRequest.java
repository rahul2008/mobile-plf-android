package com.philips.platform.core.events;

import com.philips.platform.core.listeners.BlobRequestListener;

import java.io.File;
import java.io.InputStream;

/**
 * Created by philips on 4/5/17.
 */

public class CreateBlobRequest extends Event{
    private File inputStream;
    private String type;
    private BlobRequestListener blobRequestListener;

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

    public CreateBlobRequest(File file, String type, BlobRequestListener blobRequestListener){
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
