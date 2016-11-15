package com.launcher.commliballexample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.CommLibContext;
import com.philips.cdp2.commlib.CommLibContextBuilder;
import com.philips.cdp2.commlib.strategy.BleStrategy;
import com.philips.cdp2.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;

public class MainActivity extends AppCompatActivity {

    private static final long TIMEOUT = 10000L;
    private static final String PORTNAME_FIRMWARE = "firmware";
    private static final int PRODUCT_ID = 0;

    private TextView txtState;
    private TextView txtResult;

    private SHNCentral shnCentral;
    private SHNDevice device;
    private CommLibContext commLibContext;
    private BleStrategy strategy;

    private SHNDeviceScanner.SHNDeviceScannerListener scannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
            device = shnDeviceFoundInfo.getShnDevice();
            shnCentral.getShnDeviceScanner().stopScanning();
            commLibContext.getBleDeviceCache().deviceFound(shnDeviceScanner, shnDeviceFoundInfo);
            hookupStrategy(shnDeviceFoundInfo.getDeviceAddress());
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
                performGet();
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult shnResult) {

        }

        @Override
        public void onReadRSSI(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtState = (TextView) findViewById(R.id.txtState);
        txtResult = (TextView) findViewById(R.id.txtResult);

        CommLibContextBuilder<DICommAppliance> builder = new CommLibContextBuilder<>(this);
        builder.setApplianceFactory(new DICommApplianceFactory<DICommAppliance>() {
            @Override
            public boolean canCreateApplianceForNode(final NetworkNode networkNode) {
                return false;
            }

            @Override
            public DICommAppliance createApplianceForNode(final NetworkNode networkNode) {
                return null;
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

    private void hookupStrategy(String deviceAddress) {
        txtState.setText(R.string.lblHookingUpStrategy);
        strategy = new BleStrategy(deviceAddress, commLibContext.getBleDeviceCache());
        connectToDevice();
    }

    private void connectToDevice() {
        txtState.setText(R.string.lblConnectingToDevice);
        device.registerSHNDeviceListener(deviceListener);
        device.connect();
    }

    private void performGet() {
        txtState.setText(getString(R.string.lblPerformingGetProperties, PORTNAME_FIRMWARE));
        strategy.getProperties(PORTNAME_FIRMWARE, PRODUCT_ID, new ResponseHandler() {
            @Override
            public void onSuccess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtResult.setText(s);
                        txtState.setText(R.string.lblDone);
                    }
                });
            }

            @Override
            public void onError(final Error error, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtResult.setText(getString(R.string.lblGetPropsFailed, error + " " + s));
                        txtState.setText(R.string.lblDone);
                    }
                });
            }
        });
    }
}
