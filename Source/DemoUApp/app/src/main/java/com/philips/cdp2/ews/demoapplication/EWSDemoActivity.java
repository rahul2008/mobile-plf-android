/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.demoapplication;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

public class EWSDemoActivity extends EWSDemoBaseActivity implements View.OnClickListener {

    private Spinner configSpinner;
    private static final String WAKEUP_LIGHT = "wl";
    private static final String AIRPURIFIER = "ap";
    private ObservableField<String> selection = new ObservableField<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewsdemo);
        findViewById(R.id.btnLaunchEws).setOnClickListener(this);
        findViewById(R.id.btnFragmentLaunch).setOnClickListener(this);

        configSpinner = (Spinner) findViewById(R.id.configurationSelection);
        configSpinner.setOnItemSelectedListener(itemSelectedListener);

        selection.set(DEFAULT);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.configurations));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        configSpinner.setAdapter(aa);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLaunchEws:
                launchEwsUApp();
                break;
            case R.id.btnFragmentLaunch:
                startDemoFragmentActivity();
            default:
                break;
        }
    }

    private void startDemoFragmentActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("SelectedConfig", selection.get());
        Intent intent = new Intent(this, EWSDemoFragmentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void launchEwsUApp() {
        AppInfraInterface appInfra = new AppInfra.Builder().build(getApplicationContext());
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap(), isDefaultValueSelected((String) configSpinner.getSelectedItem())), new UappSettings(getApplicationContext()));
        ewsInterface.launch(new ActivityLauncher(SCREEN_ORIENTATION_PORTRAIT, null, -1, null), new EWSLauncherInput());
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 1:
                    selection.set(WAKEUP_LIGHT);
                    updateCurrentContent(WAKEUP_LIGHT);
                    break;
                case 2:
                    selection.set(AIRPURIFIER);
                    updateCurrentContent(AIRPURIFIER);
                    break;
                case 0:
                default:
                    selection.set(DEFAULT);
                    updateCurrentContent("");
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
        }
    };
}