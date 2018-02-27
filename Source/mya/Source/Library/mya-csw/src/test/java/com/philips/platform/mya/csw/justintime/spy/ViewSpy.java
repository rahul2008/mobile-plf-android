package com.philips.platform.mya.csw.justintime.spy;

import com.philips.platform.mya.csw.justintime.JustInTimeConsentContract;

public class ViewSpy implements JustInTimeConsentContract.View {
    public JustInTimeConsentContract.Presenter presenter;

    @Override
    public void setPresenter(JustInTimeConsentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String errorTitle, String errorMessage) {

    }

    @Override
    public void showErrorDialog(int errorTitleId, int errorMessageId) {

    }

    @Override
    public void showErrorDialogForCode(int errorTitleId, int errorCode) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }
}
