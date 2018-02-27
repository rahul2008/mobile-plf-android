package com.philips.platform.mya.csw.justintime;

public interface JustInTimeConsentContract {
    interface View {
        void setPresenter(Presenter presenter);

        void showErrorDialog(String errorTitle, String errorMessage);

        void showErrorDialog(int errorTitleId, int errorMessageId);

        void showErrorDialogForCode(int errorTitleId, int errorCode);

        void showProgressDialog();

        void hideProgressDialog();
    }

    interface Presenter {
        void onConsentGivenButtonClicked();

        void onConsentRejectedButtonClicked();
    }
}
