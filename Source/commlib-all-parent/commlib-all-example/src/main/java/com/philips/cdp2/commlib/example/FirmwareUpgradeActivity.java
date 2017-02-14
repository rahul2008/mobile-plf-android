/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.commlib.core.appliance.Appliance;

import java.util.Set;

import static com.philips.cdp2.commlib.example.ApplianceActivity.CPPID;

public class FirmwareUpgradeActivity extends AppCompatActivity {
    private BleReferenceAppliance bleReferenceAppliance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Set<? extends Appliance> availableAppliances = ((App) getApplication()).getCommCentral().getApplianceManager().getAvailableAppliances();
        for (Appliance appliance: availableAppliances) {
            if (appliance.getNetworkNode().getCppId().equals(getIntent().getExtras().getString(CPPID))) {
                bleReferenceAppliance = (BleReferenceAppliance) appliance;
            }
        }

        if (bleReferenceAppliance == null) {
            finish();
        } else {
            getSupportActionBar().setTitle(bleReferenceAppliance.getNetworkNode().getName());

        }
    }
}
