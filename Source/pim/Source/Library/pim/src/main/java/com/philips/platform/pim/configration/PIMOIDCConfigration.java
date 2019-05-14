package com.philips.platform.pim.configration;

import com.philips.platform.appinfra.AppInfraInterface;

import net.openid.appauth.AuthorizationServiceConfiguration;

public class PIMOIDCConfigration {
    private static String TAG = PIMOIDCConfigration.class.getSimpleName();

    private AuthorizationServiceConfiguration authorizationServiceConfiguration;

    private AppInfraInterface appInfraInterface;

    public PIMOIDCConfigration(AuthorizationServiceConfiguration authorizationServiceConfiguration, AppInfraInterface appInfraInterface) {
        this.authorizationServiceConfiguration = authorizationServiceConfiguration;
        this.appInfraInterface = appInfraInterface;
    }

    //TODO: Note once saved AuthState, do we need to populate PIMOIDCConfigration class through AuthState
    public AuthorizationServiceConfiguration getAuthorizationServiceConfiguration() {
        return authorizationServiceConfiguration;
    }
}
