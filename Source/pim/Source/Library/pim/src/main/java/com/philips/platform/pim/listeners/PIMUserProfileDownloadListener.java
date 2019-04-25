package com.philips.platform.pim.listeners;

//TODO:Shashi, Change arguments of onUserProfileDownloadFailed to enum, when PIMErrorCode are defined.
//TODO:Shashi, Pass correct error code
public interface PIMUserProfileDownloadListener {
    void onUserProfileDownloadSuccess();
    void onUserProfileDownloadFailed(int errorCode);
}
