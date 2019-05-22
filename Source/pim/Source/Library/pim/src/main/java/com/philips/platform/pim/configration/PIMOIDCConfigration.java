package com.philips.platform.pim.configration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.pim.manager.PIMSettingManager;

import net.openid.appauth.AuthorizationServiceConfiguration;

public class PIMOIDCConfigration {
    private static String TAG = PIMOIDCConfigration.class.getSimpleName();
    private static final String GROUP_PIM = "PIM";
    private static final String CLIENT_ID = "clientId";
    private AuthorizationServiceConfiguration authorizationServiceConfiguration;

    private AppInfraInterface appInfraInterface;

    public PIMOIDCConfigration(){
        this.appInfraInterface = PIMSettingManager.getInstance().getAppInfraInterface();
    }

    public PIMOIDCConfigration(AuthorizationServiceConfiguration authorizationServiceConfiguration) {
        this.authorizationServiceConfiguration = authorizationServiceConfiguration;
        this.appInfraInterface = PIMSettingManager.getInstance().getAppInfraInterface();
    }

    public AuthorizationServiceConfiguration getAuthorizationServiceConfiguration() {
        return authorizationServiceConfiguration;
    }

   public String getClientId() {
        Object obj = getProperty(CLIENT_ID, GROUP_PIM);
        if (obj != null) {
            return (String) obj;
        }
        return null;
    }

    private Object getProperty(String key, String group) {
        //TODO: Deepthi  ( Low ) check impact of cloud config
        AppConfigurationInterface appConfigurationInterface = appInfraInterface.getConfigInterface();
        Object obj = appConfigurationInterface.getPropertyForKey(key, group, new AppConfigurationInterface.AppConfigurationError());
        return obj;
    }
}
