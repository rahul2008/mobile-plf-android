/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.networkEssential;

import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

public class NetworkEssentialsFactory {
    public static NetworkEssentials getNetworkEssentials(boolean isPlanB, UserDataInterface userDataInterface) {
        if (isPlanB) {
            return new LocalNetworkEssentials();
        } else {
            return new HybrisNetworkEssentials(userDataInterface);
        }
    }
}