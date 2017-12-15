/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.consenthandlerinterface.ConsentError;
import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.ConsentListCallback;
import com.philips.platform.consenthandlerinterface.CreateConsentCallback;
import com.philips.platform.consenthandlerinterface.datamodel.Consent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PermissionPresenter implements ConsentListCallback, ConsentToggleListener, CreateConsentCallback {

    @NonNull
    private final PermissionInterface permissionInterface;
    @NonNull
    private final ConsentHandlerInterface consentHandlerInterface;
    @NonNull
    private final PermissionAdapter adapter;

    @Inject
    PermissionPresenter(
            @NonNull final PermissionInterface permissionInterface, @NonNull final ConsentHandlerInterface consentInteractor, @NonNull final PermissionAdapter adapter) {
        this.permissionInterface = permissionInterface;
        this.consentHandlerInterface = consentInteractor;
        this.adapter = adapter;
        this.adapter.setConsentToggleListener(this);
    }

    @NonNull
    PermissionAdapter getAdapter() {
        return adapter;
    }

    void getConsentStatus() {
        permissionInterface.showProgressDialog();
        consentHandlerInterface.checkConsents(this);
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, boolean consentGiven) {
        consentHandlerInterface.post(definition, consentGiven, this);
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
        }
        adapter.onGetConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onGetConsentFailed(ConsentError error) {
        adapter.onGetConsentFailed(error);
        permissionInterface.hideProgressDialog();
        permissionInterface.showErrorDialog(error);
    }

    @Override
    public void onCreateConsentFailed(ConsentDefinition definition, ConsentError error) {
        adapter.onCreateConsentFailed(definition, error);
        permissionInterface.showErrorDialog(error);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onCreateConsentSuccess(Consent consent) {
        adapter.onCreateConsentSuccess(consent);
        permissionInterface.hideProgressDialog();
    }
}
