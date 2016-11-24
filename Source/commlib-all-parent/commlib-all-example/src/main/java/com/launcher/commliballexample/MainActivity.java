package com.launcher.commliballexample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.launcher.commliballexample.appliance.BleReferenceAppliance;
import com.launcher.commliballexample.appliance.BleReferenceApplianceFactory;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.FirmwarePort;
import com.philips.cdp.dicommclient.port.common.FirmwarePortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.CommLibContext;
import com.philips.cdp2.commlib.CommLibContextBuilder;
import com.philips.cdp2.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;

public class MainActivity extends AppCompatActivity {

    private TextView txtState;
    private TextView txtResult;

    private BleReferenceApplianceFactory applianceFactory;
    private CommLibContext commLibContext;
    private SHNCentral shnCentral;
    private SHNDevice device;

    private DiscoveryStrategy.DiscoveryListener discoveryListener = new DiscoveryStrategy.DiscoveryListener() {
        @Override
        public void onDiscoveryStarted() {
            txtState.setText(R.string.lblConnectingToDevice);
        }

        @Override
        public void onNetworkNodeDiscovered(NetworkNode networkNode) {
            final BleReferenceAppliance appliance = applianceFactory.createApplianceForNode(networkNode);

            if (appliance == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtResult.setText(getString(R.string.lblGetPropsFailed, "Appliance is null."));
                        txtState.setText(R.string.lblDone);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        appliance.getFirmwarePort().addPortListener(new DICommPortListener<FirmwarePort>() {
                            @Override
                            public void onPortUpdate(FirmwarePort firmwarePort) {
                                final FirmwarePortProperties portProperties = firmwarePort.getPortProperties();

                                txtResult.setText(getString(R.string.lblGetPropsSuccess, portProperties.getVersion()));
                                txtState.setText(R.string.lblDone);
                            }

                            @Override
                            public void onPortError(FirmwarePort diCommPort, Error error, String s) {
                                txtResult.setText(getString(R.string.lblGetPropsFailed, s));
                                txtState.setText(R.string.lblDone);
                            }
                        });
                        appliance.getFirmwarePort().getPortProperties();
                    }
                });
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

        txtState = (TextView) findViewById(R.id.txtState);
        txtResult = (TextView) findViewById(R.id.txtResult);

        CommLibContextBuilder<BleReferenceAppliance> builder = new CommLibContextBuilder<>(this, new CommLibContextBuilder.ApplianceFactoryBuilder<BleReferenceAppliance>() {
            @Override
            public DICommApplianceFactory<BleReferenceAppliance> create(@NonNull BleDeviceCache bleDeviceCache) {
                applianceFactory = new BleReferenceApplianceFactory(bleDeviceCache);

                return applianceFactory;
            }
        });

        commLibContext = builder.create();
        shnCentral = commLibContext.getShnCentral();

        txtState.setText(R.string.lblRegisteringDeviceDefinition);
        shnCentral.registerDeviceDefinition(new ReferenceNodeDeviceDefinitionInfo());

        txtState.setText(R.string.lblScanningForDevice);
        commLibContext.getDiscoveryStrategy().start(discoveryListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (device != null) {
            device.disconnect();
        }
    }
}
