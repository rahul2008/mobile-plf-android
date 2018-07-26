/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.PairingPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;

import static com.philips.cdp2.commlib.demouapp.R.string.cml_pair_failed;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_pair_success;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_unpair_failed;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_unpair_success;
import static com.philips.cdp2.demouapp.fragment.ApplianceFragmentFactory.APPLIANCE_KEY;
import static com.philips.cdp2.demouapp.util.UiUtils.showIndefiniteMessage;
import static com.philips.cdp2.demouapp.util.UiUtils.showMessage;

public class PairingPortFragment extends Fragment {

    private View rootview;

    private PairingPort pairingPort;

    private final DICommPortListener pairingListener = new DICommPortListener<PairingPort>() {
        @Override
        public void onPortUpdate(PairingPort port) {
            pairingPort.removePortListener(this);

            Activity activity = getActivity();
            if (activity != null) {
                showMessage(rootview, getString(cml_pair_success));
            }
        }

        @Override
        public void onPortError(PairingPort port, Error error, @Nullable String errorData) {
            pairingPort.removePortListener(this);

            Activity activity = getActivity();
            if (activity != null) {
                showIndefiniteMessage(rootview, getString(cml_pair_failed));
            }
        }
    };

    private final DICommPortListener unpairingListener = new DICommPortListener<PairingPort>() {
        @Override
        public void onPortUpdate(PairingPort port) {
            pairingPort.removePortListener(this);

            Activity activity = getActivity();
            if (activity != null) {
                showMessage(rootview, getString(cml_unpair_success));
            }
        }

        @Override
        public void onPortError(PairingPort port, Error error, @Nullable String errorData) {
            pairingPort.removePortListener(this);

            Activity activity = getActivity();
            if (activity != null) {
                showIndefiniteMessage(rootview, getString(cml_unpair_failed));
            }
        }
    };

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (pairingPort == null) {
                return;
            }

            final String clientProvider = getTextFor(R.id.cml_client_provider, rootview);
            final String clientType = getTextFor(R.id.cml_client_type, rootview);
            final String clientId = getTextFor(R.id.cml_client_id, rootview);
            final String secretKey = getTextFor(R.id.cml_secret_key, rootview);
            final String type = getTextFor(R.id.cml_type, rootview);
            final String[] permissions = getTextFor(R.id.cml_permissions, rootview).split(",");

            int id = view.getId();
            if (id == R.id.cml_button_pair) {
                pairingPort.addPortListener(pairingListener);
                pairingPort.pair(clientProvider, clientType, clientId, secretKey, type, permissions);
            } else if (id == R.id.cml_button_unpair) {
                pairingPort.addPortListener(unpairingListener);
                pairingPort.unpair(clientProvider, clientType, clientId, type);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.cml_fragment_port_pairing, container, false);

        final String cppId = getArguments().getString(APPLIANCE_KEY);
        Appliance appliance = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager().findApplianceByCppId(cppId);
        if (appliance != null) {
            pairingPort = appliance.getPairingPort();
        }

        rootview.findViewById(R.id.cml_button_pair).setOnClickListener(clickListener);
        rootview.findViewById(R.id.cml_button_unpair).setOnClickListener(clickListener);

        return rootview;
    }

    private String getTextFor(int id, View fragmentView) {
        return ((EditText) fragmentView.findViewById(id)).getText().toString();
    }
}
