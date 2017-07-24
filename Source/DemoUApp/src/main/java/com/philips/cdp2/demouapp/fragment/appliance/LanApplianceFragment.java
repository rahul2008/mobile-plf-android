/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.appliance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;

public class LanApplianceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cml_fragment_lan_appliance, container, false);

        final Appliance currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();
        final CompoundButton switchEnableCommunication = ((CompoundButton) rootView.findViewById(R.id.cml_switch_enable_communication));

        CompoundButton.OnCheckedChangeListener communicationCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (currentAppliance == null) {
                    return;
                }

                if (isChecked) {
                    currentAppliance.enableCommunication();
                } else {
                    currentAppliance.disableCommunication();
                }
            }
        };

        if (switchEnableCommunication.isChecked()) {
            currentAppliance.enableCommunication();
        } else {
            currentAppliance.disableCommunication();
        }

        switchEnableCommunication.setOnCheckedChangeListener(communicationCheckedChangeListener);

        return rootView;
    }
}
