package com.philips.cdp.wifirefuapp;

import android.os.Bundle;
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
import com.philips.cdp.registration.User;
import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by philips on 5/25/17.
 */

public class WifiCommLinUappWifiDetailFragment extends Fragment {
    private static final String TAG = "DetailActivity";

    private EditText editTextName;
    private EditText editTextUserId;
    private EditText editTextUserToken;
    private SwitchCompat lightSwitch;
    private AirPurifier currentPurifier;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_detail,container,false);
        currentPurifier = (AirPurifier) CurrentApplianceManager.getInstance().getCurrentAppliance();

        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextUserId = (EditText) view.findViewById(R.id.userId);
        editTextUserToken = (EditText) view.findViewById(R.id.userToken);

        lightSwitch = (SwitchCompat) view.findViewById(R.id.switchLight);
        final Button buttonSet = (Button) view.findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                updateNameProperty(editTextName.getText().toString());
            }
        });

        view.findViewById(R.id.buttonPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startPairing();
            }
        });

        view.findViewById(R.id.buttonUnPair).setOnClickListener(new View.OnClickListener() {
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
        return view;
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
    public void onResume() {
        super.onResume();
        User user= new User(getActivity());
        editTextUserId.setText(user.getHsdpUUID().toString());
        editTextUserToken.setText(user.getAccessToken().toString());
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
//            if (port instanceof AirPort) {
//                updateLightSwitchView((AirPort) port);
//            } else if (port instanceof DevicePort) {
                //updateDeviceNameView((DevicePort) port);
            //}

            Log.d(TAG,"onPortUpdate" + appliance.getName() + "::::::Port type" + port.getClass());
        }

        @Override
        public void onAppliancePortError(final Appliance appliance, final DICommPort<?> port, final com.philips.cdp.dicommclient.request.Error error) {
        }
    };

    private void updateDeviceNameView(final DevicePort devicePort) {
        DevicePortProperties properties = devicePort.getPortProperties();
        if (properties != null) {
            editTextName.setText(properties.getName());
            Map<String, Object> props = new HashMap<>();
            props.put("NAME","Manual ProductStub");
            devicePort.putProperties(props);
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

        String id = editTextUserId.getText().toString();
        String token = editTextUserToken.getText().toString();
        if (id.length() > 0 && token.length() > 0) {
            pairingHandler.startUserPairing(id, token);
        } else {
            pairingHandler.startPairing();
        }
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
