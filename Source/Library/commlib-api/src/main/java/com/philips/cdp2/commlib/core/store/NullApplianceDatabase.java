/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import com.philips.cdp2.commlib.core.appliance.Appliance;

public class NullApplianceDatabase implements ApplianceDatabase {

    @Override
    public long save(Appliance appliance) {
        // NOP
        return 0;
    }

    @Override
    public void loadDataForAppliance(Appliance appliance) {
        // NOP
    }

    @Override
    public int delete(Appliance appliance) {
        // NOP
        return 0;
    }

}
