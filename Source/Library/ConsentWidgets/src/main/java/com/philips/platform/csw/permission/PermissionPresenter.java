/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.catk.CreateConsentInteractor;
import com.philips.platform.catk.GetConsentInteractor;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionPresenter implements GetConsentInteractor.Callback, ConsentToggleListener, CreateConsentInteractor.Callback {

    private PermissionInterface permissionInterface;
    private GetConsentInteractor getConsentInteractor;
    private CreateConsentInteractor createConsentInteractor;
    private PermissionAdapter adapter;
    public static final int version = 0;

    PermissionPresenter(
            PermissionInterface permissionInterface, GetConsentInteractor getConsentInteractor, CreateConsentInteractor createConsentInteractor, PermissionAdapter adapter) {
        this.permissionInterface = permissionInterface;
        this.getConsentInteractor = getConsentInteractor;
        this.createConsentInteractor = createConsentInteractor;
        this.adapter = adapter;
        this.adapter.setConsentToggleListener(this);
    }

    PermissionAdapter getAdapter() {
        return adapter;
    }

    void getConsentStatus() {
        permissionInterface.showProgressDialog();
        getConsentInteractor.fetchLatestConsents(this);
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, boolean consentGiven) {
        createConsentInteractor.createConsentStatus(definition, this, consentGiven);
    }

    @Override
    public void onGetConsentRetrieved(@NonNull List<RequiredConsent> consents) {
        List<ConsentView> consentViews = adapter.getConsentViews();
        Map<String, RequiredConsent> consentMap = new HashMap<>();
        for (RequiredConsent consent : consents) {
            consentMap.put(consent.getType(), consent);
        }
        for (ConsentView consentView : consentViews) {
            consentView.storeConsent(consentMap.get(consentView.getType()));
        }
        adapter.onGetConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onGetConsentFailed(ConsentNetworkError error) {
        adapter.onGetConsentFailed(error);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onCreateConsentFailed(ConsentDefinition definition, ConsentNetworkError error) {
        adapter.onCreateConsentFailed(definition, error);
        permissionInterface.showErrorDialog(error);
    }

    @Override
    public void onCreateConsentSuccess(RequiredConsent consent) {
        adapter.onCreateConsentSuccess(consent);
    }
}
