/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.cdp2.commlib.example.appliance.TimePort;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Random;

import static java.lang.System.currentTimeMillis;

public final class ApplianceActivity extends AppCompatActivity {

    public static final String CPPID = "cppid";

    private static final String TAG = "ApplianceActivity";
    private static final String PROPERTY_DATETIME = "datetime";
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
        findViewById(R.id.btnFirmwareUpgrade).setOnClickListener(buttonClickListener);

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtPerformanceProgress = (TextView) findViewById(R.id.txtPerformanceProgress);
        switchLoopGet = (CompoundButton) findViewById(R.id.switchLoopGet);

        ((CompoundButton) findViewById(R.id.switchStayConnected)).setOnCheckedChangeListener(connectionCheckedChangeListener);
        ((CompoundButton) findViewById(R.id.switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);
        switchLoopGet.setOnCheckedChangeListener(loopGetCheckedChangeListener);

        bleReferenceAppliance = (BleReferenceAppliance) ((App) getApplication()).getCommCentral().getApplianceManager().findApplianceByCppId(getIntent().getExtras().getString(CPPID));

        if (bleReferenceAppliance == null) {
            finish();
        } else {
            getSupportActionBar().setTitle(bleReferenceAppliance.getNetworkNode().getName());

            findViewById(R.id.btnGetTime).setEnabled(true);
            findViewById(R.id.btnSetTime).setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        bleReferenceAppliance.getTimePort().addPortListener(timePortListener);

        boolean stayConnected = ((CompoundButton) findViewById(R.id.switchStayConnected)).isChecked();

        if (stayConnected) {
            bleReferenceAppliance.enableCommunication();
        } else {
            bleReferenceAppliance.disableCommunication();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        bleReferenceAppliance.getTimePort().removePortListener(timePortListener);
    }

    private final DICommPortListener<TimePort> timePortListener = new DICommPortListener<TimePort>() {
        @Override
        public void onPortUpdate(TimePort timePort) {
            final String datetime = timePort.getPortProperties().datetime;
            if (datetime == null) {
                return;
            }
            DateTime dt = new DateTime(datetime);
            String dateTimeString = DATETIME_FORMATTER.print(dt);

            updateResult(dateTimeString);
            onPortEvent(timePort);
        }

        @Override
        public void onPortError(TimePort timePort, Error error, final String s) {
            Log.e(TAG, "Time port error: " + error.getErrorMessage() + " (" + s + ")");

            updateResult(getString(R.string.lblResultPortError, s));
            onPortEvent(timePort);
        }

        private void onPortEvent(TimePort timePort) {
            if (switchLoopGet.isChecked()) {
                timePort.reloadProperties();
            }
        }
    };

    private void updateResult(final String result) {
        requestCount++;
        txtPerformanceProgress.setText("Count: " + requestCount);
        txtResult.setText("Last result: " + result);
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
                case R.id.btnFirmwareUpgrade:
                    Intent firmwareUpgradeIntent = new Intent(ApplianceActivity.this, FirmwareUpgradeActivity.class);
                    firmwareUpgradeIntent.putExtra(ApplianceActivity.CPPID, bleReferenceAppliance.getNetworkNode().getCppId());
                    startActivity(firmwareUpgradeIntent);
                    break;
                default:
                    Log.d(TAG, "Unknown view clicked");
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
}
