/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.FetchConsentsCallback;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.CswConstants;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.dialogs.ConfirmDialogTextResources;
import com.philips.platform.mya.csw.dialogs.ConfirmDialogView;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.mya.csw.utils.TaggingUtils;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SEND_DATA;

public class PermissionPresenter implements PermissionContract.Presenter, FetchConsentsCallback, PostConsentCallback {

    private static final String CONSENT_CENTER = "consentCenter";

    @NonNull
    private final PermissionContract.View view;

    @NonNull
    private final PermissionAdapter adapter;
    private AppTaggingInterface appTaggingInterface;

    private int togglePosition;
    private boolean toggleStatus;
    private Map<String, String> preparedTaggingInfo;

    PermissionPresenter(
            @NonNull final PermissionContract.View view, @NonNull final PermissionAdapter adapter) {
        this.view = view;
        this.view.setPresenter(this);
        this.adapter = adapter;
        this.appTaggingInterface = CswInterface.getCswComponent().getAppTaggingInterface().createInstanceForComponent(CswConstants.Tagging.COMPONENT_ID, BuildConfig.VERSION_NAME);
        this.adapter.setPresenter(this);
    }

    @NonNull
    PermissionAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void trackPageName() {
        appTaggingInterface.trackPageWithInfo(CONSENT_CENTER, null);
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitionList) {
        if (!consentDefinitionList.isEmpty()) {
            view.showProgressDialog();
            try {
                ConsentManagerInterface consentManager = CswInterface.getCswComponent().getConsentManager();
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
            ConfirmDialogTextResources confirmDialogTextResources = new ConfirmDialogTextResources(
                    R.string.csw_privacy_settings,
                    definition.getRevokeWarningText(),
                    R.string.mya_csw_consent_revoked_confirm_btn_ok,
                    R.string.mya_csw_consent_revoked_confirm_btn_cancel);
            this.view.showConfirmRevokeConsentDialog(confirmDialogTextResources, new ConfirmDialogView.ConfirmDialogResultHandler() {
                @Override
                public void onOkClicked() {
                    postConsentChange(definition, false);
                }

                @Override
                public void onCancelClicked() {
                    responseHandler.handleResponse(true);
                }
            });
        }
        else {
            postConsentChange(definition, consentGiven);
        }
    }

    private void postConsentChange(ConsentDefinition definition, boolean consentGiven) {
        toggleStatus = consentGiven;
        view.showProgressDialog();
        CswInterface.getCswComponent().getConsentManager().storeConsentState(definition, consentGiven, this);
        preparedTaggingInfo = prepareTrackActionInfo(definition, consentGiven);
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
        view.hideProgressDialog();
    }

    @Override
    public void onGetConsentsFailed(ConsentError error) {
        adapter.onGetConsentFailed(error);
        view.hideProgressDialog();
        showErrorDialogFor(true, error);
    }

    @Override
    public void onPostConsentFailed(ConsentError error) {
        adapter.onCreateConsentFailed(togglePosition, error);
        view.hideProgressDialog();
        showErrorDialogFor(false, error);
    }

    @Override
    public void onPostConsentSuccess() {
        adapter.onCreateConsentSuccess(togglePosition, toggleStatus);
        appTaggingInterface.trackActionWithInfo(SEND_DATA, preparedTaggingInfo);
        view.hideProgressDialog();
    }

    private void showErrorDialogFor(final boolean goBack, final ConsentError error) {
        if (error.getErrorCode() == ConsentError.CONSENT_ERROR_NO_CONNECTION) {
            view.showErrorDialog(goBack, R.string.csw_offline_title, R.string.csw_offline_message);
        } else {
            view.showErrorDialog(goBack, R.string.csw_problem_occurred_error_title, error);
        }
    }

    @VisibleForTesting
    protected RestInterface getRestClient() {
        return CswInterface.get().getDependencies().getAppInfra().getRestClient();
    }

    private Map<String, String> prepareTrackActionInfo(ConsentDefinition definition, boolean consentGiven) {
        final Map<String, String> info = new HashMap<>();
        info.put(CswConstants.Tagging.SPECIAL_EVENTS, (consentGiven ? CswConstants.Tagging.CONSENT_ACCEPTED : CswConstants.Tagging.CONSENT_REJECTED));
        info.put(CswConstants.Tagging.CONSENT_TYPE, TaggingUtils.join(definition.getTypes()));
        return info;
    }
}