/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.networkEssential;

public class NetworkEssentialsFactory {
    public static NetworkEssentials getNetworkEssentials(boolean isPlanB) {
        if (isPlanB) {
            return new LocalNetworkEssentials();
        } else {
            return new HybrisNetworkEssentials();
        }
    }
}