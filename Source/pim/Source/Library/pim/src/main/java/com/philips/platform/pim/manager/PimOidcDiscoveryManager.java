package com.philips.platform.pim.manager;

import com.philips.platform.pim.models.OIDCConfig;
import com.philips.platform.pim.rest.PimListener;
import com.philips.platform.pim.utilities.PimStorageUtility;

public class PimOidcDiscoveryManager {

    private PimStorageUtility pimStorageUtility;
    private PimAuthManager pimAuthManager;

    public PimOidcDiscoveryManager(PimAuthManager pimAuthManager, PimStorageUtility pimStorageUtility) {
        this.pimStorageUtility = pimStorageUtility;
        this.pimAuthManager = pimAuthManager;
    }

     void downloadOidcUrls(String baseUrl, PimListener listener) {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseUrl);
    }

     void storeDiscoveryConfig(OIDCConfig oidcConfig, PimListener listener) {
        pimStorageUtility.storeOidcConfigration(oidcConfig, listener);
    }


}
