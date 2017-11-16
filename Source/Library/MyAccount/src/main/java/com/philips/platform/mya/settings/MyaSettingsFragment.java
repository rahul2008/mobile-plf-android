/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.R;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.util.mvp.MyaBaseFragment;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.view.widget.Label;


public class MyaSettingsFragment extends MyaBaseFragment implements View.OnClickListener {

    private TextView countryTextView;
    private RelativeLayout consentLayout;
    private AppInfraInterface appInfra;
    Label philipsWebsite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_settings_fragment, container, false);
        this.appInfra = MyaInterface.getMyaDependencyComponent().getAppInfra();
        countryTextView = (TextView) view.findViewById(R.id.settings_country_value);
        consentLayout = (RelativeLayout) view.findViewById(R.id.consent_layout);
        countryTextView.setText(appInfra.getServiceDiscovery().getHomeCountry());
        philipsWebsite=  (Label) view.findViewById(R.id.philips_website);
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
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        consentLayout.setOnClickListener(this);
        philipsWebsite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId== R.id.consent_layout) {
            CswInterface cswInterface = new CswInterface();
            CswDependencies cswDependencies = new CswDependencies(appInfra);
            cswDependencies.setApplicationName(MyaInterface.getApplicationName());
            cswDependencies.setPropositionName(MyaInterface.getPropositionName());
            UappSettings uappSettings = new UappSettings(getContext());
            cswInterface.init(cswDependencies, uappSettings);
            cswInterface.launch(MyaInterface.getMyaUiComponent().getFragmentLauncher(), buildLaunchInput(true));
        } else if (viewId == R.id.philips_website) {
            String url = "http://www.Philips.com";
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setPropositionName(MyaInterface.getPropositionName());
        cswLaunchInput.setApplicationName(MyaInterface.getApplicationName());
        cswLaunchInput.addToBackStack(addToBackStack);
        cswLaunchInput.setContext(getContext());
        return cswLaunchInput;
    }
}
