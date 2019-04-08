package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import net.openid.appauth.AuthorizationServiceDiscovery;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public  class PIMOidcDiscoveryManager implements PIMAuthorizationServiceConfigurationListener {

    private final String TAG = PIMOidcDiscoveryManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;


    public PIMOidcDiscoveryManager() {
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    public void downloadOidcUrls(String baseUrl) {
        mLoggingInterface.log(DEBUG,TAG,"downloadOidcUrls called with baseUrl : "+baseUrl);
        PIMAuthManager pimAuthManager = new PIMAuthManager();
        pimAuthManager.fetchAuthWellKnownConfiguration(baseUrl, this);
    }

    @Override
    public void onSuccess(AuthorizationServiceDiscovery discoveryDoc) {
        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration : onSuccess. discoveryDoc : "+discoveryDoc);
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration(discoveryDoc, PIMSettingManager.getInstance().getAppInfraInterface());
        PIMSettingManager.getInstance().setPimOidcConfigration(pimoidcConfigration);
    }

    @Override
    public void onError() {
        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration : onError.  ");

    }
}
