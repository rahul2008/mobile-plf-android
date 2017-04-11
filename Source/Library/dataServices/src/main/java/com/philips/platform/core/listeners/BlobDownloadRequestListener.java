package com.philips.platform.core.listeners;

import java.io.File;
import java.io.InputStream;

public interface BlobDownloadRequestListener {
    void onBlobDownloadRequestSuccess(InputStream file,String mime);
    void onBlobRequestFailure(Exception exception);
}
