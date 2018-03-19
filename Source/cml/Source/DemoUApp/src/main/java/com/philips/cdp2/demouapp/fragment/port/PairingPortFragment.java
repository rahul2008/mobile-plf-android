/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;

public class PairingPortFragment extends Fragment {

    private View rootview;

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Appliance appliance = CurrentApplianceManager.getInstance().getCurrentAppliance();
            if (appliance == null) {
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
                appliance.getPairingPort().pair(clientProvider, clientType, clientId, secretKey, type, permissions);
            } else if (id == R.id.cml_button_unpair) {
                appliance.getPairingPort().unpair(clientProvider, clientType, clientId, secretKey, type);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.cml_fragment_port_pairing, container, false);

        rootview.findViewById(R.id.cml_button_pair).setOnClickListener(clickListener);
        rootview.findViewById(R.id.cml_button_unpair).setOnClickListener(clickListener);

        return rootview;
    }

    private String getTextFor(int id, View fragmentView) {
        return ((EditText) fragmentView.findViewById(id)).getText().toString();
    }
}
