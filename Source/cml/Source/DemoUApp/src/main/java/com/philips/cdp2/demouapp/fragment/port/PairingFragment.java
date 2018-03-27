/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp.dicommclient.port.common.PairingListener;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;

import static com.philips.cdp2.commlib.cloud.context.CloudTransportContext.getCloudController;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_pair_failed;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_pair_success;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_unpair_success;
import static com.philips.cdp2.demouapp.util.UiUtils.showIndefiniteMessage;
import static com.philips.cdp2.demouapp.util.UiUtils.showMessage;

public class PairingFragment extends Fragment {
    private static final String TAG = "PairingFragment";

    private EditText editTextUserId;
    private EditText editTextUserToken;
    private Appliance currentAppliance;

    private ApplianceManager applianceManager;
    private View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.cml_fragment_pairing, container, false);

        applianceManager = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager();

        editTextUserId = (EditText) rootview.findViewById(R.id.cml_userId);
        editTextUserToken = (EditText) rootview.findViewById(R.id.cml_userToken);

        rootview.findViewById(R.id.cml_buttonPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startPairing();
            }
        });

        rootview.findViewById(R.id.cml_buttonUnPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startUnpairing();
            }
        });

        currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();

        return rootview;
    }

    private void startPairing() {
        PairingHandler<Appliance> pairingHandler = new PairingHandler<>(currentAppliance, new PairingListener<Appliance>() {

            @Override
            public void onPairingSuccess(final Appliance appliance) {
                Log.d(TAG, "onPairingSuccess() called with: " + "appliance = [" + appliance + "]");

                Activity activity = getActivity();
                if (activity != null) {
                    showMessage(getActivity(), rootview, getString(cml_pair_success));
                }
            }

            @Override
            public void onPairingFailed(final Appliance appliance) {
                Log.d(TAG, "onPairingFailed() called with: " + "appliance = [" + appliance + "]");

                Activity activity = getActivity();
                if (activity != null) {
                    showIndefiniteMessage(getActivity(), rootview, getString(cml_pair_failed));
                }
            }
        }, getCloudController());

        String id = editTextUserId.getText().toString();
        String token = editTextUserToken.getText().toString();
        if (id.length() > 0 && token.length() > 0) {
            pairingHandler.startUserPairing(id, token);
        } else {
            pairingHandler.startPairing();
        }
    }

    private void startUnpairing() {
        PairingHandler<Appliance> pairingHandler = new PairingHandler<>(currentAppliance, new PairingListener<Appliance>() {

            @Override
            public void onPairingSuccess(final Appliance appliance) {
                Log.d(TAG, "onPairingSuccess() called with: " + "appliance = [" + appliance + "]");

                Activity activity = getActivity();
                if (activity != null) {
                    showMessage(getActivity(), rootview, getString(cml_unpair_success));
                }
            }

            @Override
            public void onPairingFailed(final Appliance appliance) {
                Log.d(TAG, "onPairingFailed() called with: " + "appliance = [" + appliance + "]");

                Activity activity = getActivity();
                if (activity != null) {
                    showIndefiniteMessage(getActivity(), rootview, getString(cml_pair_failed));
                }
            }
        }, getCloudController());

        pairingHandler.initializeRelationshipRemoval();
    }
}
