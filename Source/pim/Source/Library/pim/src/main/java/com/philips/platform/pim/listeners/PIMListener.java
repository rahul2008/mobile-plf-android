package com.philips.platform.pim.listeners;

import net.openid.appauth.AuthorizationServiceDiscovery;

public interface PIMListener {
    void onSuccess(AuthorizationServiceDiscovery discoveryDoc);

    void onError();
}
