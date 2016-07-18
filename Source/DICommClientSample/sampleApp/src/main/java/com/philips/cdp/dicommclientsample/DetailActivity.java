/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
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
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp.dicommclient.port.common.PairingListener;
import com.philips.cdp.dicommclientsample.airpurifier.AirPort;
import com.philips.cdp.dicommclientsample.airpurifier.AirPortProperties;
import com.philips.cdp.dicommclientsample.airpurifier.AirPurifier;

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

        findViewById(R.id.buttonUnPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startUnpairing();
            }
        });

        updateLightSwitchView(currentPurifier.getAirPort());
        updateDeviceNameView(currentPurifier.getDevicePort());

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                updateLightProperty(isChecked);
            }
        });
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

    private void updateLightSwitchView(final AirPort<? extends AirPortProperties> port) {
        AirPortProperties properties = port.getPortProperties();
        if (properties != null) {
            lightSwitch.setChecked(properties.getLightOn());
        }
    }

    private void startPairing() {

        final AirPurifier purifier = this.currentPurifier;
        PairingHandler<AirPurifier> pairingHandler = new PairingHandler<>(purifier, new PairingListener<AirPurifier>() {

            @Override
            public void onPairingSuccess(final AirPurifier appliance) {
                Log.d(TAG, "onPairingSuccess() called with: " + "appliance = [" + appliance + "]");

                DiscoveryManager<AirPurifier> discoveryManager = (DiscoveryManager<AirPurifier>) DiscoveryManager.getInstance();
                discoveryManager.insertApplianceToDatabase(appliance);

                showToast("Pairing successful");
            }

            @Override
            public void onPairingFailed(final AirPurifier appliance) {
                Log.d(TAG, "onPairingFailed() called with: " + "appliance = [" + appliance + "]");
                showToast("Pairing failed");
            }
        });

        pairingHandler.startPairing();
    }

    private void startUnpairing() {

        final AirPurifier purifier = this.currentPurifier;
        PairingHandler<AirPurifier> pairingHandler = new PairingHandler<>(purifier, new PairingListener<AirPurifier>() {

            @Override
            public void onPairingSuccess(final AirPurifier appliance) {
                Log.d(TAG, "onPairingSuccess() called with: " + "appliance = [" + appliance + "]");

                DiscoveryManager<AirPurifier> discoveryManager = (DiscoveryManager<AirPurifier>) DiscoveryManager.getInstance();
                discoveryManager.insertApplianceToDatabase(appliance);

                showToast("Unpaired successfully");
            }

            @Override
            public void onPairingFailed(final AirPurifier appliance) {
                Log.d(TAG, "onPairingFailed() called with: " + "appliance = [" + appliance + "]");
                showToast("Pairing failed");
            }
        });

        pairingHandler.initializeRelationshipRemoval();
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
