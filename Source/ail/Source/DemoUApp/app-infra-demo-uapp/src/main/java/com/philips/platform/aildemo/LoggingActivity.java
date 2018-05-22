/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.consentmanager.ConsentManager;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.appinfra.consentmanager.consenthandler.DeviceStoredConsentHandler;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.logging.CloudConsentProvider;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.logging.sync.CloudLogSyncManager;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class LoggingActivity extends AppCompatActivity {

    private AppInfra appInfra;
    static Logger logger;
    String[] LogLevels = {"ERROR", "WARNING", "INFO", "DEBUG", "VERBOSE" };
    LoggingInterface.LogLevel currentLogLevel = LoggingInterface.LogLevel.VERBOSE; //default
    String currentEventID = "";
    String currentMessage = "";
    int logCount = 1;
    LoggingInterface AILoggingInterface;
    private Switch cloudLoggingConsentSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);
        appInfra = (AppInfra) AILDemouAppInterface.getInstance().getAppInfra();
        cloudLoggingConsentSwitch = findViewById(R.id.cloud_logging_switch);

        cloudLoggingConsentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConsentDefinition consentDefinition=((ConsentManager)appInfra.getConsentManager()).getConsentDefinitionForType(CloudConsentProvider.CLOUD);
                appInfra.getConsentManager().storeConsentState(consentDefinition, isChecked, new PostConsentCallback() {
                    @Override
                    public void onPostConsentFailed(ConsentError error) {
                        Log.v("SyncTesting","Error while saving consent");
                    }

                    @Override
                    public void onPostConsentSuccess() {
                        Log.v("SyncTesting","Changed consent sucessfully");
                    }
                });
            }
        });
        appInfra.getConsentManager().fetchConsentTypeState(CloudConsentProvider.CLOUD, new FetchConsentCallback() {
            @Override
            public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
                if (consentDefinitionStatus != null && consentDefinitionStatus.getConsentState() != null) {
                    switch (consentDefinitionStatus.getConsentState()) {
                        case inactive:
                        case rejected:
                            cloudLoggingConsentSwitch.setChecked(false);
                            break;
                        case active:
                            cloudLoggingConsentSwitch.setChecked(true);
                            break;
                    }
                }

            }

            @Override
            public void onGetConsentFailed(ConsentError error) {

            }
        });
        /////////////////////////////////////

        //create Logger
        final EditText componentNameText = (EditText) findViewById(R.id.appInfraLogComponentName);
        final EditText componentVersionCount = (EditText) findViewById(R.id.appInfraComponentVersion);
        Button createLoggerButton = (Button) findViewById(R.id.appInfraLogCreateLogger);
        createLoggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  ai = new AppInfra.Builder().setLogging(myLogger).build(getApplicationContext());

                AILoggingInterface = AILDemouAppInterface.getInstance().getAppInfra().getLogging().createInstanceForComponent(componentNameText.getText().toString(), componentVersionCount.getText().toString()); //this.getClass().getPackage().toString()
            }
        });
        //////////////////////////////////////

        Spinner spinner = (Spinner) findViewById(R.id.appInfraLogSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LogLevels); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "ERROR":
                        currentLogLevel = LoggingInterface.LogLevel.ERROR;
                        break;
                    case "WARNING":
                        currentLogLevel = LoggingInterface.LogLevel.WARNING;
                        break;
                    case "INFO":
                        currentLogLevel = LoggingInterface.LogLevel.INFO;
                        break;
                    case "DEBUG":
                        currentLogLevel = LoggingInterface.LogLevel.DEBUG;
                        break;
                    case "VERBOSE":
                        currentLogLevel = LoggingInterface.LogLevel.VERBOSE;
                        break;

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final EditText eventText = (EditText) findViewById(R.id.appInfraLogEvent);
        final EditText msgText = (EditText) findViewById(R.id.appInfraLogMessage);
        final EditText logCount = (EditText) findViewById(R.id.appInfraLogCount);
        Button logTestButton = (Button) findViewById(R.id.appInfraLogTestButton);
        logTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == AILoggingInterface) {

                    AILoggingInterface = AILDemouAppInterface.getInstance().getAppInfra().getLogging();
                    AILoggingInterface.log(currentLogLevel, eventText.getText().toString(), msgText.getText().toString());
                } else {
                    if (eventText.getText().toString().isEmpty() || msgText.getText().toString().isEmpty()) {
                        Toast.makeText(LoggingActivity.this, "Event name or message is not valid",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        int totalLogCount = 1;

                        try {
                            totalLogCount = Integer.parseInt(logCount.getText().toString());
                        } catch (NumberFormatException nfe) {
                            Log.e("LoggingActivity", "Could not parse log count");
                        }

                        for (int logcount = 1; logcount <= totalLogCount; logcount++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("key1", "val1");
                            map.put("key2", "val2");
                            AILoggingInterface.log(currentLogLevel, eventText.getText().toString(), msgText.getText().toString(), map);
                        }
                    }


                }
            }
        });


    }


}
