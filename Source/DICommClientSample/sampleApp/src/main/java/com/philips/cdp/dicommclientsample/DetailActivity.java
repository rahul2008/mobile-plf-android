/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.appliance.DICommApplianceListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp.dicommclient.port.common.PairingListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclientsample.airpurifier.AirPort;
import com.philips.cdp.dicommclientsample.airpurifier.AirPortProperties;
import com.philips.cdp.dicommclientsample.airpurifier.AirPurifier;
import com.philips.cdp.dicommclientsample.reference.WifiReferenceAppliance;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.port.time.TimePort;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");

    private EditText editTextName;
    private EditText editTextUserId;
    private EditText editTextUserToken;
    private TextView timeTextView;
    private SwitchCompat lightSwitch;
    private Appliance currentAppliance;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUserId = (EditText) findViewById(R.id.userId);
        editTextUserToken = (EditText) findViewById(R.id.userToken);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

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

        updateView(currentAppliance);

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                updateLightProperty(isChecked);
            }
        });

        startTimePortSubscription();
    }

    private void startTimePortSubscription() {
        if (currentAppliance instanceof WifiReferenceAppliance) {
            WifiReferenceAppliance wifiReferenceAppliance = (WifiReferenceAppliance) currentAppliance;
            wifiReferenceAppliance.getTimePort().addPortListener(new DICommPortListener<TimePort>() {

                @Override
                public void onPortUpdate(TimePort timePort) {
                    final String datetime = timePort.getPortProperties().datetime;
                    if (datetime == null) {
                        return;
                    }
                    DateTime dt = new DateTime(datetime);
                    String dateTimeString = DATETIME_FORMATTER.print(dt);

                    timeTextView.setVisibility(View.VISIBLE);
                    timeTextView.setText(dateTimeString);
                    Log.d(TAG, "Time port update: " + timePort.getPortProperties().datetime);
                }

                @Override
                public void onPortError(TimePort port, Error error, String errorData) {
                    timeTextView.setVisibility(View.VISIBLE);
                    timeTextView.setText("Error: " + errorData);
                }
            });
            wifiReferenceAppliance.subscribe();
        }
    }

    private void updateNameProperty(final String name) {
        DevicePort devicePort = currentAppliance.getDevicePort();
        if (devicePort != null) {
            devicePort.setDeviceName(name);
        }
    }

    private void updateLightProperty(final boolean isChecked) {
        if (currentAppliance instanceof AirPurifier) {
            AirPurifier airPurifier = (AirPurifier) currentAppliance;
            AirPort airPort = airPurifier.getAirPort();

            if (airPort != null) {
                airPort.setLight(isChecked);
            }
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
        public void onAppliancePortUpdate(final Appliance appliance, final DICommPort<?> port) {
            updateView(appliance);
        }

        @Override
        public void onAppliancePortError(final Appliance appliance, final DICommPort<?> port, final Error error) {
        }
    };

    private void updateView(@NonNull final Appliance appliance) {
        DevicePortProperties devicePortProperties = appliance.getDevicePort().getPortProperties();
        if (devicePortProperties != null) {
            editTextName.setText(devicePortProperties.getName());
        }

        if (appliance instanceof AirPurifier) {
            findViewById(R.id.lightStateContainer).setVisibility(View.VISIBLE);
            AirPurifier airPurifier = (AirPurifier) appliance;
            AirPortProperties airPortProperties = airPurifier.getAirPort().getPortProperties();
            if (airPortProperties != null) {
                lightSwitch.setChecked(airPortProperties.getLightOn());
            }
        }
    }

    private void startPairing() {
        final Appliance purifier = this.currentAppliance;
        PairingHandler<Appliance> pairingHandler = new PairingHandler<>(purifier, new PairingListener<Appliance>() {

            @Override
            public void onPairingSuccess(final Appliance appliance) {
                Log.d(TAG, "onPairingSuccess() called with: " + "appliance = [" + appliance + "]");

                DiscoveryManager<Appliance> discoveryManager = (DiscoveryManager<Appliance>) DiscoveryManager.getInstance();
                discoveryManager.insertApplianceToDatabase(appliance);

                showToast("Pairing successful");
            }

            @Override
            public void onPairingFailed(final Appliance appliance) {
                Log.d(TAG, "onPairingFailed() called with: " + "appliance = [" + appliance + "]");
                showToast("Pairing failed");
            }
        });

        String id = editTextUserId.getText().toString();
        String token = editTextUserToken.getText().toString();
        if (id.length() > 0 && token.length() > 0) {
            pairingHandler.startUserPairing(id, token);
        } else {
            pairingHandler.startPairing();
        }
    }

    private void startUnpairing() {
        final Appliance purifier = this.currentAppliance;
        PairingHandler<Appliance> pairingHandler = new PairingHandler<>(purifier, new PairingListener<Appliance>() {

            @Override
            public void onPairingSuccess(final Appliance appliance) {
                Log.d(TAG, "onPairingSuccess() called with: " + "appliance = [" + appliance + "]");

                DiscoveryManager<Appliance> discoveryManager = (DiscoveryManager<Appliance>) DiscoveryManager.getInstance();
                discoveryManager.insertApplianceToDatabase(appliance);

                showToast("Unpaired successfully");
            }

            @Override
            public void onPairingFailed(final Appliance appliance) {
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
