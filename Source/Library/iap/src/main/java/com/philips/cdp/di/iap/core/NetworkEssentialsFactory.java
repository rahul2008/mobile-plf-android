/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import com.philips.cdp.di.iap.hybris.HybrisNetworkEssentials;
import com.philips.cdp.di.iap.applocal.LocalNetworkEssentials;

public class NetworkEssentialsFactory {
    public static NetworkEssentials getNetworkEssentials(boolean isPlanB) {
        if (isPlanB) {
            return new LocalNetworkEssentials();
        } else {
            return new HybrisNetworkEssentials();
        }
    }
}