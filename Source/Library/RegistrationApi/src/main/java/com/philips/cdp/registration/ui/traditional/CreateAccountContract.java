package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;

/**
 * Created by philips on 22/06/17.
 */

public interface CreateAccountContract {
    void handleUiState();
    void updateUiStatus();

    void launchMarketingAccountFragment();

    void launchMobileVerifyCodeFragment();

    void launchAccountActivateFragment();

    void launchWelcomeFragment();

    void emailError(int errorDescID);

    void emailError(String errorDesc);

    String getEmail();

    long getTrackCreateAccountTime();

    void setTrackCreateAccountTime(long trackCreateAccountTime);

    void tractCreateActionStatus(String state, String key, String value);

    void trackCheckMarketing();

    void scrollViewAutomaticallyToEmail();

    void scrollViewAutomaticallyToError();

    void hideSpinner();

    void storePref();

    void emailAlreadyUsed();

    void regFail();

}
