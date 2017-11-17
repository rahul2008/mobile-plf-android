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
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;


public class MyaSettingsFragment extends MyaBaseFragment implements View.OnClickListener {

    private AppInfraInterface appInfra;
    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_settings_fragment, container, false);
        this.appInfra = MyaInterface.getMyaDependencyComponent().getAppInfra();
        TextView countryTextView = (TextView) view.findViewById(R.id.settings_country_value);
        Button logOutButton = (Button) view.findViewById(R.id.mya_settings_logout_btn);
        RelativeLayout consentLayout = (RelativeLayout) view.findViewById(R.id.consent_layout);
        RelativeLayout countryLayout = (RelativeLayout) view.findViewById(R.id.country_layout);
        countryTextView.setText(appInfra.getServiceDiscovery().getHomeCountry());
        Label philipsWebsite = (Label) view.findViewById(R.id.philips_website);
        consentLayout.setOnClickListener(this);
        countryLayout.setOnClickListener(this);
        philipsWebsite.setOnClickListener(this);
        logOutButton.setOnClickListener(this);
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
        } else if(viewId == R.id.country_layout) {
            showDialog(getString(R.string.MYA_change_country),getString(R.string.MYA_change_country_message));
        } else if(viewId == R.id.mya_settings_logout_btn) {
            showDialog(getString(R.string.MYA_logout_title),getString(R.string.MYA_logout_message));
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

    private void showDialog(String title, String message) {
        View view = View.inflate(getContext(), R.layout.mya_dialog_layout, null);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogView(view)
                .setMessage(message)
                .setDimLayer(DialogConstants.DIM_SUBTLE)
                .setCancelable(false)
                .setTitle(title);
        TextView textView = (TextView) view.findViewById(R.id.message_label);
        Button logout = (Button) view.findViewById(R.id.mya_dialog_logout_btn);
        Button cancel = (Button) view.findViewById(R.id.mya_dialog_cancel_btn);
        textView.setText(message);
        final AlertDialogFragment alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);

        logout.setOnClickListener(handleOnClickLogOut(alertDialogFragment));
        cancel.setOnClickListener(handleOnClickCancel(alertDialogFragment));
    }

    private View.OnClickListener handleOnClickCancel(final AlertDialogFragment alertDialogFragment) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFragment.dismiss();
            }
        };
    }

    private View.OnClickListener handleOnClickLogOut(final AlertDialogFragment alertDialogFragment) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFragment.dismiss();
                clearFragmentStack();
                MyaInterface.getMyaUiComponent().getMyaListener().onLogOut();
            }
        };
    }

}
