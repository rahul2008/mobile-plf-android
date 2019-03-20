package com.philips.platform.pim.configration;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.pim.R;

import net.openid.appauth.AuthorizationServiceDiscovery;

public class PIMOIDCConfigration extends PIMConfigration {


    private AuthorizationServiceDiscovery authorizationServiceDiscovery;

    private AppConfigurationInterface appConfigurationInterface;

    public PIMOIDCConfigration(AuthorizationServiceDiscovery authorizationServiceDiscovery) {
        this.authorizationServiceDiscovery = authorizationServiceDiscovery;
        this.appConfigurationInterface = appConfigurationInterface;
    }

    //TODO: Note once saved AuthState, do we need to populate PIMOIDCConfigration class through AuthState
    public AuthorizationServiceDiscovery getAuthorizationServiceDiscovery() {
        return authorizationServiceDiscovery;
    }

    // TODO: Get appinfra via settings manager or create constructor to inject what is required
    String getClientId() {
        return "Client Id";
    }

    int getRedirectURI(){
        return R.string.redirectURL;
    }

}
