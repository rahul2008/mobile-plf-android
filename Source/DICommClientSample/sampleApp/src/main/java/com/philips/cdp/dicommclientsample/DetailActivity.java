package com.philips.cdp.dicommclientsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp.dicommclient.port.common.PairingListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private EditText editTextName;
    private SwitchCompat lightSwitch;
    private AirPurifier currentPurifier;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        currentPurifier = (AirPurifier) CurrentApplianceManager.getInstance().getCurrentAppliance();

        editTextName = (EditText) findViewById(R.id.editTextName);

        lightSwitch = (SwitchCompat) findViewById(R.id.switchLight);
        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                updateLightProperty(isChecked);
            }
        });

        final Button buttonSet = (Button) findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                updateNameProperty(editTextName.getText().toString());
            }
        });

        findViewById(R.id.buttonPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startPairing();
            }
        });

        updateLightSwitchView(currentPurifier.getAirPort());
        updateDeviceNameView(currentPurifier.getDevicePort());
    }

    private void updateNameProperty(final String name) {
        DevicePort devicePort = currentPurifier.getDevicePort();
        if (devicePort != null) {
            devicePort.setDeviceName(name);
        }
    }

    private void updateLightProperty(final boolean isChecked) {
        AirPort airPort = currentPurifier.getAirPort();
        if (airPort != null) {
            airPort.setLight(isChecked);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrentApplianceManager.getInstance().addApplianceListener(diCommApplianceListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CurrentApplianceManager.getInstance().removeApplianceListener(diCommApplianceListener);
    }

    private DICommApplianceListener diCommApplianceListener = new DICommApplianceListener() {

        @Override
        public void onAppliancePortUpdate(final DICommAppliance appliance, final DICommPort<?> port) {
            if (port instanceof AirPort) {
                updateLightSwitchView((AirPort) port);
            } else if (port instanceof DevicePort) {
                updateDeviceNameView((DevicePort) port);
            }
        }

        @Override
        public void onAppliancePortError(final DICommAppliance appliance, final DICommPort<?> port, final com.philips.cdp.dicommclient.request.Error error) {
        }
    };

    private void updateDeviceNameView(final DevicePort devicePort) {
        DevicePortProperties properties = devicePort.getPortProperties();
        if (properties != null) {
            editTextName.setText(properties.getName());
        }
    }

    private void updateLightSwitchView(final AirPort port) {
        AirPortProperties properties = port.getPortProperties();
        if (properties != null) {
            lightSwitch.setChecked(properties.getLightOn());
        }
    }

    private void startPairing() {

        PairingHandler<AirPurifier> pairingHandler = new PairingHandler<>(currentPurifier, new PairingListener() {
            @Override
            public void onPairingSuccess(final NetworkNode networkNode) {
                Log.d(TAG, "onPairingSuccess() called with: " + "networkNode = [" + networkNode + "]");

                DiscoveryManager<AirPurifier> discoveryManager = (DiscoveryManager<AirPurifier>) DiscoveryManager.getInstance();
                discoveryManager.insertApplianceToDatabase(currentPurifier);

                showToast("Pairing successful");
            }

            @Override
            public void onPairingFailed(final NetworkNode networkNode) {
                Log.d(TAG, "onPairingFailed() called with: " + "networkNode = [" + networkNode + "]");
                showToast("Pairing failed");
            }
        });

        pairingHandler.startPairing();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
