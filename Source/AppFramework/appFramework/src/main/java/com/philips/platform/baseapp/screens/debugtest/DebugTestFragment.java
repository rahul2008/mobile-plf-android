/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.debugtest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.utility.AppStateConfiguration;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.Arrays;
import java.util.List;

/**
 * This fragment if for internal testing of dynamic configuration change of User registration
 */

public class DebugTestFragment extends AbstractAppFrameworkBaseFragment implements BackEventListener{
    public static final String TAG = DebugTestFragment.class.getSimpleName();
//    private List<String> list;
    private TextView configurationTextView;
    private Spinner spinner;
    private Context context;
    private final String AppInfra = "appinfra";

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_DebugScreen_Title);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBar(getString(R.string.RA_DebugScreen_Title), true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_debug_fragment, container, false);
        setUpView(view);
        startAppTagging();
        return view;
    }

    @NonNull
    protected List<String> getList(String configurationType[]) {
        return Arrays.asList(configurationType);
    }

    protected void startAppTagging() {
        AppFrameworkTagging.getInstance().trackPage(TAG);
    }

    protected void setUpView(final View view) {
        String configurationType[] =
                {
                        AppStateConfiguration.DEVELOPMENT.getValue(),
                        AppStateConfiguration.STAGING.getValue(),
                        AppStateConfiguration.TEST.getValue()
                };

        context = getActivity();
        initViews(view);
        setSpinnerAdaptor(configurationType);
        int position = getList(configurationType).indexOf(getApplicationContext().getAppState());
        setSpinnerSelection(position, configurationType);
        getSpinner().setOnItemSelectedListener(getSpinnerListener(getList(configurationType)));
        getConfigurationTextView().setTextColor(ContextCompat.getColor(context, R.color.uikit_white));
    }

    @NonNull
    protected AdapterView.OnItemSelectedListener getSpinnerListener(final List<String> list) {
        return new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final AdapterView<?> adapter, View v,
                                       final int position, long id) {
                final int appStatePosition = list.indexOf(getApplicationContext().getAppState());
                if (appStatePosition != position) {

                    new AlertDialog.Builder(context, R.style.alertDialogStyle)
                            .setTitle(getString(R.string.RA_Change_Configuration))
                            .setMessage(context.getResources().getString(R.string.RA_change_config_desc))

                            .setPositiveButton(getString(R.string.RA_OK),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            settingState(adapter, position);
                                        }
                                    })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
    }

    protected void settingState(AdapterView<?> adapter, int position) {
        final String configuration = adapter.getItemAtPosition(position).toString();
        getUserRegistration().getUserObject(context).logout(null);
        if (configuration.equalsIgnoreCase(AppStateConfiguration.DEVELOPMENT.getValue())) {
            setState(AppStateConfiguration.DEVELOPMENT.getValue());
        } else if (configuration.equalsIgnoreCase(AppStateConfiguration.TEST.getValue())) {
            setState(AppStateConfiguration.TEST.getValue());
        } else if (configuration.equalsIgnoreCase(AppStateConfiguration.STAGING.getValue())) {
            setState(AppStateConfiguration.STAGING.getValue());
        }
        getConfigurationTextView().setText(configuration);
    }

    private TextView getConfigurationTextView() {
        return configurationTextView;
    }

    protected UserRegistrationSettingsState getUserRegistration() {
        return new UserRegistrationSettingsState();
    }

    protected void setSpinnerSelection(int position, String configurationType[]) {
        if (position >= 0) {
            getSpinner().setSelection(position);

            getConfigurationTextView().setText(configurationType[position]);
        } else {
            getConfigurationTextView().setText(configurationType[0]);
        }
    }

    protected void setSpinnerAdaptor(String configurationType[]) {
        ArrayAdapter<String> configType = getArrayAdapter(configurationType);
        getSpinner().setAdapter(configType);
    }

    protected Spinner getSpinner() {
        return spinner;
    }

    @NonNull
    protected ArrayAdapter<String> getArrayAdapter(String[] configurationType) {
        ArrayAdapter<String> configType = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, configurationType);
        configType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return configType;
    }

    protected void initViews(final View view) {
        spinner = (Spinner) view.findViewById(R.id.spinner);
        configurationTextView = (TextView) view.findViewById(R.id.configuration);
    }

    protected void setState(final String state) {
        String APPIDENTITY_APP_STATE = "appidentity.appState";

        AppInfraInterface appInfra = getApplicationContext().getAppInfra();
        AppConfigurationInterface appConfigurationInterface = appInfra.getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        appConfigurationInterface.setPropertyForKey(APPIDENTITY_APP_STATE,
                AppInfra, state, configError);
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getActivity().getApplicationContext();
    }

    @Override
    public boolean handleBackEvent() {
        return true;
    }
}