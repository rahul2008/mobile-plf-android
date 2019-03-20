package com.philips.platform;

import net.openid.appauth.AuthorizationServiceDiscovery;

public interface PIMListener {
    void onSuccess(AuthorizationServiceDiscovery discoveryDoc);

    void onError();
}
