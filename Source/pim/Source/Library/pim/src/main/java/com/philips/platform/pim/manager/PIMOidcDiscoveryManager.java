package com.philips.platform.pim.manager;

import android.util.Log;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import net.openid.appauth.AuthorizationServiceDiscovery;

public  class PIMOidcDiscoveryManager implements PIMAuthorizationServiceConfigurationListener {

    private PIMAuthManager pimAuthManager;
    private static  final String TAG = PIMOidcDiscoveryManager.class.getSimpleName();


    public PIMOidcDiscoveryManager(PIMAuthManager pimAuthManager) {
        this.pimAuthManager = pimAuthManager;

    }

    public void downloadOidcUrls(String baseUrl) {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseUrl, this);
    }


    @Override
    public void onSuccess(AuthorizationServiceDiscovery discoveryDoc) {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration(discoveryDoc, PIMSettingManager.getInstance().getAppInfraInterface());
        PIMSettingManager.getInstance().setPimOidcConfigration(pimoidcConfigration);
    }

    @Override
    public void onError() {
        Log.i(TAG,"onError");
    }
}
