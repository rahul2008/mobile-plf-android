/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.justintime;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.description.DescriptionView;
import com.philips.platform.mya.csw.dialogs.DialogView;
import com.philips.platform.mya.csw.permission.helper.ErrorMessageCreator;
import com.philips.platform.mya.csw.permission.uielement.LinkSpan;
import com.philips.platform.mya.csw.permission.uielement.LinkSpanClickListener;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

public class JustInTimeFragmentWidget extends CswBaseFragment {
    private JustInTimeWidgetHandler completionListener;
    private ConsentDefinition consentDefinition;
    private ConsentHandlerInterface consentHandlerInterface;
    private ProgressDialog progressDialog;
    private JustInTimeTextResources textResources;
    private
    @LayoutRes
    int containerId;
    public static final String TAG = "JustInTimeConsent";

    public static JustInTimeFragmentWidget newInstance(ConsentDefinition consentDefinition, ConsentHandlerInterface consentHandlerInterface, JustInTimeTextResources textResources, int containerId) {
        Bundle args = new Bundle();
        JustInTimeFragmentWidget fragment = new JustInTimeFragmentWidget();
        fragment.setArguments(args);
        fragment.setDependencies(consentDefinition, consentHandlerInterface, textResources, containerId);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View justInTimeConsentView = inflater.inflate(R.layout.csw_just_in_time_consent_view, container, false);

        initializeDescriptionLabel(justInTimeConsentView);
        initializeHelpLabel(justInTimeConsentView);
        initializeGiveConsentButton(justInTimeConsentView);
        initializeConsentRejectButton(justInTimeConsentView);

        handleOrientation(justInTimeConsentView);
        return justInTimeConsentView;
    }

    @Override
    protected void setViewParams(Configuration config, int width) {
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return textResources.titleTextRes;
    }

    public void setCompletionListener(JustInTimeWidgetHandler completionListener) {
        this.completionListener = completionListener;
    }

    private void setDependencies(ConsentDefinition consentDefinition, ConsentHandlerInterface consentHandlerInterface, JustInTimeTextResources textResources, int containerId) {
        this.consentDefinition = consentDefinition;
        this.consentHandlerInterface = consentHandlerInterface;
        this.textResources = textResources;
        this.containerId = containerId;
    }

    private void initializeConsentRejectButton(View justInTimeConsentView) {
        Button rejectConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_later);
        rejectConsentButton.setText(textResources.rejectTextRes);
        rejectConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConsentRejectedButtonClicked();
            }
        });
    }

    private void initializeGiveConsentButton(View justInTimeConsentView) {
        Button giveConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_ok);
        giveConsentButton.setText(textResources.acceptTextRes);
        giveConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConsentGivenButtonClicked();
            }
        });
    }

    private void initializeDescriptionLabel(View justInTimeConsentView) {
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_description);
        descriptionLabel.setText(consentDefinition.getText());
    }

    private void initializeHelpLabel(View justInTimeConsentView) {
        Spannable helpLink = new SpannableString(getContext().getString(R.string.mya_Consent_Help_Label));
        helpLink.setSpan(new LinkSpan(new LinkSpanClickListener() {
            @Override
            public void onClick() {
                DescriptionView.show(getFragmentManager(), consentDefinition.getHelpText(), containerId);
            }
        }), 0, helpLink.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_helplink);
        descriptionLabel.setText(helpLink);
    }

    private void onConsentGivenButtonClicked() {
        postConsent(true, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                if (completionListener != null) {
                    completionListener.onConsentGiven();
                }
            }
        }));
    }

    private void onConsentRejectedButtonClicked() {
        postConsent(false, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                if (completionListener != null) {
                    completionListener.onConsentRejected();
                }
            }
        }));
    }

    private void showErrorDialog(String errorTitle, String errorMessage) {
        CswLogger.e(getClass().getName(), errorMessage);
        DialogView dialogView = new DialogView();
        dialogView.showDialog(getActivity(), errorTitle, errorMessage);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private void postConsent(boolean status, PostConsentCallback callback) {
        boolean isOnline = CswInterface.get().getDependencies().getAppInfra().getRestClient().isInternetReachable();
        if (isOnline) {
            showProgressDialog();
            consentHandlerInterface.storeConsentState(consentDefinition, status, callback);
        } else {
            showErrorDialog(getString(R.string.csw_offline_title), getString(R.string.csw_offline_message));
        }
    }

    class JustInTimePostConsentCallback implements PostConsentCallback {

        private final PostConsentSuccessHandler handler;

        public JustInTimePostConsentCallback(PostConsentSuccessHandler handler) {
            this.handler = handler;
        }

        @Override
        public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
            hideProgressDialog();
            String errorTitle = getContext().getString(R.string.csw_problem_occurred_error_title);
            String errorMessage = ErrorMessageCreator.getMessageErrorBasedOnErrorCode(getContext(), error.getErrorCode());
            showErrorDialog(errorTitle, errorMessage);
        }

        @Override
        public void onPostConsentSuccess(Consent consent) {
            hideProgressDialog();
            handler.onSuccess();
        }
    }

    interface PostConsentSuccessHandler {
        void onSuccess();
    }
}
