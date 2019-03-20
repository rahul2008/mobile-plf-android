package com.philips.platform.pim.configration;

import net.openid.appauth.AuthorizationServiceDiscovery;

public class PIMOIDCConfigration extends PIMConfigration {
    private AuthorizationServiceDiscovery authorizationServiceDiscovery;

    public PIMOIDCConfigration(AuthorizationServiceDiscovery authorizationServiceDiscovery) {
        this.authorizationServiceDiscovery = authorizationServiceDiscovery;
    }

}
