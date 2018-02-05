/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.thsdemolaunch;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.ArrayList;

public class THSDemoApplication extends Application {
    public AppInfraInterface appInfra;

 //   public static AppInfraInterface appInfra;
    public static LoggingInterface loggingInterface;
    private AppConfigurationInterface.AppConfigurationError configError;

    final String AI = "appinfra";

    /***
     * Initializar app infra
     *
     */

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        UIDHelper.injectCalligraphyFonts();
        AmwellLog.enableLogging(true);
        initAppInfra();
    }

    private void initAppInfra() {
        initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
            @Override
            public void onAppInfraInitialization() {
                initializeUserRegistrationLibrary(Configuration.STAGING);
                initHSDP();
            }
        });
    }

    public void initializeAppInfra(AppInitializationCallback.AppInfraInitializationCallback appInfraInitializationCallback) {
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging();
        appInfraInitializationCallback.onAppInfraInitialization();
    }

    /**
     * For doing dynamic initialisation Of User registration
     *
     * @param configuration The environment ype as required by UR
     */
    public void initializeUserRegistrationLibrary(Configuration configuration) {
        final String UR = "UserRegistration";

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        appInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.DEVELOPMENT
                , UR,
                "8kaxdrpvkwyr7pnp987amu4aqb4wmnte",
                configError);
        appInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.TESTING
                , UR,
                "g52bfma28yjbd24hyjcswudwedcmqy7c",
                configError);
        appInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.EVALUATION
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        appInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.STAGING
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        appInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.PRODUCTION
                , UR,
                "9z23k3q8bhqyfwx78aru6bz8zksga54u",
                configError);


        appInfra.getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "MicrositeID",
                UR,
                "77000",
                configError);
        appInfra.getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "RegistrationEnvironment",
                UR,
                configuration.getValue(),
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey("Flow." +
                        "EmailVerificationRequired",
                UR,
                "" + true,
                configError);
        appInfra.
                getConfigInterface().setPropertyForKey("Flow." +
                        "TermsAndConditionsAcceptanceRequired",
                UR,
                "" + true,
                configError);

        String minAge = "{ \"NL\":12 ,\"GB\":0,\"default\": 16}";
        appInfra.
                getConfigInterface().setPropertyForKey("Flow." +
                        "MinimumAgeLimit",
                UR,
                minAge,
                configError);

        ArrayList<String> providers = new ArrayList<>();
        providers.add("facebook");
        providers.add("googleplus");
        providers.add("sinaweibo");
        providers.add("qq");
        appInfra.
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "NL",
                UR,
                providers,
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "US",
                UR,
                providers,
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "default",
                UR,
                providers,
                configError);


        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.apply();

        URDependancies urDependancies = new URDependancies(appInfra);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

    }


    public void initHSDP() {
        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.ApplicationName",
                "UserRegistration",
                "OneBackend",
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Secret",
                "UserRegistration",
                "f5b62a26d680e5ae8001522a8e3268f966545a1a14a47ea2040793ea825484cd12fce9c46b43e2c2604cb836db64362a0c8b39eb7b162b8b3e83740143337eda",
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Shared",
                "UserRegistration",
                "f52cd90d-c955-43e1-8380-999e03d0d4c0",
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.BaseURL",
                "UserRegistration",
                "https://user-registration-assembly-staging.eu-west.philips-healthsuite.com",
                configError);

    }
}