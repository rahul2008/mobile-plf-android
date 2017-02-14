/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.cdp2.commlib.example.appliance.TimePort;
import com.philips.commlib.core.appliance.Appliance;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Random;
import java.util.Set;

import static java.lang.System.currentTimeMillis;

public final class ApplianceActivity extends AppCompatActivity {

    private static final String TAG = "ApplianceActivity";

    private static final String PROPERTY_DATETIME = "datetime";
    public static final String CPPID = "cppid";
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    private int requestCount;

    private TextView txtResult;
    private TextView txtPerformanceProgress;
    private CompoundButton switchLoopGet;

    private BleReferenceAppliance bleReferenceAppliance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance);

        findViewById(R.id.btnGetTime).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnSetTime).setOnClickListener(buttonClickListener);

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtPerformanceProgress = (TextView) findViewById(R.id.txtPerformanceProgress);
        switchLoopGet = (CompoundButton) findViewById(R.id.switchLoopGet);

        ((CompoundButton) findViewById(R.id.switchStayConnected)).setOnCheckedChangeListener(connectionCheckedChangeListener);
        ((CompoundButton) findViewById(R.id.switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);
        switchLoopGet.setOnCheckedChangeListener(loopGetCheckedChangeListener);

        final Set<? extends Appliance> availableAppliances = ((App) getApplication()).getCommCentral().getApplianceManager().getAvailableAppliances();
        for (Appliance appliance: availableAppliances) {
            if (appliance.getNetworkNode().getCppId().equals(getIntent().getExtras().getString(CPPID))) {
                bleReferenceAppliance = (BleReferenceAppliance) appliance;
            }
        }

        if (bleReferenceAppliance == null) {
            finish();
        } else {
            getSupportActionBar().setTitle(bleReferenceAppliance.getNetworkNode().getName());

            setupAppliance(bleReferenceAppliance);

            findViewById(R.id.btnGetTime).setEnabled(true);
            findViewById(R.id.btnSetTime).setEnabled(true);
        }
    }

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnGetTime:
                    bleReferenceAppliance.getTimePort().reloadProperties();
                    break;
                case R.id.btnSetTime:
                    DateTime dateTime = new DateTime(currentTimeMillis() + new Random().nextInt());
                    DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                    String timestamp = dateTime.toString(fmt);

                    bleReferenceAppliance.getTimePort().putProperties(PROPERTY_DATETIME, timestamp);
                    break;
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener connectionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (bleReferenceAppliance == null) {
                return;
            }

            if (isChecked) {
                bleReferenceAppliance.enableCommunication();
            } else {
                bleReferenceAppliance.disableCommunication();
            }
        }
    };
    private final CompoundButton.OnCheckedChangeListener subscriptionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (bleReferenceAppliance == null) {
                return;
            }

            if (isChecked) {
                bleReferenceAppliance.getTimePort().subscribe();
            } else {
                bleReferenceAppliance.getTimePort().unsubscribe();
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener loopGetCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked) {
                bleReferenceAppliance.getTimePort().reloadProperties();
            }
        }
    };

    private void setupAppliance(@NonNull BleReferenceAppliance appliance) {
        boolean stayConnected = ((CompoundButton) findViewById(R.id.switchStayConnected)).isChecked();

        if (stayConnected) {
            appliance.enableCommunication();
        } else {
            appliance.disableCommunication();
        }

        appliance.getTimePort().addPortListener(new DICommPortListener<TimePort>() {

            @Override
            public void onPortUpdate(TimePort timePort) {
                final String datetime = timePort.getPortProperties().datetime;
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
                Log.e(TAG, "Time port error: " + error.getErrorMessage() + " (" + s + ")");

                updateResult(getString(R.string.lblResultPortError, s));
            }
        });
    }

    private void updateResult(final String result) {
        requestCount++;
        txtPerformanceProgress.setText("Count: " + requestCount);
        txtResult.setText("Last result: " + result);
    }
}
