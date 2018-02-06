/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.widgets;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.description.DescriptionView;
import com.philips.platform.mya.csw.permission.uielement.LinkSpan;
import com.philips.platform.mya.csw.permission.uielement.LinkSpanClickListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;


public class JustInTimeFragmentWidget extends CswBaseFragment {
    private JustInTimeWidgetHandler completionListener;
    private ConsentDefinition consentDefinition;
    private ConsentHandlerInterface consentHandlerInterface;

    public void setDependencies(ConsentDefinition consentDefinition, ConsentHandlerInterface consentHandlerInterface) {
        this.consentDefinition = consentDefinition;
        this.consentHandlerInterface = consentHandlerInterface;
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
        return R.string.mya_csw_justintime_title;
    }

    public void setCompletionListener(JustInTimeWidgetHandler completionListener) {
        this.completionListener = completionListener;
    }

    private void initializeConsentRejectButton(View justInTimeConsentView) {
        Button rejectConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_later);
        rejectConsentButton.setText(R.string.mya_csw_justintime_reject);
        rejectConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConsentRejectedButtonClicked();
            }
        });
    }

    private void initializeGiveConsentButton(View justInTimeConsentView) {
        Button giveConsentButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_ok);
        giveConsentButton.setText(R.string.mya_csw_justintime_accept);
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
                DescriptionView.show(getFragmentManager(), consentDefinition.getHelpText());
            }
        }), 0, helpLink.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_helplink);
        descriptionLabel.setText(helpLink);
    }

    private void onConsentGivenButtonClicked() {
        postConsent(true, new PostConsentCallback() {
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
        postConsent(false, new PostConsentCallback() {

            @Override
            public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
            }

            @Override
            public void onPostConsentSuccess(Consent consent) {
                if (completionListener != null) {
                    completionListener.onConsentRejected();
                }
            }
        });
    }

    private void postConsent(boolean status, PostConsentCallback callback) {
        consentHandlerInterface.post(consentDefinition, status, callback);
    }
}
