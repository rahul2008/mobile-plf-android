package com.launcher.commliballexample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.launcher.commliballexample.appliance.BleReferenceAppliance;
import com.launcher.commliballexample.appliance.BleReferenceApplianceFactory;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
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
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;

public class MainActivity extends AppCompatActivity {

    private static final long TIMEOUT = 10000L;

    private TextView txtState;
    private TextView txtResult;

    private BleReferenceApplianceFactory applianceFactory;
    private CommLibContext commLibContext;
    private SHNCentral shnCentral;
    private SHNDevice device;

    private SHNDeviceScanner.SHNDeviceScannerListener scannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
            device = shnDeviceFoundInfo.getShnDevice();
            shnCentral.getShnDeviceScanner().stopScanning();
            commLibContext.getBleDeviceCache().deviceFound(shnDeviceScanner, shnDeviceFoundInfo);

            connectToDevice();
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            // NOOP
        }
    };

    private SHNDevice.SHNDeviceListener deviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            if (SHNDevice.State.Connected.equals(shnDevice.getState())) {

                NetworkNode networkNode = createNetworkNode(device);
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

        startSearchingForDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (device != null) {
            device.disconnect();
        }
    }

    private void startSearchingForDevice() {
        txtState.setText(R.string.lblScanningForDevice);
        shnCentral.getShnDeviceScanner().startScanning(scannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, TIMEOUT);
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

    private void connectToDevice() {
        txtState.setText(R.string.lblConnectingToDevice);
        device.registerSHNDeviceListener(deviceListener);
        device.connect();
    }
}
