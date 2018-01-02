/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.catk.ConsentInteractor;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PermissionPresenter implements ConsentInteractor.ConsentListCallback, ConsentToggleListener,
        ConsentInteractor.CreateConsentCallback {

    @NonNull
    private final PermissionInterface permissionInterface;
    @NonNull
    private final ConsentInteractor consentInteractor;
    @NonNull
    private final PermissionAdapter adapter;

    private static final String CONSENT_TYPE_CLICKSTREAM = "clickstream";

    @Inject
    PermissionPresenter(
            @NonNull final PermissionInterface permissionInterface, @NonNull final ConsentInteractor consentInteractor, @NonNull final PermissionAdapter adapter) {
        this.permissionInterface = permissionInterface;
        this.consentInteractor = consentInteractor;
        this.adapter = adapter;
        this.adapter.setConsentToggleListener(this);
    }

    @NonNull
    PermissionAdapter getAdapter() {
        return adapter;
    }

    void getConsentStatus() {
        permissionInterface.showProgressDialog();
        consentInteractor.fetchLatestConsents(this);
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, boolean consentGiven) {
        consentInteractor.createConsentStatus(definition, this, consentGiven);
        permissionInterface.showProgressDialog();
    }

    @Override
    public void onGetConsentRetrieved(@NonNull List<Consent> consents) {
        List<ConsentView> consentViews = adapter.getConsentViews();
        Map<String, Consent> consentMap = new HashMap<>();
        for (Consent consent : consents) {
            consentMap.put(consent.getType(), consent);
        }
        for (ConsentView consentView : consentViews) {
            consentView.storeConsent(consentMap.get(consentView.getType()));
            if (consentView.getType().equals(CONSENT_TYPE_CLICKSTREAM)) {
                updateClickStream(consentView.isChecked());
            }
        }
        adapter.onGetConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onGetConsentFailed(ConsentNetworkError error) {
        adapter.onGetConsentFailed(error);
        permissionInterface.hideProgressDialog();
        permissionInterface.showErrorDialog(error);
    }

    @Override
    public void onCreateConsentFailed(ConsentDefinition definition, ConsentNetworkError error) {
        adapter.onCreateConsentFailed(definition, error);
        permissionInterface.showErrorDialog(error);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onCreateConsentSuccess(Consent consent) {
        if (consent != null && consent.getType().equals(CONSENT_TYPE_CLICKSTREAM)) {
            updateClickStream(consent.getStatus().name().equals(ConsentStatus.active.name()));
        }
        adapter.onCreateConsentSuccess(consent);
        permissionInterface.hideProgressDialog();
    }

    private void updateClickStream(boolean isActive) {
        if (isActive) {
            CswInterface.getCswComponent().getAppTaggingInterface().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        } else {
            CswInterface.getCswComponent().getAppTaggingInterface().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
        }
    }
}
