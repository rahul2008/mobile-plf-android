/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclientsample.reference.ReferenceAppliance;

public class WifiReferenceApplianceActivity extends ReferenceApplianceActivity {

    @Override
    protected ReferenceAppliance getCurrentAppliance() {
        return (ReferenceAppliance) CurrentApplianceManager.getInstance().getCurrentAppliance();
    }
}
