/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

import java.io.File;

/**
 * The interface AppUpdateListener.
 * <p>
 * Provides notifications on app updates.
 */
public interface AppUpdateListener {
    /**
     * On app update available.
     */
    void onAppUpdateAvailable();

    /**
     * On app update info downloaded.
     */
    void onAppUpdateInfoDownloaded();

    /**
     * Create file for app update download file.
     *
     * @return the file
     */
    File createFileForAppUpdateDownload();

    /**
     * On app update download start.
     *
     * @param percentage the percentage
     */
    void onAppUpdateDownloadStart(int percentage);

    /**
     * On app update download progress.
     *
     * @param percentage the percentage
     */
    void onAppUpdateDownloadProgress(int percentage);

    /**
     * On app update download complete.
     */
    void onAppUpdateDownloadComplete();

    /**
     * On app update download failed.
     */
    void onAppUpdateDownloadFailed();
}
