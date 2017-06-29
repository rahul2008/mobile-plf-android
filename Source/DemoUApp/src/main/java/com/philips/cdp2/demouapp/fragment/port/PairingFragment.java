/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp.dicommclient.port.common.PairingListener;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.airpurifier.AirPurifier;

public class PairingFragment extends Fragment {
    private static final String TAG = "PairingFragment";
    private EditText editTextUserId;
    private EditText editTextUserToken;
    private AirPurifier currentAppliance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_pairing, container, false);

        editTextUserId = (EditText) rootview.findViewById(R.id.userId);
        editTextUserToken = (EditText) rootview.findViewById(R.id.userToken);

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

        currentAppliance = (AirPurifier) CurrentApplianceManager.getInstance().getCurrentAppliance();

        return rootview;
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
