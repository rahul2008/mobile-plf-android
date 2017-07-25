package com.philips.platform.baseapp.screens.termsandconditions;

/**
 * Created by philips on 25/07/17.
 */

public class TermsAndConditionsContract {
    interface View{
        void updateUiOnUrlLoaded(String url);

        void onUrlLoadError(String errorMessage);
    }

    interface Action{
        void loadTermsAndConditionsUrl();
    }

}
