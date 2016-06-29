/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.logging.Logger;

public class LoggingActivity extends AppCompatActivity {



    String[] LogLevels= {"ERROR","WARNING","INFO","DEBUG","VERBOSE"};
    LoggingInterface.LogLevel currentLogLevel = LoggingInterface.LogLevel.VERBOSE; //default
    String currentEventID="";
    String currentMessage="";
    int logCount=1;
    LoggingInterface AILoggingInterface;

    static Logger logger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        /////////////////////////////////////

        //create Logger
        final EditText componentNameText= (EditText) findViewById(R.id.appInfraLogComponentName);
        final EditText componentVersionCount= (EditText) findViewById(R.id.appInfraComponentVersion);
        Button createLoggerButton = (Button)findViewById(R.id.appInfraLogCreateLogger);
        createLoggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  ai = new AppInfra.Builder().setLogging(myLogger).build(getApplicationContext());

                AILoggingInterface = AppInfraApplication.gAppInfra.getLogging().createInstanceForComponent(componentNameText.getText().toString(), componentVersionCount.getText().toString()); //this.getClass().getPackage().toString()
            }
        });
        //////////////////////////////////////

        Spinner spinner = (Spinner) findViewById(R.id.appInfraLogSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,LogLevels ); //selected item will look like a spinner set from XML
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

        final CheckBox consoleLogEnabled =(CheckBox) findViewById(R.id.appInfraLogConsole);
        final CheckBox fileLogEnabled =(CheckBox) findViewById(R.id.appInfraLogFile);
        final EditText eventText= (EditText) findViewById(R.id.appInfraLogEvent);
        final EditText msgText= (EditText) findViewById(R.id.appInfraLogMessage);
        final EditText logCount= (EditText) findViewById(R.id.appInfraLogCount);
        Button logTestButton = (Button)findViewById(R.id.appInfraLogTestButton);
        logTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == AILoggingInterface) {

                    AILoggingInterface.log(currentLogLevel, eventText.getText().toString(), msgText.getText().toString());
                  /*  Toast.makeText(LoggingActivity.this, "Please initialize Logger for component",
                            Toast.LENGTH_SHORT).show();*/
                } else {
                    if (null == eventText.getText().toString() || eventText.getText().toString().isEmpty() || null == msgText.getText().toString() || msgText.getText().toString().isEmpty()) {
                        Toast.makeText(LoggingActivity.this, "Event name or message is not valid",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (consoleLogEnabled.isChecked()) {
                            AILoggingInterface.enableConsoleLog(true);
                        } else {
                            AILoggingInterface.enableConsoleLog(false);
                        }
                        if (fileLogEnabled.isChecked()) {
                            AILoggingInterface.enableFileLog(true);
                        } else {
                            AILoggingInterface.enableFileLog(false);
                        }
                        int totalLogCount = 1;

                        try {
                            totalLogCount = Integer.parseInt(logCount.getText().toString());
                        } catch (NumberFormatException nfe) {
                            System.out.println("Could not parse log count" + nfe);
                        }

                        for (int logcount = 1; logcount <= totalLogCount; logcount++) {
                            AILoggingInterface.log(currentLogLevel, eventText.getText().toString(), msgText.getText().toString());

                            // Component1 component1= new Component1(AppInfraApplication.gAppInfra);
                            // AILoggingInterface.log(currentLogLevel, "back", "back");
                        }
                    }
                }
            }
        });


    }



}
