package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

/**
 * Callback interface for user profile download
 */
public interface PIMUserProfileDownloadListener {
    void onUserProfileDownloadSuccess();
    void onUserProfileDownloadFailed(Error error);
}
