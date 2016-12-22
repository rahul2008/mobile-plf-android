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
import android.view.View;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.FirmwarePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.CommCentral;
import com.philips.cdp2.commlib.appliance.ApplianceManager;
import com.philips.cdp2.commlib.context.BleTransportContext;
import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.cdp2.commlib.example.appliance.BleReferenceApplianceFactory;
import com.philips.cdp2.commlib.example.appliance.TimePort;
import com.philips.cdp2.commlib.exception.MissingPermissionException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;

    private static final String PROPERTY_DATETIME = "datetime";
    private static final String CHRISTMAS_TIMESTAMP = "2016-12-25T00:00:00+00:00";
    private static final String DATE_OUTPUT_FORMAT = "dd MMM YYYY";

    private TextView txtState;
    private TextView txtResult;

    private CommCentral commCentral;
    private DICommApplianceFactory applianceFactory;
    private BleReferenceAppliance bleReferenceAppliance;

    private Runnable permissionCallback;

    private final ApplianceManager.ApplianceListener applianceListener = new ApplianceManager.ApplianceListener() {
        @Override
        public <A extends DICommAppliance> void onApplianceFound(@NonNull A foundAppliance) {
            bleReferenceAppliance = (BleReferenceAppliance) foundAppliance;
            setupAppliance(bleReferenceAppliance);

            // Perform request on port
            bleReferenceAppliance.getFirmwarePort().getPortProperties();
        }
    };

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnStartDiscovery:
                    startDiscovery();
                    break;
                case R.id.btnGetTime:
                    bleReferenceAppliance.getTimePort().reloadProperties();
                    break;
                case R.id.btnSetTime:
                    bleReferenceAppliance.getTimePort().putProperties(PROPERTY_DATETIME, CHRISTMAS_TIMESTAMP);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup CommCentral
        final BleTransportContext bleTransportContext = new BleTransportContext(this, true);
        final Set<DiscoveryStrategy> discoveryStrategies = new HashSet<DiscoveryStrategy>() {{
            add(bleTransportContext.getDiscoveryStrategy());
        }};
        this.applianceFactory = new BleReferenceApplianceFactory(bleTransportContext);
        this.commCentral = new CommCentral(discoveryStrategies, this.applianceFactory);
        this.commCentral.getApplianceManager().addApplianceManagerListener(this.applianceListener);

        // Setup buttons
        findViewById(R.id.btnStartDiscovery).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnGetTime).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnSetTime).setOnClickListener(buttonClickListener);

        // Text fields
        txtState = (TextView) findViewById(R.id.txtState);
        txtResult = (TextView) findViewById(R.id.txtResult);

        // Init view
        updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultNotApplicable));
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
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot create bleReferenceAppliance for provided NetworkNode.");
        }

        // Setup firmware port
        appliance.getFirmwarePort().addPortListener(new DICommPortListener<FirmwarePort>() {
            @Override
            public void onPortUpdate(FirmwarePort firmwarePort) {
                updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultGetPropsSuccess, firmwarePort.getPortProperties().getVersion()));
            }

            @Override
            public void onPortError(FirmwarePort diCommPort, Error error, String s) {
                updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultGetPropsFailed, s));
            }
        });

        // Setup time port
        appliance.getTimePort().addPortListener(new DICommPortListener<TimePort>() {

            @Override
            public void onPortUpdate(TimePort timePort) {
                DateTime dateTime = new DateTime(timePort.getPortProperties().datetime);
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_OUTPUT_FORMAT);

                updateStateAndResult(getString(R.string.lblStateDone), dateTime.toString(dateTimeFormatter));
            }

            @Override
            public void onPortError(TimePort timePort, Error error, final String s) {
                updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultPortError, s));
            }
        });
    }
}
