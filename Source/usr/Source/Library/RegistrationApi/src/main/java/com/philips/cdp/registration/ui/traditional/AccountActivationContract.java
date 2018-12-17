package com.philips.cdp.registration.ui.traditional;

/**
 * Created by philips on 22/06/17.
 */

public interface AccountActivationContract {

   void handleUiState(boolean isNetworkAvailble);
    void updateActivationUIState();
    void hideActivateSpinner();
    void activateButtonEnable(boolean enable);
    void verificationError(String errorMsg);

}
