/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.airpurifier.AirPurifier;
import com.philips.cdp2.demouapp.appliance.brighteyes.BrightEyesAppliance;
import com.philips.cdp2.demouapp.appliance.polaris.PolarisAppliance;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;
import com.philips.cdp2.demouapp.appliance.reference.WifiReferenceAppliance;
import com.philips.cdp2.demouapp.fragment.appliance.BleApplianceFragment;
import com.philips.cdp2.demouapp.fragment.appliance.PersistApplianceFragment;
import com.philips.cdp2.demouapp.fragment.port.AirPortFragment;
import com.philips.cdp2.demouapp.fragment.port.DevicePortFragment;
import com.philips.cdp2.demouapp.fragment.port.FirmwareUpgradeFragment;
import com.philips.cdp2.demouapp.fragment.port.PairingFragment;
import com.philips.cdp2.demouapp.fragment.port.PairingPortFragment;
import com.philips.cdp2.demouapp.fragment.port.TimePortFragment;
import com.philips.cdp2.demouapp.fragment.port.WakeUpAlarmPortFragment;

public class ApplianceFragment extends Fragment {

    private Appliance currentAppliance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.cml_fragment_appliance, container, false);

        currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();

        setupFragments();

        return rootview;
    }

    private void setupFragments() {
        if (currentAppliance == null) {
            return;
        }

        addFragment(new DevicePortFragment());
        addFragment(new PersistApplianceFragment());

        if (currentAppliance instanceof AirPurifier) {
            addFragment(new PairingFragment());
            addFragment(new AirPortFragment());
        }

        if (currentAppliance instanceof ReferenceAppliance) {

            if (currentAppliance instanceof WifiReferenceAppliance) {
                addFragment(new PairingPortFragment());
            }

            addFragment(new TimePortFragment());

            if (currentAppliance instanceof BleReferenceAppliance) {
                addFragment(new BleApplianceFragment());
            }

            addFragment(new FirmwareUpgradeFragment());
        }

        if (currentAppliance instanceof BrightEyesAppliance) {
            addFragment(new WakeUpAlarmPortFragment());
        }

        if(currentAppliance instanceof PolarisAppliance) {
            addFragment(new PairingFragment());
        }
        
        addFragment(new PinFragment());
    }

    public void addFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.cml_ports, fragment)
                .commit();
    }
}
