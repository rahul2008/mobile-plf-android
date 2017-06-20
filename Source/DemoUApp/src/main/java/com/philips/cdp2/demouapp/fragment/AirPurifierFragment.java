/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.appliance.DICommApplianceListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp.dicommclient.port.common.PairingListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.port.air.AirPort;
import com.philips.cdp2.demouapp.port.air.AirPortProperties;
import com.philips.cdp2.demouapp.appliance.airpurifier.AirPurifier;

public class AirPurifierFragment extends Fragment {
    private static final String TAG = "AirPurifierFragment";

    private EditText editTextName;
    private EditText editTextUserId;
    private EditText editTextUserToken;
    private SwitchCompat lightSwitch;

    private AirPurifier currentAppliance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_airpurifier_detail, container, false);

        editTextName = (EditText) rootview.findViewById(R.id.editTextName);
        editTextUserId = (EditText) rootview.findViewById(R.id.userId);
        editTextUserToken = (EditText) rootview.findViewById(R.id.userToken);

        lightSwitch = (SwitchCompat) rootview.findViewById(R.id.switchLight);
        final Button buttonSet = (Button) rootview.findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                updateNameProperty(editTextName.getText().toString());
            }
        });

        rootview.findViewById(R.id.buttonPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startPairing();
            }
        });

        rootview.findViewById(R.id.buttonUnPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startUnpairing();
            }
        });

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                updateLightProperty(isChecked);
            }
        });

        currentAppliance = (AirPurifier) CurrentApplianceManager.getInstance().getCurrentAppliance();

        if (currentAppliance == null) {
            getFragmentManager().popBackStack();
        } else {
            updateView(currentAppliance);
        }

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        CurrentApplianceManager.getInstance().addApplianceListener(diCommApplianceListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        CurrentApplianceManager.getInstance().removeApplianceListener(diCommApplianceListener);
    }

    private DICommApplianceListener diCommApplianceListener = new DICommApplianceListener() {

        @Override
        public void onAppliancePortUpdate(final Appliance appliance, final DICommPort<?> port) {
            updateView(appliance);
        }

        @Override
        public void onAppliancePortError(Appliance appliance, DICommPort<?> port, Error error) {
        }
    };

    private void updateNameProperty(final String name) {
        DevicePort devicePort = currentAppliance.getDevicePort();
        if (devicePort != null) {
            devicePort.setDeviceName(name);
        }
    }

    private void updateLightProperty(final boolean isChecked) {
        AirPort airPort = currentAppliance.getAirPort();

        if (airPort != null) {
            airPort.setLight(isChecked);
        }
    }

    private void updateView(@NonNull final Appliance appliance) {
        DevicePortProperties devicePortProperties = appliance.getDevicePort().getPortProperties();
        if (devicePortProperties != null) {
            final String portName = devicePortProperties.getName();
            if (!portName.equals(editTextName.getText().toString())) {
                editTextName.setText(portName);
            }
        }

        AirPortProperties airPortProperties = currentAppliance.getAirPort().getPortProperties();
        if (airPortProperties != null) {
            lightSwitch.setChecked(airPortProperties.getLightOn());
        }
    }

    private void startPairing() {
        PairingHandler<AirPurifier> pairingHandler = new PairingHandler<>(currentAppliance, new PairingListener<AirPurifier>() {

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

        String id = editTextUserId.getText().toString();
        String token = editTextUserToken.getText().toString();
        if (id.length() > 0 && token.length() > 0) {
            pairingHandler.startUserPairing(id, token);
        } else {
            pairingHandler.startPairing();
        }
    }

    private void startUnpairing() {
        PairingHandler<AirPurifier> pairingHandler = new PairingHandler<>(currentAppliance, new PairingListener<AirPurifier>() {

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
