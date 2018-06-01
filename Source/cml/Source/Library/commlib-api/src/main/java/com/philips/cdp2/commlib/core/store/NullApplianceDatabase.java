/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import android.support.annotation.NonNull;
import com.philips.cdp2.commlib.core.appliance.Appliance;

public class NullApplianceDatabase implements ApplianceDatabase {

    @Override
    public boolean save(@NonNull Appliance appliance) {
        // NOP
        return false;
    }

    @Override
    public void loadDataForAppliance(@NonNull Appliance appliance) {
        // NOP
    }

    @Override
    public boolean delete(@NonNull Appliance appliance) {
        // NOP
        return false;
    }

}
