/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.MyaUiHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.util.mvp.MyaBaseFragment;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class MyaSettingsFragment extends MyaBaseFragment implements View.OnClickListener {

    private TextView countryTextView;
    private RelativeLayout consentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_settings_fragment, container, false);
        countryTextView = (TextView) view.findViewById(R.id.settings_country_value);
        consentLayout = (RelativeLayout) view.findViewById(R.id.consent_layout);
        countryTextView.setText(MyaUiHelper.getInstance().getAppInfra().getServiceDiscovery().getHomeCountry());
        return view;
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        consentLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        CswInterface cswInterface = new CswInterface();
        CswDependencies cswDependencies = new CswDependencies(MyaUiHelper.getInstance().getAppInfra());
        cswDependencies.setApplicationName("shetty");
        cswDependencies.setPropositionName("loves somebody");
        UappSettings uappSettings = new UappSettings(getContext());
        cswInterface.init(cswDependencies,uappSettings);
        cswInterface.launch(MyaUiHelper.getInstance().getFragmentLauncher(), buildLaunchInput(true));
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setPropositionName("Shetty");
        cswLaunchInput.setApplicationName("loves somebody");
        cswLaunchInput.addToBackStack(addToBackStack);
        cswLaunchInput.setContext(getContext());
        return cswLaunchInput;
    }
}
