/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.exception.MissingPermissionException;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.FirmwarePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.CommLibContext;
import com.philips.cdp2.commlib.CommLibContextBuilder;
import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.cdp2.commlib.example.appliance.BleReferenceApplianceFactory;
import com.philips.cdp2.commlib.example.appliance.TimePort;
import com.philips.cdp2.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;
    private static final long BLE_SCAN_TIMEOUT_MS = 20000L;

    private static final String PROPERTY_DATETIME = "datetime";
    private static final String CHRISTMAS_TIMESTAMP = "2016-12-25T00:00:00+00:00";
    private static final String DATE_OUTPUT_FORMAT = "dd MMM YYYY";

    private TextView txtState;
    private TextView txtResult;

    private BleReferenceApplianceFactory applianceFactory;
    private CommLibContext commLibContext;
    private SHNCentral shnCentral;
    private SHNDevice device;
    private DiscoveryStrategy discoveryStrategy;
    private BleReferenceAppliance appliance;

    private Runnable permissionCallback;

    private final DiscoveryStrategy.DiscoveryListener discoveryListener = new DiscoveryStrategy.DiscoveryListener() {

        @Override
        public void onDiscoveryStarted() {
            updateStateAndResult(getString(R.string.lblStateDiscovering), getString(R.string.lblResultNotApplicable));
        }

        @Override
        public void onNetworkNodeDiscovered(NetworkNode networkNode) {
            try {
                appliance = setupAppliance(createNetworkNode(device));

                // Perform request on port
                appliance.getFirmwarePort().getPortProperties();
            } catch (IllegalArgumentException e) {
                updateStateAndResult(getString(R.string.lblStateError), getString(R.string.lblResultPortError, "Appliance is null."));
            }
        }

        @Override
        public void onNetworkNodeLost(NetworkNode networkNode) {
            // NOOP
        }

        @Override
        public void onNetworkNodeUpdated(NetworkNode networkNode) {
            // NOOP
        }

        @Override
        public void onDiscoveryStopped() {
            updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultNotApplicable));
        }
    };

    private final SHNDeviceScanner.SHNDeviceScannerListener scannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
            shnDeviceScanner.stopScanning();

            device = shnDeviceFoundInfo.getShnDevice();
            commLibContext.getBleDeviceCache().addDevice(device);

            connect();
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            // NOOP
        }
    };

    private final SHNDevice.SHNDeviceListener deviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            if (SHNDevice.State.Connected.equals(shnDevice.getState())) {
                updateStateAndResult(getString(R.string.lblStateConnected), getString(R.string.lblResultNotApplicable));

                try {
                    appliance = setupAppliance(createNetworkNode(device));
                } catch (IllegalArgumentException e) {
                    updateStateAndResult(getString(R.string.lblStateError), getString(R.string.lblResultPortError, "Appliance is null."));
                }
            } else if (SHNDevice.State.Disconnected.equals(shnDevice.getState())) {
                updateStateAndResult(getString(R.string.lblStateDisconnected), getString(R.string.lblResultNotApplicable));
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult shnResult) {
            // NOOP
        }

        @Override
        public void onReadRSSI(int i) {
            // NOOP
        }
    };

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnStartDiscovery:
                    startDiscovery(MainActivity.this);
                    break;
                case R.id.btnStopDiscovery:
                    stopDiscovery();
                    break;
                case R.id.btnConnect:
                    scan();
                    break;
                case R.id.btnDisconnect:
                    disconnect();
                    break;
                case R.id.btnGetTime:
                    appliance.getTimePort().reloadProperties();
                    break;
                case R.id.btnSetTime:
                    appliance.getTimePort().putProperties(PROPERTY_DATETIME, CHRISTMAS_TIMESTAMP);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup CommLib
        CommLibContextBuilder<BleReferenceAppliance> builder = new CommLibContextBuilder<>(this, new CommLibContextBuilder.ApplianceFactoryBuilder<BleReferenceAppliance>() {
            @Override
            public DICommApplianceFactory<BleReferenceAppliance> create(@NonNull BleDeviceCache bleDeviceCache) {
                applianceFactory = new BleReferenceApplianceFactory(bleDeviceCache);

                return applianceFactory;
            }
        });

        commLibContext = builder.create();

        // Setup BlueLib
        shnCentral = commLibContext.getShnCentral();
        shnCentral.registerDeviceDefinition(new ReferenceNodeDeviceDefinitionInfo());

        // Setup discovery strategy
        discoveryStrategy = commLibContext.getDiscoveryStrategy();
        discoveryStrategy.addDiscoveryListener(discoveryListener);

        // Setup buttons
        findViewById(R.id.btnStartDiscovery).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnStopDiscovery).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnConnect).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnDisconnect).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnGetTime).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnSetTime).setOnClickListener(buttonClickListener);

        // Text fields
        txtState = (TextView) findViewById(R.id.txtState);
        txtResult = (TextView) findViewById(R.id.txtResult);

        // Init view
        updateStateAndResult(getString(R.string.lblStateDisconnected), getString(R.string.lblResultNotApplicable));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disconnect();
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

    private void startDiscovery(final Context context) {
        try {
            discoveryStrategy.start(context);
        } catch (MissingPermissionException e) {
            updateStateAndResult(getString(R.string.lblStatePermissionError), getString(R.string.lblResultDiscoveryFailed));

            acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startDiscovery(context);
                }
            });
        }
    }

    private void stopDiscovery() {
        discoveryStrategy.stop();
        updateStateAndResult(getString(R.string.lblStateDisconnected), getString(R.string.lblResultNotApplicable));
    }

    private void scan() {
        updateStateAndResult(getString(R.string.lblStateScanning), getString(R.string.lblResultNotApplicable));
        shnCentral.getShnDeviceScanner().startScanning(scannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, BLE_SCAN_TIMEOUT_MS);
    }

    private void connect() {
        updateStateAndResult(getString(R.string.lblStateConnecting), getString(R.string.lblResultNotApplicable));
        device.registerSHNDeviceListener(deviceListener);
        device.connect();
    }

    private void disconnect() {
        if (device != null) {
            device.disconnect();
            updateStateAndResult(getString(R.string.lblStateDisconnected), getString(R.string.lblResultNotApplicable));
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

    private BleReferenceAppliance setupAppliance(NetworkNode networkNode) {
        BleReferenceAppliance appliance = applianceFactory.createApplianceForNode(networkNode);
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot create appliance for provided NetworkNode.");
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

        return appliance;
    }

    private NetworkNode createNetworkNode(final SHNDevice shnDevice) {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setBootId(-1L);
        networkNode.setCppId(shnDevice.getAddress()); // TODO cloud identifier; hijacked address for now
        networkNode.setName(shnDevice.getName()); // TODO Friendly name
        networkNode.setModelName(BleReferenceAppliance.MODELNAME); // TODO model name, e.g. Polaris
        networkNode.setModelType(null); // TODO model type, e.g. FC8932
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        return networkNode;
    }
}
