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

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;

public class BleApplianceFragment extends Fragment {

    private Appliance currentAppliance;
    private CompoundButton switchContinuousConnection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cml_fragment_ble_appliance, container, false);

        currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();

        switchContinuousConnection = ((CompoundButton) rootView.findViewById(R.id.cml_switch_continuous_connection));
        switchContinuousConnection.setChecked(true);
        handleContinuousConnectionChanged();

        switchContinuousConnection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                handleContinuousConnectionChanged();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        handleContinuousConnectionChanged();
    }

    private void handleContinuousConnectionChanged() {
        if (currentAppliance == null) {
            return;
        }

        if (switchContinuousConnection.isChecked()) {
            currentAppliance.enableCommunication();
        } else {
            currentAppliance.disableCommunication();
        }
    }
}
