package com.philips.cdp.registration.ui.social;


import android.app.Activity;

interface MergeSocialToSocialAccountContract {

    void connectionStatus(boolean isOnline);

    void mergeStatus(boolean isOnline);

    void mergeSuccess();

    void mergeFailure(String errorDescription);

    void mergeFailureIgnored();

    Activity getActivityContext();
}
