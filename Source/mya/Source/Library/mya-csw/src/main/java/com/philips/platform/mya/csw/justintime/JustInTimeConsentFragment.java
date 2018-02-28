/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.justintime;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.description.DescriptionView;
import com.philips.platform.mya.csw.dialogs.DialogView;
import com.philips.platform.mya.csw.dialogs.ProgressDialogView;
import com.philips.platform.mya.csw.permission.helper.ErrorMessageCreator;
import com.philips.platform.mya.csw.permission.uielement.LinkSpan;
import com.philips.platform.mya.csw.permission.uielement.LinkSpanClickListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

public class JustInTimeConsentFragment extends CswBaseFragment implements JustInTimeConsentContract.View {
    private ProgressDialogView progressDialogView;
    @LayoutRes
    private int containerId;
    private JustInTimeConsentContract.Presenter presenter;

    public static JustInTimeConsentFragment newInstance(final int containerId) {
        JustInTimeConsentFragment fragment = new JustInTimeConsentFragment();
        fragment.containerId = containerId;
        return fragment;
    }

    @Override
    public void setPresenter(JustInTimeConsentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        android.view.View justInTimeConsentView = inflater.inflate(R.layout.csw_just_in_time_consent_view, container, false);
        initializeDescriptionLabel(justInTimeConsentView);
        initializeHelpLabel(justInTimeConsentView);
        initializeGiveConsentButton(justInTimeConsentView);
        initializeConsentRejectButton(justInTimeConsentView);
        initializeUserBenefitsDescriptionLabel(justInTimeConsentView);
        initializeUserBenefitsTitleLabel(justInTimeConsentView);

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
    protected void handleOrientation(android.view.View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return JustInTimeConsentDependencies.textResources.titleTextRes;
    }

    private void initializeConsentRejectButton(android.view.View justInTimeConsentView) {
        Button rejectConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_later);
        rejectConsentButton.setText(JustInTimeConsentDependencies.textResources.rejectTextRes);
        rejectConsentButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                presenter.onConsentRejectedButtonClicked();
            }
        });
    }

    private void initializeGiveConsentButton(android.view.View justInTimeConsentView) {
        Button giveConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_ok);
        giveConsentButton.setText(JustInTimeConsentDependencies.textResources.acceptTextRes);
        giveConsentButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                presenter.onConsentGivenButtonClicked();
            }
        });
    }

    private void initializeDescriptionLabel(android.view.View justInTimeConsentView) {
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_description);
        descriptionLabel.setText(JustInTimeConsentDependencies.consentDefinition.getText());
    }

    private void initializeUserBenefitsDescriptionLabel(android.view.View justInTimeConsentView) {
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_user_benefits_description);
        descriptionLabel.setText(JustInTimeConsentDependencies.textResources.userBenefitsDescriptionRes);
    }

    private void initializeUserBenefitsTitleLabel(android.view.View justInTimeConsentView) {
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_user_benefits_title);
        descriptionLabel.setText(JustInTimeConsentDependencies.textResources.userBenefitsTitleRes);
    }

    private void initializeHelpLabel(android.view.View justInTimeConsentView) {
        Spannable helpLink = new SpannableString(getContext().getString(R.string.mya_Consent_Help_Label));
        helpLink.setSpan(new LinkSpan(new LinkSpanClickListener() {
            @Override
            public void onClick() {
                DescriptionView.show(getFragmentManager(), JustInTimeConsentDependencies.consentDefinition.getHelpText(), containerId);
            }
        }), 0, helpLink.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_helplink);
        descriptionLabel.setText(helpLink);
    }

    private void showErrorDialog(String errorTitle, String errorMessage) {
        DialogView dialogView = new DialogView();
        dialogView.showDialog(getActivity(), errorTitle, errorMessage);
    }

    @Override
    public void showErrorDialog(int errorTitleId, int errorMessageId) {
        showErrorDialog(getString(errorTitleId), getString(errorMessageId));
    }

    @Override
    public void showErrorDialogForCode(int errorTitleId, int errorCode) {
        String errorTitle = getContext().getString(errorTitleId);
        String errorMessage = ErrorMessageCreator.getMessageErrorBasedOnErrorCode(getContext(), errorCode);
        showErrorDialog(errorTitle, errorMessage);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialogView == null) {
            progressDialogView = new ProgressDialogView();
        }
        progressDialogView.showDialog(getActivity());
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialogView != null && progressDialogView.isDialogShown()) {
            progressDialogView.hideDialog();
        }
    }
}
