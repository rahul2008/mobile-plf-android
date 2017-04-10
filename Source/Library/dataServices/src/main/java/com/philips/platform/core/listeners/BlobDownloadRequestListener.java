package com.philips.platform.core.listeners;

import java.io.File;
import java.io.InputStream;

public interface BlobDownloadRequestListener {
    void onBlobDownloadRequestSuccess(InputStream file);
    void onBlobRequestFailure(Exception exception);
}
