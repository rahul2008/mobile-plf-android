package com.philips.platform.pim.listeners;

import net.openid.appauth.AuthorizationServiceDiscovery;

public interface PIMAuthorizationServiceConfigurationListener {


    void onError();

    void onSuccess(AuthorizationServiceDiscovery authorizationServiceDiscovery);
}
