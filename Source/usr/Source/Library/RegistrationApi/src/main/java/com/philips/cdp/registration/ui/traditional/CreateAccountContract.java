package com.philips.cdp.registration.ui.traditional;

/**
 * Created by philips on 22/06/17.
 */

public interface CreateAccountContract {
    void handleUiState();
    void updateUiStatus();

    void launchMarketingAccountFragment();

    void launchMobileVerifyCodeFragment();

    void launchAccountActivateFragment();

    void completeRegistration();

    void emailError(int errorDescID);

    void emailError(String errorDesc);

    void genericError(String errorDesc);

    void genericError(int errorDescID);

    void serverConnectionError(int errorDesc);

    String getEmail();

    long getTrackCreateAccountTime();

    void setTrackCreateAccountTime(long trackCreateAccountTime);

    void tractCreateActionStatus(String state, String key, String value);

    void trackCheckMarketing();

    void scrollViewAutomaticallyToEmail();

    void scrollViewAutomaticallyToError();

    void hideSpinner();

    void storeEMail();

    void emailAlreadyUsed();

    void registrtionFail();

}
