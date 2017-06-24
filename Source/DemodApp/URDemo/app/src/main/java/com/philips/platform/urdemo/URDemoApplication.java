package com.philips.platform.urdemo;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.LocaleList;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.uappframework.UappInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

public class URDemoApplication extends Application {

    private AppInfraInterface appInfraInterface;

    private static final String CHINA_CODE = "CN";
    private static final String DEFAULT = "default";
    private static final String URL_ENCODING = "UTF-8";

    @Override
    public void onCreate() {
        super.onCreate();

        appInfraInterface = new AppInfra.Builder().build(this);
        UappInterface standardRegistrationInterface = new URDemouAppInterface();
        standardRegistrationInterface.init(new URDemouAppDependencies(appInfraInterface), new URDemouAppSettings(this));

        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {
            String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
            if (restoredHSDPText != null && restoredHSDPText.equals(restoredText)) {
                initHSDP(RegUtility.getConfiguration(restoredText));
            }
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {
            initRegistration(Configuration.STAGING);
        }
    }


    public void initRegistration(Configuration configuration) {
        if(appInfraInterface == null){
            appInfraInterface = new AppInfra.Builder().build(this);
        }
        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();


        String languageCode;
        String countryCode;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            languageCode = LocaleList.getDefault().get(0).getLanguage();
            countryCode = LocaleList.getDefault().get(0).getCountry();
        }else{
            languageCode = Locale.getDefault().getLanguage();
            countryCode = Locale.getDefault().getCountry();
        }

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(appInfraInterface);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

        RLog.enableLogging();
    }

    public void initHSDP(Configuration configuration) {
        if(appInfraInterface == null){
            appInfraInterface = new AppInfra.Builder().build(this);
        }
        final AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();


        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        switch (configuration) {
            case EVALUATION:
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_APPLICATION_NAME,
                        UR,
                        "uGrow",
                        configError);

                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_SECRET,
                        UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);

                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_SHARED,
                        UR,
                        "e95f5e71-c3c0-4b52-8b12-ec297d8ae960",
                        configError);

                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_BASE_URL,
                        UR,
                        "https://ugrow-ds-staging.eu-west.philips-healthsuite.com",
                        configError);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case DEVELOPMENT:

                AppConfigurationInterface anInterface = appInfraInterface.getConfigInterface();

                Map<String, String> hsdpAppNames = new HashMap<>();
                hsdpAppNames.put(CHINA_CODE, "CDP");
                hsdpAppNames.put(DEFAULT, "uGrow");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_APPLICATION_NAME,
                        UR, hsdpAppNames, configError);

                Map<String, String> hsdpSecrets = new HashMap<>();
                hsdpSecrets.put(CHINA_CODE, "057b97e0-f9b1-11e6-bc64-92361f002671");
                hsdpSecrets.put(DEFAULT, "c623685e-f02c-11e5-9ce9-5e5517507c66");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SECRET,
                        UR, hsdpSecrets, configError);

                Map<String, String> hsdpSharedIds = new HashMap<>();
                hsdpSharedIds.put(CHINA_CODE, "fe53a854-f9b0-11e6-bc64-92361f002671");
                hsdpSharedIds.put(DEFAULT, "c62362a0-f02c-11e5-9ce9-5e5517507c66");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SHARED,
                        UR, hsdpSharedIds, configError);

                Map<String, String> hsdpBaseUrls = new HashMap<>();
                try {
                    hsdpBaseUrls.put(CHINA_CODE, URLEncoder.encode("https://user-registration-assembly-hsdpchinadev.cn1.philips-healthsuite.com.cn", URL_ENCODING));
                    hsdpBaseUrls.put(DEFAULT, URLEncoder.encode("https://ugrow-ds-development.cloud.pcftest.com", URL_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_BASE_URL,
                        UR, hsdpBaseUrls, configError);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();

                break;
            case PRODUCTION:
                SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
            case STAGING:
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_APPLICATION_NAME,
                        UR,
                        "uGrow",
                        configError);
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_SECRET,
                        UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_SHARED,
                        UR,
                        "fe53a854-f9b0-11e6-bc64-92361f002671",
                        configError);
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        HSDP_CONFIGURATION_BASE_URL,
                        UR,
                        "https://ugrow-ds-staging.eu-west.philips-healthsuite.com",
                        configError);
                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();

                break;
            case TESTING:
                prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
        }
    }

    final String AI = "appinfra";
    private void initAppIdentity(Configuration configuration) {

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        appInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        appInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);


        switch (configuration) {
            case EVALUATION:
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                appInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "TEST",
                        configError);
                break;
        }
    }
}
