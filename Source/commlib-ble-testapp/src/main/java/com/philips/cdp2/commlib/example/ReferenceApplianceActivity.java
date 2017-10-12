/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.example.appliance.ReferenceAppliance;
import com.philips.cdp2.commlib.example.port.time.TimePort;
import com.philips.cdp2.commlib.example.port.time.TimePortProperties;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Locale;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

public abstract class ReferenceApplianceActivity extends AppCompatActivity {

    private static final String TAG = "RefApplianceActivity";

    private static final String PROPERTY_DATETIME = "datetime";
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    private int requestCount = 0;

    private TextView txtResult;
    private TextView txtProgress;

    private CompoundButton switchLoopGet;

    private EditText deviceNameEdit;

    private ReferenceAppliance currentAppliance;

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();

            if (viewId == R.id.btnGetTime) {
                currentAppliance.getTimePort().reloadProperties();
            } else if (viewId == R.id.btnSetTime) {
                DateTime dateTime = new DateTime(currentTimeMillis() + new Random().nextInt());
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                String timestamp = dateTime.toString(fmt);

                currentAppliance.getTimePort().putProperties(PROPERTY_DATETIME, timestamp);
            } else if (viewId == R.id.btnFirmwareUpgrade) {
                CurrentApplianceManager.getInstance().setCurrentAppliance(currentAppliance);

                startActivity(new Intent(ReferenceApplianceActivity.this, FirmwareUpgradeActivity.class));
            } else {
                DICommLog.d(TAG, "Unknown view clicked");
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener connectionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (currentAppliance == null) {
                return;
            }

            if (isChecked) {
                currentAppliance.enableCommunication();
            } else {
                currentAppliance.disableCommunication();
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener subscriptionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (currentAppliance == null) {
                return;
            }

            if (isChecked) {
                currentAppliance.getTimePort().subscribe();
            } else {
                currentAppliance.getTimePort().unsubscribe();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_appliance);

        findViewById(R.id.btnGetTime).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnSetTime).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnFirmwareUpgrade).setOnClickListener(buttonClickListener);

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtProgress = (TextView) findViewById(R.id.txtProgress);

        switchLoopGet = (CompoundButton) findViewById(R.id.switchLoopGet);
        switchLoopGet.setOnCheckedChangeListener(loopGetCheckedChangeListener);

        ((CompoundButton) findViewById(R.id.switchStayConnected)).setOnCheckedChangeListener(connectionCheckedChangeListener);
        ((CompoundButton) findViewById(R.id.switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        deviceNameEdit = (EditText) findViewById(R.id.device_name);
        Button setButton = (Button) findViewById(R.id.btn_set);
        Button getButton = (Button) findViewById(R.id.btn_get);

        currentAppliance = getCurrentAppliance();

        if (currentAppliance == null) {
            finish();
        } else {
            setupAppliance(currentAppliance);

            findViewById(R.id.btnGetTime).setEnabled(true);
            findViewById(R.id.btnSetTime).setEnabled(true);
        }

        setButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentAppliance.getDevicePort().setDeviceName(deviceNameEdit.getText().toString());
                    }
                }
        );

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAppliance.getDevicePort().reloadProperties();
            }
        });

        currentAppliance.getDevicePort().addPortListener(new DICommPortListener() {
            @Override
            public void onPortUpdate(DICommPort port) {
                DevicePortProperties properties = (DevicePortProperties) port.getPortProperties();
                if (properties == null) {
                    return;
                }
                deviceNameEdit.setText(properties.getName());
            }

            @Override
            public void onPortError(DICommPort port, Error error, String errorData) {
                Log.e(TAG, "Device port error:" + errorData);
            }
        });

        currentAppliance.getDevicePort().reloadProperties();
    }

    protected abstract ReferenceAppliance getCurrentAppliance();

    private final CompoundButton.OnCheckedChangeListener loopGetCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked) {
                currentAppliance.getTimePort().reloadProperties();
            }
        }
    };

    private void setupAppliance(@NonNull ReferenceAppliance appliance) {
        boolean stayConnected = ((CompoundButton) findViewById(R.id.switchStayConnected)).isChecked();

        if (stayConnected) {
            appliance.enableCommunication();
        } else {
            appliance.disableCommunication();
        }

        appliance.getTimePort().addPortListener(new DICommPortListener<TimePort>() {

            @Override
            public void onPortUpdate(TimePort timePort) {
                TimePortProperties properties = timePort.getPortProperties();
                if (properties == null) {
                    return;
                }

                final String datetime = properties.datetime;
                if (datetime == null) {
                    return;
                }
                DateTime dt = new DateTime(datetime);
                String dateTimeString = DATETIME_FORMATTER.print(dt);

                updateResult(dateTimeString);

                if (switchLoopGet.isChecked()) {
                    timePort.reloadProperties();
                }
            }

            @Override
            public void onPortError(TimePort timePort, Error error, final String s) {
                DICommLog.e(TAG, "Time port error: " + error.getErrorMessage() + " (" + s + ")");

                updateResult(getString(R.string.lblResultPortError, s));

                if (switchLoopGet.isChecked()) {
                    timePort.reloadProperties();
                }
            }
        });
    }

    private void updateResult(final String result) {
        requestCount++;

        txtProgress.setText(String.format(Locale.US, "Count: %d", requestCount));
        txtResult.setText(String.format(Locale.US, "Last result: %s", result));
    }
}
