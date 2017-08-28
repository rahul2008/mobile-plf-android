/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import com.philips.cdp2.commlib.core.appliance.Appliance;

public class NullApplianceDatabase implements ApplianceDatabase {

    @Override
    public boolean save(Appliance appliance) {
        // NOP
        return false;
    }

    @Override
    public void loadDataForAppliance(Appliance appliance) {
        // NOP
    }

    @Override
    public boolean delete(Appliance appliance) {
        // NOP
        return false;
    }

}
