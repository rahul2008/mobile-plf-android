package com.philips.cdp.registration.ui.traditional.mobile;

public interface AddSecureEmailContract {


    void showWelcomeScreen();

    void showInvalidEmailError();

    void onAddRecoveryEmailSuccess();

    void onAddRecoveryEmailFailure(String error);

    void enableButtons();

    void disableButtons();

    void showErrorMsg();

    void hideErrorMsg();
}

