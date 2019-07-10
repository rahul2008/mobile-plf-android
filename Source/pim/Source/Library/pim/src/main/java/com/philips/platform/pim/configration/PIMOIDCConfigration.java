package com.philips.platform.pim.configration;

import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.utilities.UserCustomClaims;

import net.openid.appauth.AuthorizationServiceConfiguration;

import java.util.Map;

/**
 * Responsible for providing configuration required for OIDC.
 */
public class PIMOIDCConfigration {
    private static final String USR_CLIENT_ID_KEY = "JanRainConfiguration.RegistrationClientID.";
    private static String TAG = PIMOIDCConfigration.class.getSimpleName();
    private static final String GROUP_PIM = "PIM";
    private static final String CLIENT_ID = "clientId";
    private static final String MIGRATION_CLIENT_ID = "clientId";
    private static final String REDIRECT_URL = "redirectURL";
    private static final String RS_ID = "rsid";
    private AuthorizationServiceConfiguration authorizationServiceConfiguration;

    private AppInfraInterface appInfraInterface;

    public PIMOIDCConfigration() {
        this.appInfraInterface = PIMSettingManager.getInstance().getAppInfraInterface();
    }

    public PIMOIDCConfigration(AuthorizationServiceConfiguration authorizationServiceConfiguration) {
        this.authorizationServiceConfiguration = authorizationServiceConfiguration;
        this.appInfraInterface = PIMSettingManager.getInstance().getAppInfraInterface();
    }

    public AuthorizationServiceConfiguration getAuthorizationServiceConfiguration() {
        return authorizationServiceConfiguration;
    }

    /**
     * Fetch client id from App config
     *
     * @return client id
     */
    public String getClientId() {
        Map<String, String> defaultConfig = getDefaultConfiguraton();
        if (defaultConfig != null)
            return defaultConfig.get(CLIENT_ID);
        return null;
    }

    public String getMigrationClientId() {
        Map<String, String> migrationConfig = getMigrationConfiguraton();
        if (migrationConfig != null)
            return migrationConfig.get(CLIENT_ID);
        return null;
    }

    public String getRedirectUrl() {
        Map<String, String> defaultConfig = getDefaultConfiguraton();
        if (defaultConfig != null)
            return defaultConfig.get(REDIRECT_URL);
        return null;
    }

    public String getMigrationRedirectUrl() {
        Map<String, String> migrationConfig = getMigrationConfiguraton();
        if (migrationConfig != null)
            return migrationConfig.get(REDIRECT_URL);
        return null;
    }

    /**
     * Fetch rsid from App config
     *
     * @return rsid
     */
    public String getrsID() {
        Object obj = getProperty(RS_ID, GROUP_PIM);
        if (obj != null) {
            return (String) obj;
        }
        return null;
    }

    private Map<String, String> getDefaultConfiguraton() {
        Object obj = getProperty("PIM.default", GROUP_PIM);
        if (obj != null && obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }

    private Map<String, String> getMigrationConfiguraton() {
        Object obj = getProperty("PIM.migration", GROUP_PIM);
        if (obj != null && obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }

    private Object getProperty(String key, String group) {
        //TODO: Deepthi  ( Low ) check impact of cloud config
        AppConfigurationInterface appConfigurationInterface = appInfraInterface.getConfigInterface();
        Object obj = appConfigurationInterface.getPropertyForKey(key, group, new AppConfigurationInterface.AppConfigurationError());
        return obj;
    }

    public String getCustomClaims() {
        JsonObject customClaimObject = new JsonObject();
        customClaimObject.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL_CONSENT, null);
        customClaimObject.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL_TIMESTAMP, null);
        customClaimObject.add(UserCustomClaims.SOCIAL_PROFILES, null);
        customClaimObject.add(UserCustomClaims.UUID, null);

        JsonObject userInfo = new JsonObject();
        userInfo.add("userinfo", customClaimObject);
        PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "PIM_KEY_CUSTOM_CLAIMS: " + userInfo.toString());
        return userInfo.toString();
    }
}
