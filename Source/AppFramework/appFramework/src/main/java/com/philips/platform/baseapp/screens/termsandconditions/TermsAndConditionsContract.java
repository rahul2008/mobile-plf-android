/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

/**
 * Created by philips on 25/07/17.
 */

public class TermsAndConditionsContract {
    interface View {
        void updateUiOnUrlLoaded(String url);

        void onUrlLoadError(String errorMessage);
    }

    interface Action {
        void loadTermsAndConditionsUrl(String state);
    }

}
