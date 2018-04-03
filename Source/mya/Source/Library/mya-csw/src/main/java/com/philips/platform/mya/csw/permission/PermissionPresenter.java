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
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.FetchConsentsCallback;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.dialogs.ConfirmDialogView;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.mya.csw.permission.helper.ErrorMessageCreator;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import java.util.List;

public class PermissionPresenter implements ConsentToggleListener, FetchConsentsCallback, PostConsentCallback {

    public Context mContext;

    @NonNull
    private final PermissionInterface permissionInterface;

    @NonNull
    private final PermissionAdapter adapter;

    private int togglePosition;

    private boolean toggleStatus;

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
    public void onToggledConsent(int position, final ConsentDefinition definition, final boolean consentGiven, final ConsentToggleResponse responseHandler) {
        togglePosition = position;
        if(definition.hasRevokeWarningText() && !consentGiven) {
            // User has revoked consent
            ConfirmDialogView dialog = new ConfirmDialogView();
            dialog.setupDialog(
                R.string.csw_privacy_settings,
                definition.getRevokeWarningText(),
                R.string.mya_csw_consent_revoked_confirm_btn_ok,
                R.string.mya_csw_consent_revoked_confirm_btn_cancel
            );
            this.permissionInterface.showConfirmRevokeConsentDialog(dialog, new ConfirmDialogView.ConfirmDialogResultHandler() {
                @Override
                public void onOkClicked() {
                    boolean wasConsentPosted = postConsentChange(definition, false);
                    if(!wasConsentPosted) {
                        responseHandler.handleResponse(false);
                    }
                }

                @Override
                public void onCancelClicked() {
                    responseHandler.handleResponse(true);
                }
            });
        }
        else {
            boolean wasConsentPosted = postConsentChange(definition, consentGiven);
            if(!wasConsentPosted) {
                responseHandler.handleResponse(!consentGiven);
            }
        }
    }

    private boolean postConsentChange(ConsentDefinition definition, boolean consentGiven) {
        toggleStatus = consentGiven;
        boolean isOnline = getRestClient().isInternetReachable();
        if(isOnline) {
            permissionInterface.showProgressDialog();
            CswInterface.getCswComponent().getConsentManager().storeConsentState(definition, consentGiven, this);
            return true;
        }
        else {
            String offlineTitle = mContext.getString(R.string.mya_Offline_title);
            String offlineMessage = mContext.getString(R.string.mya_Offline_message);
            permissionInterface.showErrorDialog(false, offlineTitle, offlineMessage);
            return false;
        }
    }

    @Override
    public void onGetConsentsSuccess(List<ConsentDefinitionStatus> consentDefinitionStatusList) {
        List<ConsentView> consentViews = adapter.getConsentViews();
        for (ConsentView consentView : consentViews) {
            for (ConsentDefinitionStatus consentDefinitionStatus : consentDefinitionStatusList) {
                if (consentDefinitionStatus.getConsentDefinition() == consentView.getDefinition()) {
                    consentView.storeConsentDefnitionStatus(consentDefinitionStatus);
                }
            }
        }
        adapter.onGetConsentRetrieved(consentViews);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public void onGetConsentsFailed(ConsentError error) {
        adapter.onGetConsentFailed(error);
        permissionInterface.hideProgressDialog();
        permissionInterface.showErrorDialog(true, mContext.getString(R.string.csw_problem_occurred_error_title), toErrorMessage(error));
    }

    @Override
    public void onPostConsentFailed(ConsentError error) {
        adapter.onCreateConsentFailed(togglePosition, error);
        permissionInterface.hideProgressDialog();
        permissionInterface.showErrorDialog(false, mContext.getString(R.string.csw_problem_occurred_error_title), toErrorMessage(error));
    }

    @Override
    public void onPostConsentSuccess() {
        adapter.onCreateConsentSuccess(togglePosition, toggleStatus);
        permissionInterface.hideProgressDialog();
    }

    @VisibleForTesting
    protected RestInterface getRestClient() {
        return CswInterface.get().getDependencies().getAppInfra().getRestClient();
    }

    private String toErrorMessage(ConsentError error) {
        return ErrorMessageCreator.getMessageErrorBasedOnErrorCode(mContext, error.getErrorCode());
    }
}