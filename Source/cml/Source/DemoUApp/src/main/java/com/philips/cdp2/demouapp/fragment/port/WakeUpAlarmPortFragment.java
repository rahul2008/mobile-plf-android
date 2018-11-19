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
import com.philips.cdp2.demouapp.appliance.brighteyes.BrightEyesAppliance;
import com.philips.cdp2.demouapp.port.brighteyes.WakeUpAlarmPort;
import com.philips.cdp2.demouapp.port.brighteyes.WakeUpAlarmPortProperties;

import java.util.Locale;

import static com.philips.cdp2.demouapp.fragment.ApplianceFragmentFactory.APPLIANCE_KEY;

public class WakeUpAlarmPortFragment extends Fragment {

    private static final String TAG = "WakeUpAlarmPortFragment";

    private WakeUpAlarmPort wakeUpAlarmPort;
    private Switch enableAlarmSwitch;
    private DICommPortListener<WakeUpAlarmPort> portListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.cml_fragment_wakeup, container, false);

        final String cppId = getArguments().getString(APPLIANCE_KEY);
        Appliance appliance = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager().findApplianceByCppId(cppId);
        if (appliance != null && appliance instanceof BrightEyesAppliance) {
            wakeUpAlarmPort = ((BrightEyesAppliance) appliance).getWakeUpAlarmPort();
        }

        enableAlarmSwitch = rootview.findViewById(R.id.cml_switchWakeup);

        ((CompoundButton) rootview.findViewById(R.id.cml_switchWakeupSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (wakeUpAlarmPort == null) {
            getFragmentManager().popBackStack();
            return;
        }

        portListener = new DICommPortListener<WakeUpAlarmPort>() {
            @Override
            public void onPortUpdate(WakeUpAlarmPort port) {
                if (isAdded()) {
                    WakeUpAlarmPortProperties portProperties = port.getPortProperties();
                    if (portProperties != null) {
                        enableAlarmSwitch.setChecked(portProperties.isEnabled());
                    }
                }
            }

            @Override
            public void onPortError(WakeUpAlarmPort port, Error error, @Nullable String errorData) {
                DICommLog.e(TAG, String.format(Locale.US, "WakeUpAlarmPort error: [%s], data: [%s]", error.getErrorMessage(), errorData));
            }
        };
        wakeUpAlarmPort.addPortListener(portListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (wakeUpAlarmPort != null) {
            wakeUpAlarmPort.removePortListener(portListener);
        }
    }

    private final CompoundButton.OnCheckedChangeListener subscriptionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (wakeUpAlarmPort == null) {
                return;
            }

            if (isChecked) {
                wakeUpAlarmPort.subscribe();
            } else {
                wakeUpAlarmPort.unsubscribe();
            }
        }
    };
}
