/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.widgets;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.R;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;


public class JustInTimeFragmentWidget extends CswBaseFragment {
    private String title;
    private String description;
    private String okayText;
    private String cancelText;
    private JustInTimeWidgetHandler completionListener;
    private ConsentDefinition consentDefinition;
    private ConsentHandlerInterface consentHandlerInterface;

    public void setDependencies(ConsentDefinition consentDefinition, ConsentHandlerInterface consentHandlerInterface) {
        this.consentDefinition = consentDefinition;
        this.consentHandlerInterface = consentHandlerInterface;
    }

    public void setTextResources(String title, String description, String okayText, String cancelText) {
        this.title = title;
        this.description = description;
        this.okayText = okayText;
        this.cancelText = cancelText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View justInTimeConsentView = inflater.inflate(R.layout.csw_just_in_time_consent_view, container, false);

        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_description);
        descriptionLabel.setText(description);

        initializeGiveConsentButton(justInTimeConsentView);
        initializeConsentRejectButton(justInTimeConsentView);

        handleOrientation(justInTimeConsentView);
        return justInTimeConsentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(title);
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
        return R.string.csw_privacy_settings;
    }

    public void setCompletionListener(JustInTimeWidgetHandler completionListener) {
        this.completionListener = completionListener;
    }

    private void initializeConsentRejectButton(View justInTimeConsentView) {
        Button rejectConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_later);
        rejectConsentButton.setText(cancelText);
        rejectConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConsentRejectedButtonClicked();
            }
        });
    }

    private void initializeGiveConsentButton(View justInTimeConsentView) {
        Button giveConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_ok);
        giveConsentButton.setText(okayText);
        giveConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConsentGivenButtonClicked();
            }
        });
    }

    private void onConsentGivenButtonClicked() {
            consentHandlerInterface.post(consentDefinition, true, new PostConsentCallback() {
                @Override
                public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {

                }

                @Override
                public void onPostConsentSuccess(Consent consent) {
                    if (completionListener != null) {
                        completionListener.onConsentGiven();
                    }
                }
            });
    }

    private void onConsentRejectedButtonClicked() {
        getFragmentManager().popBackStack();
    }
}
