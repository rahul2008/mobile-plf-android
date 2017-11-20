package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import java.util.List;


public class PermissionPresenter implements GetConsentInteractor.Callback {

    private PermissionInterface permissionInterface;
    private GetConsentInteractor getConsentInteractor;
    public static final int version = 0;

    PermissionPresenter(
            PermissionInterface permissionInterface, GetConsentInteractor getConsentInteractor) {
        this.permissionInterface = permissionInterface;
        this.getConsentInteractor = getConsentInteractor;
    }


    void getConsentStatus() {
        permissionInterface.showProgressDialog();
        getConsentInteractor.getConsents(this, permissionInterface);
    }

    @Override
    public void onConsentFailed(int error) {

    }

    @Override
    public void onConsentRetrieved(@NonNull List<ConsentView> consent) {
        permissionInterface.onConsentRetrieved(consent);
    }
}
