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

import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.R;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;


public class JustInTimeFragmentWidget extends CswBaseFragment {
    private String title;
    private String description;
    private String okString;
    private String cancelString;

    public void setArguments(String title, String description, String okString, String cancelString) {
        this.title = title;
        this.description = description;
        this.okString = okString;
        this.cancelString = cancelString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View justInTimeConsentView = inflater.inflate(R.layout.csw_just_in_time_consent_view, container, false);

        Label descriptionLabel = justInTimeConsentView.findViewById(R.id.mya_cws_label_in_time_consent_description);
        descriptionLabel.setText(description);

        Button okButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_ok);
        okButton.setText(okString);

        Button cancelButton = justInTimeConsentView.findViewById(R.id.mya_cws_button_in_time_consent_later);
        cancelButton.setText(cancelString);

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
        //
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_privacy_settings;
    }
}
