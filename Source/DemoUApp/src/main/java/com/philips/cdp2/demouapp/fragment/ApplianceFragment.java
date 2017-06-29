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

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.airpurifier.AirPurifier;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;
import com.philips.cdp2.demouapp.fragment.appliance.LanApplianceFragment;
import com.philips.cdp2.demouapp.fragment.port.AirPortFragment;
import com.philips.cdp2.demouapp.fragment.port.DevicePortFragment;
import com.philips.cdp2.demouapp.fragment.port.FirmwareUpgradeFragment;
import com.philips.cdp2.demouapp.fragment.port.PairingFragment;
import com.philips.cdp2.demouapp.fragment.port.PairingPortFragment;
import com.philips.cdp2.demouapp.fragment.port.TimePortFragment;

public class ApplianceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_appliance, container, false);

        Appliance currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();

        addFragment(new LanApplianceFragment());
        addFragment(new DevicePortFragment());

        if (currentAppliance instanceof AirPurifier) {
            addFragment(new PairingFragment());
            addFragment(new AirPortFragment());
        }

        if (currentAppliance instanceof ReferenceAppliance) {
            addFragment(new PairingPortFragment());
            addFragment(new TimePortFragment());
            addFragment(new FirmwareUpgradeFragment());
        }

        return rootview;
    }

    public void addFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.ports, fragment)
                .commit();
    }
}
