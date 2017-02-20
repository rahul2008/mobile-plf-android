package com.philips.cdp.registration.appconfig;


import android.support.annotation.Nullable;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class AppConfig {

    @Nullable
    public static String getResetPasswordClientId() {
        String clientId;
        String env = RegistrationConfiguration.getInstance().getRegistrationEnvironment();
        if (env.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.PRODUCTION.getValue())) {
            clientId = RegConstants.RESET_PASSWORD_SMS_CLIENT_ID_PROD;
            return clientId;
        }
        if (env.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.STAGING.getValue())) {
            clientId = RegConstants.RESET_PASSWORD_SMS_CLIENT_ID_STAG;
            return clientId;
        }
        if (env.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.TESTING.getValue())) {
            clientId = RegConstants.RESET_PASSWORD_SMS_CLIENT_ID_TEST;
            return clientId;
        }
        return null;
    }

    @Nullable
    public static String getResetPasswordRedirectUri() {
        String redirectUri;
        String env = RegistrationConfiguration.getInstance().getRegistrationEnvironment();
        if (env.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.PRODUCTION.getValue())) {
            redirectUri = RegConstants.PROD_RESET_PASSWORD_SMS_REDIRECT_URI;
            return redirectUri;
        }
        if (env.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.STAGING.getValue())) {
            redirectUri = RegConstants.STAGE_RESET_PASSWORD_SMS_REDIRECT_URI;
            return redirectUri;
        }
        if (env.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.TESTING.getValue())) {
            redirectUri = RegConstants.TEST_RESET_PASSWORD_SMS_REDIRECT_URI;
            return redirectUri;
        }
        return null;
    }
}
