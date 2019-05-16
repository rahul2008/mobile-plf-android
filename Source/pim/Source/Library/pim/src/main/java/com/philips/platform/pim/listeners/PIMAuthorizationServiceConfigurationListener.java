package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

import net.openid.appauth.AuthorizationServiceConfiguration;

public interface PIMAuthorizationServiceConfigurationListener {

    void onAuthorizationServiceConfigurationSuccess(AuthorizationServiceConfiguration authorizationServiceConfiguration);

    void onAuthorizationServiceConfigurationFailed(Error error);

}
