package com.philips.cdp.registration.ui.traditional.mobile;

public interface AddSecureEmailContract {


    void registrationComplete();

    void showInvalidEmailError();

    void onAddRecoveryEmailSuccess();

    void onAddRecoveryEmailFailure(String error);

    void enableButtons();

    void disableButtons();

    void showNetworkUnavailableError();

    void hideError();

    void showProgress();

    void hideProgress();

    void storePreference(String emailOrMobileNumber);
}

