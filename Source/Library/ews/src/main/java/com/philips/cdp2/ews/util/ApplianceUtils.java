/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.util;

import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp2.commlib.core.appliance.Appliance;

public final class ApplianceUtils {

    public static boolean isApplianceOnline(@Nullable Appliance appliance) {
        if (appliance == null || appliance.getNetworkNode() == null) {
            return false;
        }
        return appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY;
    }

}

