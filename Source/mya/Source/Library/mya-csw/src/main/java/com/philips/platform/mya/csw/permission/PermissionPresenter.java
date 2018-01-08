/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PermissionPresenter implements CheckConsentsCallback, ConsentToggleListener, PostConsentCallback {

    @NonNull
    private final PermissionInterface permissionInterface;
    @NonNull
    private final List<ConsentConfiguration> configurationList;
    @NonNull
    private final PermissionAdapter adapter;

    private static final String CONSENT_TYPE_CLICKSTREAM = "clickstream";

    @Inject
    PermissionPresenter(
            @NonNull final PermissionInterface permissionInterface, @NonNull final List<ConsentConfiguration> configurationList, @NonNull final PermissionAdapter adapter) {
        this.permissionInterface = permissionInterface;
        this.configurationList = configurationList;
        this.adapter = adapter;
        this.adapter.setConsentToggleListener(this);
    }

    @NonNull
    PermissionAdapter getAdapter() {
        return adapter;
    }

    void getConsentStatus() {
        if (!configurationList.isEmpty()) {
            permissionInterface.showProgressDialog();
            for (ConsentConfiguration configuration : configurationList) {
                ConsentHandlerInterface handlerInterface = configuration.getHandlerInterface();
                if (handlerInterface != null) {
                    handlerInterface.checkConsents(this);
                }
            }
        }
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, ConsentHandlerInterface handler, boolean consentGiven) {
        handler.post(definition, consentGiven, this);
        permissionInterface.showProgressDialog();
    }

    @Override
    public void onGetConsentsSuccess(@NonNull List<Consent> consents) {
        List<ConsentView> consentViews = adapter.getConsentViews();
        Map<String, Consent> consentMap = new HashMap<>();
        for (Consent consent : consents) {
            consentMap.put(consent.getType(), consent);
        }
        for (ConsentView consentView : consentViews) {
            Consent consent = consentMap.get(consentView.getType());
            if (consent != null) {
                consentView.storeConsent(consent);
            }
        }
        adapter.onGetConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onGetConsentsFailed(ConsentError error) {
        adapter.onGetConsentFailed(error);
        permissionInterface.hideProgressDialog();
        permissionInterface.showErrorDialog(error);
    }

    @Override
    public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
        adapter.onCreateConsentFailed(definition, error);
        permissionInterface.showErrorDialog(error);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onPostConsentSuccess(Consent consent) {
        if (consent != null && consent.getType().equals(CONSENT_TYPE_CLICKSTREAM)) {
            updateClickStream(consent.getStatus());
        }
        adapter.onCreateConsentSuccess(consent);
        permissionInterface.hideProgressDialog();
    }

    private void updateClickStream(ConsentStatus consentStatus) {
        if (consentStatus.name().equals(ConsentStatus.active.name())) {
            CswInterface.getCswComponent().getAppTaggingInterface().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        } else {
            CswInterface.getCswComponent().getAppTaggingInterface().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
        }
    }
}