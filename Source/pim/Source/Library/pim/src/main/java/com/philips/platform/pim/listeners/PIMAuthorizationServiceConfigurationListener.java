package com.philips.platform.pim.listeners;

import net.openid.appauth.AuthorizationServiceConfiguration;

public interface PIMAuthorizationServiceConfigurationListener {


    void onError(String errorMessage);

    void onSuccess(AuthorizationServiceConfiguration authorizationServiceConfiguration);
}
