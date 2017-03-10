/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.URInterface;

public class AppConfiguration extends BaseConfiguration {

    private static final String REGISTRATION_ENVIRONMENT = "appidentity.appState";
    private static final String MICROSITE_ID_KEY = "appidentity.micrositeId";
    private static final String WE_CHAT_APP_ID_KEY = "weChatAppId";
    private static final String WE_CHAT_APP_SECRET_KEY = "weChatAppSecret";
    private static final String CLIENT_ID_KEY = "JanRainConfiguration.RegistrationClientID.";

    public AppConfiguration() {
        URInterface.getComponent().inject(this);
    }

    public String getWeChatAppId() {
        Object property = appInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY);
        return getConfigPropertyValue(property);
    }

    public String getWeChatAppSecret() {
        Object property = appInfraWrapper.getURProperty(WE_CHAT_APP_SECRET_KEY);
        return getConfigPropertyValue(property);
    }

    public String getMicrositeId() {
        Object property = appInfraWrapper.getAppInfraProperty(MICROSITE_ID_KEY);
        return getConfigPropertyValue(property);
    }

    public String getRegistrationEnvironment() {
        Object property = appInfraWrapper.getAppInfraProperty(REGISTRATION_ENVIRONMENT);
        return getConfigPropertyValue(property);
    }

    public String getClientId(String environment) {
        Object clientIdObject = appInfraWrapper.getURProperty(CLIENT_ID_KEY + environment);
        return getConfigPropertyValue(clientIdObject);
    }

}
