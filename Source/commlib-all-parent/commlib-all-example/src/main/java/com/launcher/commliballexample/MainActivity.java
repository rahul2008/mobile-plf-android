package com.launcher.commliballexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.launcher.commliballexample.appliance.BleReferenceAppliance;
import com.launcher.commliballexample.appliance.BleReferenceApplianceFactory;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.exception.MissingPermissionException;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.FirmwarePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.CommLibContext;
import com.philips.cdp2.commlib.CommLibContextBuilder;
import com.philips.cdp2.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNCentral;

public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;

    private Button btnStartDiscovery;
    private TextView txtState;
    private TextView txtResult;

    private BleReferenceApplianceFactory applianceFactory;
    private CommLibContext commLibContext;
    private SHNCentral shnCentral;

    private DiscoveryStrategy.DiscoveryListener discoveryListener = new DiscoveryStrategy.DiscoveryListener() {
        @Override
        public void onDiscoveryStarted() {
            txtState.setText(R.string.lblConnectingToDevice);
        }

        @Override
        public void onNetworkNodeDiscovered(NetworkNode networkNode) {
            BleReferenceAppliance appliance = applianceFactory.createApplianceForNode(networkNode);

            if (appliance == null) {
                updateStateAndResult(getString(R.string.lblDone), getString(R.string.lblGetPropsFailed, "Unsupported network node: " + networkNode.getName()));
            } else {
                appliance.getFirmwarePort().addPortListener(new DICommPortListener<FirmwarePort>() {
                    @Override
                    public void onPortUpdate(FirmwarePort firmwarePort) {
                        updateStateAndResult(getString(R.string.lblDone), getString(R.string.lblGetPropsSuccess, firmwarePort.getPortProperties().getVersion()));
                    }

                    @Override
                    public void onPortError(FirmwarePort diCommPort, Error error, String s) {
                        updateStateAndResult(getString(R.string.lblDone), getString(R.string.lblGetPropsFailed, s));
                    }
                });
                appliance.getFirmwarePort().getPortProperties();
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
        public void onDiscoveryFinished() {
            // NOOP
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommLibContextBuilder<BleReferenceAppliance> builder = new CommLibContextBuilder<>(this, new CommLibContextBuilder.ApplianceFactoryBuilder<BleReferenceAppliance>() {
            @Override
            public DICommApplianceFactory<BleReferenceAppliance> create(@NonNull BleDeviceCache bleDeviceCache) {
                applianceFactory = new BleReferenceApplianceFactory(bleDeviceCache);

                return applianceFactory;
            }
        });

        commLibContext = builder.create();
        shnCentral = commLibContext.getShnCentral();
        shnCentral.registerDeviceDefinition(new ReferenceNodeDeviceDefinitionInfo());

        btnStartDiscovery = (Button) findViewById(R.id.btnStartDiscovery);
        btnStartDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtState.setText(R.string.lblScanningForDevice);

                try {
                    commLibContext.getDiscoveryStrategy().start(MainActivity.this, discoveryListener);
                } catch (MissingPermissionException e) {
                    txtState.setText(R.string.lblDiscoveryFailed);

                    acquirePermission();
                }
            }
        });

        txtState = (TextView) findViewById(R.id.txtState);
        txtState.setText(R.string.lblReady);

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtResult.setText(R.string.lblNotApplicable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            }
        }
    }

    private void acquirePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
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
}
