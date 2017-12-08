/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
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

import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;


public class PairingPortFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.cml_fragment_port_pairing, container, false);

        rootview.findViewById(R.id.cml_button_pair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CurrentApplianceManager.getInstance().getCurrentAppliance().getPairingPort().triggerPairing(
                        getTextFor(R.id.cml_client_provider, rootview),
                        getTextFor(R.id.cml_client_type, rootview),
                        getTextFor(R.id.cml_client_id, rootview),
                        getTextFor(R.id.cml_secret_key, rootview),
                        getTextFor(R.id.cml_type, rootview),
                        getTextFor(R.id.cml_permissions, rootview).split(",")
                );
            }
        });

        return rootview;
    }

    private String getTextFor(int id, View fragmentView) {
        return ((EditText) fragmentView.findViewById(id)).getText().toString();
    }
}
