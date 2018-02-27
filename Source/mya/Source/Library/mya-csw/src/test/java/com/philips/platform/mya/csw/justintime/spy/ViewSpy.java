package com.philips.platform.mya.csw.justintime.spy;

import com.philips.platform.mya.csw.justintime.JustInTimeConsentContract;

public class ViewSpy implements JustInTimeConsentContract.View {
    public JustInTimeConsentContract.Presenter presenter;
    public int errorTileId_showErrorDialogForCode;
    public int errorCode_showErrorDialogForCode;
    public int errorTitleId_showErrorDialog;
    public int errorMessageId_showErrorDialog;
    public boolean progressDialogShown;
    public boolean progressDialogHidden;

    @Override
    public void setPresenter(JustInTimeConsentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String errorTitle, String errorMessage) {

    }

    @Override
    public void showErrorDialog(int errorTitleId, int errorMessageId) {
        this.errorTitleId_showErrorDialog = errorTitleId;
        this.errorMessageId_showErrorDialog = errorMessageId;
    }

    @Override
    public void showErrorDialogForCode(int errorTitleId, int errorCode) {
        this.errorTileId_showErrorDialogForCode = errorTitleId;
        this.errorCode_showErrorDialogForCode = errorCode;
    }

    @Override
    public void showProgressDialog() {
        this.progressDialogShown = true;
    }

    @Override
    public void hideProgressDialog() {
        this.progressDialogHidden = true;
    }
}
