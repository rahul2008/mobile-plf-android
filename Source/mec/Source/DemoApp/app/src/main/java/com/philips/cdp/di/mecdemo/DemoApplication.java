package com.philips.cdp.di.mecdemo;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.squareup.leakcanary.LeakCanary;

public class DemoApplication extends Application{
    final String UR = "UserRegistration";
    private AppInfra mAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();

        UIDHelper.injectCalligraphyFonts();
        getTheme().applyStyle(R.style.Theme_DLS_Blue_UltraLight, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
        mAppInfra = new AppInfra.Builder().build(getApplicationContext());
        // HSDPConfiguration();
        initRegistration(Configuration.STAGING);
        RLog.enableLogging();
    }

    public void initRegistration(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        if(mAppInfra == null){
            mAppInfra = new AppInfra.Builder().build(this);
        }
        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();


        URDependancies urDependancies = new URDependancies(mAppInfra);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

        RLog.enableLogging();

    }


    private void HSDPConfiguration() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        mAppInfra.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_APPLICATION_NAME,
                URConfigurationConstants.UR,
                "uGrow",
                configError);
        mAppInfra.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_SECRET,
                URConfigurationConstants.UR,
                "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                configError);
        mAppInfra.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_SHARED,
                URConfigurationConstants.UR,
                "e95f5e71-c3c0-4b52-8b12-ec297d8ae960",
                configError);
        mAppInfra.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_BASE_URL,
                URConfigurationConstants.UR,
                "https://ugrow-ds-staging.eu-west.philips-healthsuite.com",
                configError);
    }

    public AppInfra getAppInfra() {
        return mAppInfra;
    }

//    public void initRegistration(Configuration configuration) {
//        AppConfigurationInterface.AppConfigurationError configError = new
//                AppConfigurationInterface.AppConfigurationError();
//        if (mAppInfra == null) {
//            mAppInfra = new AppInfra.Builder().build(this);
//        }
//        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
//        editor.putString("reg_environment", configuration.getValue());
//        editor.apply();
//        initAppIdentity(configuration);
//        URDependancies urDependancies = new URDependancies(mAppInfra);
//        URSettings urSettings = new URSettings(this);
//        URInterface urInterface = new URInterface();
//        urInterface.init(urDependancies, urSettings);
//
//        RLog.enableLogging();
//    }

    public void initHSDP(Configuration configuration) {
        if (mAppInfra == null) {
            mAppInfra = new AppInfra.Builder().build(this);
        }
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        //store hsdp last envoronment
        HSDPInfo hsdpInfo;
        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        switch (configuration) {
            case EVALUATION:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_APPLICATION_NAME,
                        URConfigurationConstants.UR,
                        "uGrow",
                        configError);

                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SECRET,
                        URConfigurationConstants.UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);

                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SHARED,
                        URConfigurationConstants.UR,
                        "e95f5e71-c3c0-4b52-8b12-ec297d8ae960",
                        configError);

                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_BASE_URL,
                        URConfigurationConstants.UR,
                        "https://ugrow-ds-staging.eu-west.philips-healthsuite.com",
                        configError);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.apply();
                break;
            case DEVELOPMENT:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_APPLICATION_NAME,
                        URConfigurationConstants.UR,
                        "uGrow",
                        configError);

                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SECRET,
                        URConfigurationConstants.UR,
                        "c623685e-f02c-11e5-9ce9-5e5517507c66",
                        configError);

                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SHARED,
                        URConfigurationConstants.UR,
                        "c62362a0-f02c-11e5-9ce9-5e5517507c66",
                        configError);

                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_BASE_URL,
                        URConfigurationConstants.UR,
                        "https://ugrow-ds-development.cloud.pcftest.com",
                        configError);
                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.apply();

                break;
            case PRODUCTION:
                SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").apply();
                break;
            case STAGING:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_APPLICATION_NAME,
                        URConfigurationConstants.UR,
                        "uGrow",
                        configError);
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SECRET,
                        URConfigurationConstants.UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SHARED,
                        URConfigurationConstants.UR,
                        "e95f5e71-c3c0-4b52-8b12-ec297d8ae960",
                        configError);
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_BASE_URL,
                        URConfigurationConstants.UR,
                        "https://ugrow-ds-staging.eu-west.philips-healthsuite.com",
                        configError);
                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.apply();

                break;
            case TESTING:
                prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").apply();
                break;
        }


    }

    public static final String SERVICE_DISCOVERY_TAG = "ServiceDiscovery";
    final String AI = "appinfra";
}
