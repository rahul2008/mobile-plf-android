/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.debugtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

import java.util.Arrays;
import java.util.List;

public class DebugTestFragment extends AppFrameworkBaseFragment {
    public static final String TAG = DebugTestFragment.class.getSimpleName();
    private String configurationType[] = {"Staging", "Evaluation", "Testing", "Development", "Production"};
    private List<String> list = Arrays.asList(configurationType);
    private TextView txt_title, configurationTextView;
    private Spinner spinner;
    private SharedPreferences sharedPreferences;
    private Context context;
    private TextView version;

    @Override
    public String getActionbarTitle() {
        return "Debug and Test ";
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
        final String PRODUCT_REGISTRATION = "prod_demo";
        sharedPreferences = context.getSharedPreferences(PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
        initViews(view);
        setSpinnerAdaptor();
        final int position = list.indexOf(sharedPreferences.getString("reg_env", "Evaluation"));
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
                int position1 = list.indexOf(sharedPreferences.getString("reg_env", "Evaluation"));
                if (position1 != position) {
                    User user = new User(context);
                    user.logout(null);
                    Log.d(TAG, "Before Configuration" + configuration);
                    if (configuration.equalsIgnoreCase("Development")) {
                        initialiseUserRegistration("Development");
                    } else if (configuration.equalsIgnoreCase("Testing")) {
                        initialiseUserRegistration("Testing");
                    } else if (configuration.equalsIgnoreCase("Evaluation")) {
                        initialiseUserRegistration("Evaluation");
                    } else if (configuration.equalsIgnoreCase("Staging")) {
                        initialiseUserRegistration("Staging");
                    } else if (configuration.equalsIgnoreCase("Production")) {
                        initialiseUserRegistration("Production");
                    }
                    Log.d(TAG, "After Configuration" + configuration);
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

        version.setText(" App Version " + BuildConfig.VERSION_NAME);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        configurationTextView = (TextView) view.findViewById(R.id.configuration);
    }

    private void initialiseUserRegistration(final String development) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reg_env", development);
        editor.commit();

    }

}
