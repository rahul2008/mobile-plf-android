/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.example;

import com.philips.cdp2.commlib.example.appliance.ReferenceAppliance;

import static com.philips.cdp2.commlib.example.appliance.ReferenceAppliance.CPPID;

public class BleReferenceApplianceActivity extends ReferenceApplianceActivity {

    @Override
    protected ReferenceAppliance getCurrentAppliance() {
        final String cppId = getIntent().getStringExtra(CPPID);
        final ReferenceAppliance appliance = (ReferenceAppliance) ((App) getApplication()).getCommCentral().getApplianceManager().findApplianceByCppId(cppId);

        return appliance;
    }
}
