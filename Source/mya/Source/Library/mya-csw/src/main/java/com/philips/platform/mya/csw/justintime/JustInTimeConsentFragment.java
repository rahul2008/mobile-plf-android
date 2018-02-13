/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.justintime;

import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.description.DescriptionView;
import com.philips.platform.mya.csw.dialogs.DialogView;
import com.philips.platform.mya.csw.permission.helper.ErrorMessageCreator;
import com.philips.platform.mya.csw.permission.uielement.LinkSpan;
import com.philips.platform.mya.csw.permission.uielement.LinkSpanClickListener;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class JustInTimeConsentFragment extends CswBaseFragment {
    private ProgressDialog progressDialog;
    private @LayoutRes int containerId;
    public static final String TAG = "JustInTimeConsent";

    public static JustInTimeConsentFragment newInstance(final int containerId) {
        JustInTimeConsentFragment fragment = new JustInTimeConsentFragment();
        fragment.containerId = containerId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View justInTimeConsentView = inflater.inflate(R.layout.csw_just_in_time_consent_view, container, false);
        initializeDescriptionLabel(justInTimeConsentView);
        initializeHelpLabel(justInTimeConsentView);
        initializeGiveConsentButton(justInTimeConsentView);
        initializeConsentRejectButton(justInTimeConsentView);

        handleOrientation(justInTimeConsentView);
        return justInTimeConsentView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        return JustInTimeDependencies.textResources.titleTextRes;
    }

    private void initializeConsentRejectButton(View justInTimeConsentView) {
        Button rejectConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_later);
        rejectConsentButton.setText(JustInTimeDependencies.textResources.rejectTextRes);
        rejectConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConsentRejectedButtonClicked();
            }
        });
    }

    private void initializeGiveConsentButton(View justInTimeConsentView) {
        Button giveConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_ok);
        giveConsentButton.setText(JustInTimeDependencies.textResources.acceptTextRes);
        giveConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConsentGivenButtonClicked();
            }
        });
    }

    private void initializeDescriptionLabel(View justInTimeConsentView) {
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_description);
        descriptionLabel.setText(JustInTimeDependencies.consentDefinition.getText());
    }

    private void initializeHelpLabel(View justInTimeConsentView) {
        Spannable helpLink = new SpannableString(getContext().getString(R.string.mya_Consent_Help_Label));
        helpLink.setSpan(new LinkSpan(new LinkSpanClickListener() {
            @Override
            public void onClick() {
            DescriptionView.show(getFragmentManager(), JustInTimeDependencies.consentDefinition.getHelpText(), containerId);
            }
        }), 0, helpLink.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_helplink);
        descriptionLabel.setText(helpLink);
    }

    private void onConsentGivenButtonClicked() {
        postConsent(true, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                if (JustInTimeDependencies.completionListener != null) {
                    JustInTimeDependencies.completionListener.onConsentGiven();
                }
            }
        }));
    }

    private void onConsentRejectedButtonClicked() {
        postConsent(false, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                if (JustInTimeDependencies.completionListener != null) {
                    JustInTimeDependencies.completionListener.onConsentRejected();
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
        boolean isOnline = JustInTimeDependencies.appInfra.getRestClient().isInternetReachable();
        if (isOnline) {
            showProgressDialog();
            JustInTimeDependencies.consentHandlerInterface.storeConsentState(JustInTimeDependencies.consentDefinition, status, callback);
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
