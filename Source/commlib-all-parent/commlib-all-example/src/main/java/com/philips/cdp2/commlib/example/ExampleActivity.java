/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.cdp2.commlib.example.appliance.BleReferenceApplianceFactory;
import com.philips.cdp2.commlib.example.appliance.TimePort;
import com.philips.commlib.core.CommCentral;
import com.philips.commlib.core.appliance.Appliance;
import com.philips.commlib.core.appliance.ApplianceManager;
import com.philips.commlib.core.exception.MissingPermissionException;
import com.philips.commlib.core.port.firmware.FirmwarePort;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Random;

import static java.lang.System.currentTimeMillis;

public class ExampleActivity extends AppCompatActivity {

    private static final String TAG = "ExampleActivity";

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;

    private static final String PROPERTY_DATETIME = "datetime";
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");

    private TextView txtState;
    private TextView txtResult;

    private ArrayAdapter<Appliance> applianceAdapter;
    private BleReferenceAppliance bleReferenceAppliance;

    private CommCentral commCentral;
    private DICommApplianceFactory applianceFactory;

    private Runnable permissionCallback;

    private final ApplianceManager.ApplianceListener applianceListener = new ApplianceManager.ApplianceListener<BleReferenceAppliance>() {
        @Override
        public void onApplianceFound(@NonNull BleReferenceAppliance foundAppliance) {
            Log.d(TAG, "Found appliance: " + foundAppliance.getNetworkNode().getCppId());

            applianceAdapter.clear();
            applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());
        }

        @Override
        public void onApplianceUpdated(@NonNull BleReferenceAppliance bleReferenceAppliance) {
            // NOOP
        }
    };

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnStartDiscovery:
                    startDiscovery();
                    break;
                case R.id.btnStopDiscovery:
                    stopDiscovery();
                    break;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup CommCentral
        final boolean showPopupIfBleIsTurnedOff = false;
        final BleTransportContext bleTransportContext = new BleTransportContext(this, showPopupIfBleIsTurnedOff);
        this.applianceFactory = new BleReferenceApplianceFactory(bleTransportContext);

        this.commCentral = new CommCentral(this.applianceFactory, bleTransportContext);
        this.commCentral.getApplianceManager().addApplianceListener(this.applianceListener);

        // Setup buttons
        findViewById(R.id.btnStartDiscovery).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnStopDiscovery).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnGetTime).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnSetTime).setOnClickListener(buttonClickListener);

        ((CompoundButton) findViewById(R.id.switchStayConnected)).setOnCheckedChangeListener(connectionCheckedChangeListener);
        ((CompoundButton) findViewById(R.id.switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        // Text fields
        txtState = (TextView) findViewById(R.id.txtState);
        txtResult = (TextView) findViewById(R.id.txtResult);

        // Setup appliance list
        applianceAdapter = new ArrayAdapter<Appliance>(this, android.R.layout.simple_list_item_2, android.R.id.text1) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Appliance appliance = getItem(position);

                ((TextView) view.findViewById(android.R.id.text1)).setText(appliance.getName());
                ((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%s - %s", appliance.getDeviceType(), appliance.getNetworkNode().getCppId()));

                return view;
            }
        };

        final ListView listViewAppliances = (ListView) findViewById(R.id.listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                stopDiscovery();

                bleReferenceAppliance = (BleReferenceAppliance) applianceAdapter.getItem(position);
                if (bleReferenceAppliance == null) {
                    return;
                }
                setupAppliance(bleReferenceAppliance);

                findViewById(R.id.btnGetTime).setEnabled(true);
                findViewById(R.id.btnSetTime).setEnabled(true);

                // Perform request on port
                bleReferenceAppliance.getFirmwarePort().getPortProperties();
            }
        });

        // Init view
        updateStateAndResult(getString(R.string.lblStateIdle), getString(R.string.lblResultNotApplicable));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(this.permissionCallback);
                } else {
                    updateStateAndResult(getString(R.string.lblStateError), getString(R.string.lblResultNotApplicable));
                }
            }
        }
    }

    private void acquirePermission(@NonNull Runnable permissionCallback) {
        this.permissionCallback = permissionCallback;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    private void startDiscovery() {
        applianceAdapter.clear();
        applianceAdapter.addAll(commCentral.getApplianceManager().getAvailableAppliances());

        try {
            this.commCentral.startDiscovery();
            updateStateAndResult(getString(R.string.lblStateDiscovering), getString(R.string.lblResultNotApplicable));
        } catch (MissingPermissionException e) {
            updateStateAndResult(getString(R.string.lblStatePermissionError), getString(R.string.lblResultDiscoveryFailed));

            acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startDiscovery();
                }
            });
        }
    }

    private void stopDiscovery() {
        this.commCentral.stopDiscovery();
        updateStateAndResult(getString(R.string.lblStateIdle), getString(R.string.lblResultNotApplicable));
    }

    private void updateStateAndResult(final String state, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtState.setText(state);
                txtResult.setText(result);
            }
        });
    }

    private void setupAppliance(@NonNull BleReferenceAppliance appliance) {
        boolean stayConnected = ((CompoundButton) findViewById(R.id.switchStayConnected)).isChecked();

        if (stayConnected) {
            appliance.enableCommunication();
        } else {
            appliance.disableCommunication();
        }

        // Setup firmware port
        appliance.getFirmwarePort().addPortListener(new DICommPortListener<FirmwarePort>() {
            @Override
            public void onPortUpdate(FirmwarePort firmwarePort) {
                updateStateAndResult(getString(R.string.lblStateIdle), getString(R.string.lblResultGetPropsSuccess, firmwarePort.getPortProperties().getVersion()));
            }

            @Override
            public void onPortError(FirmwarePort diCommPort, Error error, String s) {
                updateStateAndResult(getString(R.string.lblStateIdle), getString(R.string.lblResultGetPropsFailed, s));
            }
        });

        // Setup time port
        appliance.getTimePort().addPortListener(new DICommPortListener<TimePort>() {

            @Override
            public void onPortUpdate(TimePort timePort) {
                final String datetime = timePort.getPortProperties().datetime;
                if (datetime == null) {
                    return;
                }
                DateTime dt = new DateTime(datetime);
                String dateTimeString = DATETIME_FORMATTER.print(dt);

                updateStateAndResult(getString(R.string.lblStateIdle), dateTimeString);
            }

            @Override
            public void onPortError(TimePort timePort, Error error, final String s) {
                Log.e(TAG, "Time port error: " + error.getErrorMessage() + " (" + s + ")");

                updateStateAndResult(getString(R.string.lblStateIdle), getString(R.string.lblResultPortError, s));
            }
        });
    }
}
