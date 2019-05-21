package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface PIMUserProfileDownloadListener {
    void onUserProfileDownloadSuccess();
    void onUserProfileDownloadFailed(Error error);
}
