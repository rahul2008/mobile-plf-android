package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

import net.openid.appauth.AuthorizationServiceConfiguration;

/**
 * Callback interface for Authorization service configuration
 */
public interface PIMAuthServiceConfigListener {

    void onAuthServiceConfigSuccess(AuthorizationServiceConfiguration authServiceConfig);

    void onAuthServiceConfigFailed(Error error);

}
