package com.philips.platform.pim.manager;

import com.philips.platform.pim.models.OIDCConfig;
import com.philips.platform.pim.rest.PIMListener;
import com.philips.platform.pim.utilities.PIMStorageUtility;

public class PIMOidcDiscoveryManager {

    private PIMStorageUtility pimStorageUtility;
    private PIMAuthManager pimAuthManager;

    public PIMOidcDiscoveryManager(PIMAuthManager pimAuthManager, PIMStorageUtility pimStorageUtility) {
        this.pimStorageUtility = pimStorageUtility;
        this.pimAuthManager = pimAuthManager;
    }

     void downloadOidcUrls(String baseUrl, PIMListener listener) {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseUrl);
    }

     void storeDiscoveryConfig(OIDCConfig oidcConfig, PIMListener listener) {
        pimStorageUtility.storeOidcConfigration(oidcConfig, listener);
    }


}
