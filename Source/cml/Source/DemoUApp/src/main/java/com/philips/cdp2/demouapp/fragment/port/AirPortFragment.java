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
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.appliance.airpurifier.AirPurifier;
import com.philips.cdp2.demouapp.port.air.AirPort;
import com.philips.cdp2.demouapp.port.air.AirPortProperties;

import java.util.Locale;

import static com.philips.cdp2.demouapp.fragment.ApplianceFragmentFactory.APPLIANCE_KEY;

public class AirPortFragment extends Fragment {

    private static final String TAG = "AirPortFragment";

    private AirPort airPort;
    private Switch lightSwitch;
    private DICommPortListener<AirPort<AirPortProperties>> portListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.cml_fragment_airport, container, false);

        final String cppId = getArguments().getString(APPLIANCE_KEY);
        Appliance appliance = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager().findApplianceByCppId(cppId);
        if (appliance != null && appliance instanceof AirPurifier) {
            airPort = ((AirPurifier) appliance).getAirPort();
        }

        lightSwitch = rootview.findViewById(R.id.cml_switchLight);

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                updateLightProperty(isChecked);
            }
        });

        ((CompoundButton) rootview.findViewById(R.id.cml_switchAirportSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (airPort == null) {
            getFragmentManager().popBackStack();
            return;
        }

        portListener = new DICommPortListener<AirPort<AirPortProperties>>() {
            @Override
            public void onPortUpdate(AirPort<AirPortProperties> port) {
                if (isAdded()) {
                    AirPortProperties airPortProperties = port.getPortProperties();
                    if (airPortProperties != null) {
                        lightSwitch.setChecked(airPortProperties.getLightOn());
                    }
                }
            }

            @Override
            public void onPortError(AirPort<AirPortProperties> port, Error error, @Nullable String errorData) {
                DICommLog.e(TAG, String.format(Locale.US, "Air port error: [%s], data: [%s]", error.getErrorMessage(), errorData));
            }
        };
        airPort.addPortListener(portListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (airPort != null) {
            airPort.removePortListener(portListener);
        }
    }

    private final CompoundButton.OnCheckedChangeListener subscriptionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (airPort == null) {
                return;
            }

            if (isChecked) {
                airPort.subscribe();
            } else {
                airPort.unsubscribe();
            }
        }
    };

    private void updateLightProperty(final boolean isChecked) {
        if (airPort == null) {
            return;
        }
        airPort.setLight(isChecked);
    }
}
