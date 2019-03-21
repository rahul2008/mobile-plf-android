package com.philips.platform.pim.manager;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import net.openid.appauth.AuthorizationServiceDiscovery;

class PIMOidcDiscoveryManager implements PIMAuthorizationServiceConfigurationListener {

    private PIMAuthManager pimAuthManager;


    public PIMOidcDiscoveryManager(PIMAuthManager pimAuthManager) {
        this.pimAuthManager = pimAuthManager;

    }

    void downloadOidcUrls(String baseUrl) {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseUrl, this);
    }


    @Override
    public void onSuccess(AuthorizationServiceDiscovery discoveryDoc) {

        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration(discoveryDoc, PIMSettingManager.getInstance().getAppConfigurationInterface());
        PIMSettingManager.getInstance().setPimOidcConfigration(pimoidcConfigration);
    }

    @Override
    public void onError() {
     // TODO: Handle error 
    }
}
