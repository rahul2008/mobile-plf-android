package com.philips.cdp.registration.app.infra;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import static com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface.AppConfigurationError;

public class AppInfraWrapper {

    private static final String GROUP_APP_INFRA = "appinfra";
    private static final String GROUP_USER_REGISTRATION = "UserRegistration";

    private final AppInfraInterface appInfra;
    private final AppConfigurationError error;
    private final AppConfigurationInterface appConfigurationInterface;


    public AppInfraWrapper(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
        appConfigurationInterface = appInfra.getConfigInterface();
        error = new AppConfigurationError();
    }

    public Object getAppInfraProperty(String key) {
        return getProperty(key, GROUP_APP_INFRA);
    }

    public Object getURProperty(String key) {
        return getProperty(key, GROUP_USER_REGISTRATION);
    }

    private Object getProperty(String key, String group) {
        Object property = appConfigurationInterface.getPropertyForKey(key, group, error);
        return property;
    }
}
