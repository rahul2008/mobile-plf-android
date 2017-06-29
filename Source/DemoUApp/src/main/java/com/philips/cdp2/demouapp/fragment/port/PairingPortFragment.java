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

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;


public class PairingPortFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_port_pairing, container, false);

        rootview.findViewById(R.id.button_pair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CurrentApplianceManager.getInstance().getCurrentAppliance().getPairingPort().triggerPairing(
                        getTextFor(R.id.client_provider, rootview),
                        getTextFor(R.id.client_type, rootview),
                        getTextFor(R.id.client_id, rootview),
                        getTextFor(R.id.secret_key, rootview),
                        getTextFor(R.id.type, rootview),
                        getTextFor(R.id.permissions, rootview).split(",")
                );
            }
        });

        return rootview;
    }

    private String getTextFor(int id, View fragmentView) {
        return ((EditText) fragmentView.findViewById(id)).getText().toString();
    }
}
