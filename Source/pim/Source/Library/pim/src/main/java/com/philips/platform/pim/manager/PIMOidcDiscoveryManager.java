package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import net.openid.appauth.AuthorizationServiceConfiguration;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public  class PIMOidcDiscoveryManager implements PIMAuthorizationServiceConfigurationListener {

    private final String TAG = PIMOidcDiscoveryManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;


    PIMOidcDiscoveryManager() {
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    void downloadOidcUrls(String baseUrl) {
        mLoggingInterface.log(DEBUG,TAG,"downloadOidcUrls called with baseUrl : "+baseUrl);
        PIMAuthManager pimAuthManager = new PIMAuthManager();
        pimAuthManager.fetchAuthWellKnownConfiguration(baseUrl, this);
    }

    @Override
    public void onAuthorizationServiceConfigurationSuccess(AuthorizationServiceConfiguration authorizationServiceConfiguration) {
        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration : onLoginSuccess. authorizationServiceConfiguration : "+authorizationServiceConfiguration);
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration(authorizationServiceConfiguration, PIMSettingManager.getInstance().getAppInfraInterface());
        PIMSettingManager.getInstance().setPimOidcConfigration(pimoidcConfigration);
    }

    @Override
    public void onAuthorizationServiceConfigurationFailed(Error error) {
        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration : onLoginFailed :  "+error.getErrDesc());
    }
}
