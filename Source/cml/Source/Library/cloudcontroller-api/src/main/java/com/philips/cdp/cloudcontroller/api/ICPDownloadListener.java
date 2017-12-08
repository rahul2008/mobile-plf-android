/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api;

/**
 * Listener interface to notify objects about completed downloads from ICP.
 * @publicApi
 */
public interface ICPDownloadListener {
    /**
     * Indicates that downloading data from ICP has been completed.
     * @param status int Code to indicate statusses.
     * @param downloadedData String The downloaded data as received from ICP.
     */
    void onDataDownload(int status, String downloadedData);
}
