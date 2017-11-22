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
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.csw.utils.CswLogger;

import java.util.List;
import java.util.Map;

public class PermissionPresenter implements GetConsentInteractor.Callback, ConsentToggleListener {

    private PermissionInterface permissionInterface;
    private GetConsentInteractor getConsentInteractor;
    private CreateConsentInteractor createConsentInteractor;
    private PermissionAdapter adapter;
    private List<ConsentView> consentViews;
    public static final int version = 0;

    PermissionPresenter(
            PermissionInterface permissionInterface, GetConsentInteractor getConsentInteractor, CreateConsentInteractor createConsentInteractor, List<ConsentView> consentViews) {
        this.permissionInterface = permissionInterface;
        this.getConsentInteractor = getConsentInteractor;
        this.createConsentInteractor = createConsentInteractor;
        this.consentViews = consentViews;
        adapter = new PermissionAdapter(consentViews, this);
    }

    public PermissionAdapter getAdapter() {
        return adapter;
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
    public void onConsentRetrieved(@NonNull Map<String, RequiredConsent> consents) {
        for(ConsentView consentView: consentViews) {
            consentView.storeConsent(consents.get(consentView.getType()));
        }
        adapter.onConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onToggledConsent(ConsentDefinition definition, boolean on) {
        createConsentInteractor.createConsentStatus(definition, on, new CreateConsentResponseHandler());
    }

    private class CreateConsentResponseHandler implements CreateConsentListener {

        @Override
        public void onSuccess(int code) {
            CswLogger.d(" Create Consent: ", "Success : " + code);
        }

        @Override
        public int onFailure(int errCode) {
            CswLogger.d(" Create Consent: ", "Failed : " + errCode);
            return errCode;
        }
    }
}
