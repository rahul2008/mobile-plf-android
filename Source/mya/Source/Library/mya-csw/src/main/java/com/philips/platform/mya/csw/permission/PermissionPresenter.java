/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.consentmanager.FetchConsentsCallback;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.mya.csw.permission.helper.ErrorMessageCreator;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import java.util.List;

public class PermissionPresenter implements ConsentToggleListener, FetchConsentsCallback, PostConsentCallback {

    public Context mContext;

    @NonNull
    private final PermissionInterface permissionInterface;

    @NonNull
    private final PermissionAdapter adapter;

    PermissionPresenter(
            @NonNull final PermissionInterface permissionInterface, @NonNull final PermissionAdapter adapter) {
        this.permissionInterface = permissionInterface;
        this.adapter = adapter;
        this.adapter.setConsentToggleListener(this);
    }

    @NonNull
    PermissionAdapter getAdapter() {
        return adapter;
    }

    void getConsentStatus(List<ConsentDefinition> consentDefinitionList) {
        ConsentManagerInterface consentManager = CswInterface.getCswComponent().getConsentManager();
        if (!consentDefinitionList.isEmpty()) {
            permissionInterface.showProgressDialog();
            try {
                consentManager.fetchConsentStates(consentDefinitionList, this);
            } catch (RuntimeException ex) {
                CswLogger.e("RuntimeException", ex.getMessage());
            }
        }
    }

    @Override
    public boolean onToggledConsent(ConsentDefinition definition, boolean consentGiven) {
        boolean isOnline = getRestClient().isInternetReachable();
        if (isOnline) {
            CswInterface.getCswComponent().getConsentManager().storeConsentState(definition, consentGiven, this);
            permissionInterface.showProgressDialog();
            return consentGiven;
        } else {
            permissionInterface.showErrorDialog(false, mContext.getString(R.string.csw_offline_title), mContext.getString(R.string.csw_offline_message));
            return !consentGiven;
        }
    }

   /* @Override
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
                if (consentView.getType().equals(CONSENT_TYPE_CLICKSTREAM)) {
                    updateClickStream(consentView.isChecked());
                }
            }
        }
        adapter.onGetConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }*/


    @Override
    public void onGetConsentsSuccess(List<ConsentDefinitionStatus> consentDefinitionStatusList) {
        //TODO update the status wrt the results
    }

    @Override
    public void onGetConsentsFailed(ConsentError error) {
        adapter.onGetConsentFailed(error);
        permissionInterface.hideProgressDialog();
        permissionInterface.showErrorDialog(true, mContext.getString(R.string.csw_problem_occurred_error_title), toErrorMessage(error));
    }

    @Override
    public void onPostConsentFailed(ConsentError error) {
//        adapter.onCreateConsentFailed(definition, error); //TODO
        permissionInterface.hideProgressDialog();
        permissionInterface.showErrorDialog(false, mContext.getString(R.string.csw_problem_occurred_error_title), toErrorMessage(error));
    }

    @Override
    public void onPostConsentSuccess() {
       /* if (consent != null && consent.getType().equals(CONSENT_TYPE_CLICKSTREAM)) {
            updateClickStream(consent.getStatus().name().equals(ConsentStates.active.name()));
        }*/
//        adapter.onCreateConsentSuccess(consent); //TODO
        permissionInterface.hideProgressDialog();
    }

    @VisibleForTesting
    protected RestInterface getRestClient() {
        return CswInterface.get().getDependencies().getAppInfra().getRestClient();
    }

    private void updateClickStream(boolean isActive) {
        if (isActive) {
            CswInterface.getCswComponent().getAppTaggingInterface().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        } else {
            CswInterface.getCswComponent().getAppTaggingInterface().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
        }
    }

    private String toErrorMessage(ConsentError error) {
        return ErrorMessageCreator.getMessageErrorBasedOnErrorCode(mContext, error.getErrorCode());
    }
}