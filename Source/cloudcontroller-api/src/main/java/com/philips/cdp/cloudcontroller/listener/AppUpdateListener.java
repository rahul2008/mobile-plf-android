/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

import java.io.File;

public interface AppUpdateListener {
    void onAppUpdateAvailable();

    void onAppUpdateInfoDownloaded();

    File createFileForAppUpdateDownload();

    void onAppUpdateDownloadStart(int percentage);

    void onAppUpdateDownloadProgress(int percentage);

    void onAppUpdateDownloadComplete();

    void onAppUpdateDownloadFailed();
}
