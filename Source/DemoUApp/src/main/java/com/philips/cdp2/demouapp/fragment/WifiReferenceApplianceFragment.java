/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;

public class WifiReferenceApplianceFragment extends ReferenceApplianceFragment {

    @Override
    protected ReferenceAppliance getCurrentAppliance() {
        return (ReferenceAppliance) CurrentApplianceManager.getInstance().getCurrentAppliance();
    }
}
