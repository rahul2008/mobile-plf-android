package com.philips.platform.mya.csw.justintime;

import com.philips.platform.pif.chi.PostConsentCallback;

interface JustInTimeConsentContract {
    interface View {
        void showErrorDialog(String errorTitle, String errorMessage);

        void showProgressDialog();

        void hideProgressDialog();
    }

    interface Presenter {
        void onConsentGivenButtonClicked();

        void onConsentRejectedButtonClicked();

        void postConsent(boolean status, PostConsentCallback callback);
    }
}
