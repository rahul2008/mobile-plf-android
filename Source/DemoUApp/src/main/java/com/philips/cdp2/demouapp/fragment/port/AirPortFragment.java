/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.airpurifier.AirPurifier;
import com.philips.cdp2.demouapp.port.air.AirPort;
import com.philips.cdp2.demouapp.port.air.AirPortProperties;

public class AirPortFragment extends Fragment {

    private AirPurifier currentAppliance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_airport, container, false);

        final Switch lightSwitch = (Switch) rootview.findViewById(R.id.switchLight);

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                updateLightProperty(isChecked);
            }
        });

        ((CompoundButton) rootview.findViewById(R.id.switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        currentAppliance = (AirPurifier) CurrentApplianceManager.getInstance().getCurrentAppliance();

        currentAppliance.getAirPort().addPortListener(new DICommPortListener() {
            @Override
            public void onPortUpdate(DICommPort port) {
                AirPortProperties airPortProperties = currentAppliance.getAirPort().getPortProperties();
                if (airPortProperties != null) {
                    lightSwitch.setChecked(airPortProperties.getLightOn());
                }
            }

            @Override
            public void onPortError(DICommPort port, Error error, String errorData) {

            }
        });

        return rootview;
    }

    private final CompoundButton.OnCheckedChangeListener subscriptionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (currentAppliance == null) {
                return;
            }

            if (isChecked) {
                currentAppliance.getAirPort().subscribe();
            } else {
                currentAppliance.getAirPort().unsubscribe();
            }
        }
    };

    private void updateLightProperty(final boolean isChecked) {
        AirPort airPort = currentAppliance.getAirPort();

        if (airPort != null) {
            airPort.setLight(isChecked);
        }
    }
}
