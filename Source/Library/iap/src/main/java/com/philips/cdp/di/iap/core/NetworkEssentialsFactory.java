/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.core;

import com.philips.cdp.di.iap.hybris.HybrisNetworkEssentials;
import com.philips.cdp.di.iap.local.LocalNetworkEssentials;

public class NetworkEssentialsFactory {
    public static int LOAD_LOCAL_DATA = 0;
    public static int LOAD_HYBRIS_DATA = 1;

    public static NetworkEssentials getNetworkEssentials(int requestCode) {
        if (requestCode == LOAD_LOCAL_DATA) {
            return new HybrisNetworkEssentials();
        } else if (requestCode == LOAD_HYBRIS_DATA) {
            return new LocalNetworkEssentials();
        }
        throw new RuntimeException("Request code not supported");
    }
}