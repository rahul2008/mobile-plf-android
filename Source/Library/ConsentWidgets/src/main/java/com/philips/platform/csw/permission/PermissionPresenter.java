/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import java.util.List;

import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;

import android.support.annotation.NonNull;

public class PermissionPresenter implements GetConsentInteractor.Callback, ConsentToggleListener, CreateConsentInteractor.Callback {

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
        permissionInterface.onConsentGetFailed(error);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onConsentRetrieved(@NonNull List<ConsentView> consent) {
        permissionInterface.onConsentRetrieved(consent);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, boolean on) {
        createConsentInteractor.createConsentStatus(definition, this, on);
    }

    @Override
    public void onCreateConsentFailed(ConsentDefinition definition, int errorCode) {
        permissionInterface.onCreateConsentFailed(definition, errorCode);
    }

    @Override
    public void onCreateConsentSuccess(ConsentDefinition definition, Consent consent, int code) {
        permissionInterface.onCreateConsentSuccess(definition, consent, code);
    }
}
