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
    private List<ConsentView> consentViews;
    public static final int version = 0;

    PermissionPresenter(
            PermissionInterface permissionInterface, GetConsentInteractor getConsentInteractor, CreateConsentInteractor createConsentInteractor, PermissionAdapter adapter) {
        this.permissionInterface = permissionInterface;
        this.getConsentInteractor = getConsentInteractor;
        this.createConsentInteractor = createConsentInteractor;
        this.consentViews = adapter.getConsentViews();
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
    public void onConsentRetrieved(@NonNull List<RequiredConsent> consents) {
        Map<String, RequiredConsent> consentMap = new HashMap<>();
        for (RequiredConsent consent : consents) {
            consentMap.put(consent.getType(), consent);
        }
        for (ConsentView consentView : consentViews) {
            consentView.storeConsent(consentMap.get(consentView.getType()));
        }
        adapter.onConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onConsentFailed(int error) {
        adapter.onConsentGetFailed(error);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, boolean consentGiven) {
        createConsentInteractor.createConsentStatus(definition, this, consentGiven);
    }

    @Override
    public void onCreateConsentFailed(ConsentDefinition definition, int errorCode) {
        adapter.onCreateConsentFailed(definition, errorCode);
    }

    @Override
    public void onCreateConsentSuccess(RequiredConsent consent, int code) {
        adapter.onCreateConsentSuccess(consent, code);
    }
}
