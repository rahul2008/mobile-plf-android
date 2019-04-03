package com.philips.platform.pim.manager;

import android.util.Log;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import net.openid.appauth.AuthorizationServiceDiscovery;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public  class PIMOidcDiscoveryManager implements PIMAuthorizationServiceConfigurationListener {

    private PIMAuthManager pimAuthManager;
    private final String TAG = PIMOidcDiscoveryManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;


    public PIMOidcDiscoveryManager(PIMAuthManager pimAuthManager) {
        this.pimAuthManager = pimAuthManager;
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    public void downloadOidcUrls(String baseUrl) {
        mLoggingInterface.log(DEBUG,TAG,"downloadOidcUrls called with baseUrl : "+baseUrl);
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
        Log.i(TAG,"onError");
    }
}
