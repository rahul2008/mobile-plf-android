package com.philips.platform.pim.configration;

import android.util.Log;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.pim.R;

import net.openid.appauth.AuthorizationServiceDiscovery;

public class PIMOIDCConfigration {
    private static String TAG = PIMOIDCConfigration.class.getSimpleName();
    private static final String GROUP_PIM = "PIM";
    private static final String CLIENT_ID = "clientId";
    private AuthorizationServiceDiscovery authorizationServiceDiscovery;

    private AppConfigurationInterface appConfigurationInterface;

    public PIMOIDCConfigration(AuthorizationServiceDiscovery authorizationServiceDiscovery, AppConfigurationInterface appConfigurationInterface) {
        this.authorizationServiceDiscovery = authorizationServiceDiscovery;
        this.appConfigurationInterface = appConfigurationInterface;
    }

    //TODO: Note once saved AuthState, do we need to populate PIMOIDCConfigration class through AuthState
    public AuthorizationServiceDiscovery getAuthorizationServiceDiscovery() {
        return authorizationServiceDiscovery;
    }

    // TODO: Get appinfra via settings manager or create constructor to inject what is required
    protected String getClientId() {
        Object obj = getPIMProperty(CLIENT_ID);
        if (obj != null) {
            Log.d(TAG, "getClientId : " + obj);
            return (String) obj;
        }
        return null;
    }


    protected int getRedirectURI() {
        return R.string.redirectURL;
    }

    private Object getPIMProperty(String key) {
        return getProperty(key, GROUP_PIM);
    }

    private Object getProperty(String key, String group) {
        return appConfigurationInterface.getPropertyForKey(key, group, new AppConfigurationInterface.AppConfigurationError());
    }
}
