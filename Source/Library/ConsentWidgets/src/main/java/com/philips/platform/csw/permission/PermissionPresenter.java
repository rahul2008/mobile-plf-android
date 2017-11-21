/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.catk.model.ConsentDefinition;

import java.util.List;

public class PermissionPresenter implements GetConsentInteractor.Callback, ConsentToggleListener {

    private PermissionInterface permissionInterface;
    private GetConsentInteractor getConsentInteractor;
    private CreateConsentInteractor createConsentInteractor;
    public static final int version = 0;

    PermissionPresenter(
            PermissionInterface permissionInterface, GetConsentInteractor getConsentInteractor, CreateConsentInteractor createConsentInteractor) {
        this.permissionInterface = permissionInterface;
        this.getConsentInteractor = getConsentInteractor;
        this.createConsentInteractor = createConsentInteractor;
    }

    void getConsentStatus() {
        permissionInterface.showProgressDialog();
        getConsentInteractor.getConsents(this);
    }

    @Override
    public void onConsentFailed(int error) {
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onConsentRetrieved(@NonNull List<ConsentView> consent) {
        permissionInterface.onConsentRetrieved(consent);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, boolean on) {
        createConsentInteractor.createConsentStatus(definition, on);
    }
}
