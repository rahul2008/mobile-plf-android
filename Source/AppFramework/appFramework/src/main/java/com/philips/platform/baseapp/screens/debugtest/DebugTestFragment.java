/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.debugtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.baseapp.screens.utility.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * This fragment if for internal testing of dynamic configuration change of User registration
 *
 */

public class DebugTestFragment extends AppFrameworkBaseFragment {
    public static final String TAG = DebugTestFragment.class.getSimpleName();
    private String configurationType[] = {Constants.STAGING, Constants.TESTING, Constants.DEVELOPMENT};
    private List<String> list = Arrays.asList(configurationType);
    private TextView configurationTextView;
    private Spinner spinner;
    private SharedPreferences sharedPreferences;
    private Context context;
    private TextView version;
    private UserRegistrationState userRegistrationState;

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_DebugScreen_Title);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppFrameworkBaseActivity)getActivity()).updateActionBarIcon(false);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_debug_fragment, container, false);
        setUp(view);
        return view;
    }

    private void setUp(final View view) {
        context = getActivity();

        sharedPreferences = context.getSharedPreferences(Constants.PRODUCT_REGISTRATION_PREFERENCES, Context.MODE_PRIVATE);
        initViews(view);
        setSpinnerAdaptor();
        final int position = list.indexOf(sharedPreferences.getString(Constants.REGISTRATION_ENV_PREFERENCES, Constants.STAGING));
        setSpinnerSelection(position);
        spinner.setOnItemSelectedListener(getSpinnerListener());
        configurationTextView.setTextColor(ContextCompat.getColor(context, R.color.uikit_white));

    }

    @NonNull
    private AdapterView.OnItemSelectedListener getSpinnerListener() {
        return new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                final String configuration = adapter.getItemAtPosition(position).toString();
                if (adapter != null && ((TextView) adapter.getChildAt(position)) != null) {
                    ((TextView) adapter.getChildAt(position)).setTextColor(Color.WHITE);
                }
                int position1 = list.indexOf(sharedPreferences.getString(Constants.REGISTRATION_ENV_PREFERENCES, Constants.EVALUATION));
                if (position1 != position) {
                    userRegistrationState = new UserRegistrationSettingsState();
                    userRegistrationState.getUserObject(context).logout(null);
                    if (configuration.equalsIgnoreCase(Constants.DEVELOPMENT)) {
                        initialiseUserRegistration(Constants.DEVELOPMENT);
                    } else if (configuration.equalsIgnoreCase(Constants.TESTING)) {
                        initialiseUserRegistration(Constants.TESTING);
                    }
                    else{
                        initialiseUserRegistration(Constants.STAGING);
                    }
                    configurationTextView.setText(configuration);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
    }

    private void setSpinnerSelection(final int position) {
        if (position >= 0) {
            spinner.setSelection(position);

            configurationTextView.setText(configurationType[position]);
        } else {
            configurationTextView.setText(configurationType[0]);
        }
    }

    private void setSpinnerAdaptor() {
        final ArrayAdapter<String> configType = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, configurationType);
        configType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(configType);
    }

    private void initViews(final View view) {
        version = (TextView) view.findViewById(R.id.version);
        version.setText(getResources().getString(R.string.RA_About_App_Version) + BuildConfig.VERSION_NAME);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        configurationTextView = (TextView) view.findViewById(R.id.configuration);
    }

    private void initialiseUserRegistration(final String development) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PRODUCT_REGISTRATION_PREFERENCES, development);
        editor.commit();
    }
}
