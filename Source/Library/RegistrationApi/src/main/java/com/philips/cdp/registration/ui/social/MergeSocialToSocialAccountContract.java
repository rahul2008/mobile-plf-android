package com.philips.cdp.registration.ui.social;


import android.app.*;

interface MergeSocialToSocialAccountContract {
    void connectionStatus(boolean isOnline);

    void mergeStatus(boolean isOnline);

    void mergeSuccess();

    void mergeFailuer(String errorDescription);

    void mergeFailuerIgnored();

    Activity getActivityContext();
}
