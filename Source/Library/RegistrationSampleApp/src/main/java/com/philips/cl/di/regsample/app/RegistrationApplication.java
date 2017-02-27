
package com.philips.cl.di.regsample.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.LocaleList;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import java.util.Locale;

public class RegistrationApplication extends Application {


    private static volatile RegistrationApplication mRegistrationHelper = null;

    private AppInfraInterface mAppInfraInterface;

    /**
     * @return instance of this class
     */
    public synchronized static RegistrationApplication getInstance() {
        return mRegistrationHelper;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRegistrationHelper = this;
        mAppInfraInterface = new AppInfra.Builder().build(this);
        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        //if (restoredText != null) {
            String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
         //   if (restoredHSDPText != null && restoredHSDPText.equals(restoredText)) {
                initHSDP(Configuration.DEVELOPMENT);
           // }
            initRegistration(Configuration.DEVELOPMENT);
        //} else {
        //    initRegistration(Configuration.STAGING);
        //}
    }


    public void initRegistration(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        if(mAppInfraInterface == null){
            mAppInfraInterface = new AppInfra.Builder().build(this);
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
     //   localeManager.setInputLocale("zh", "CN");

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(mAppInfraInterface);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

        RLog.enableLogging();


       mAppInfraInterface.getConfigInterface().setPropertyForKey("appidentity.micrositeId",
                "appinfra",
                "77000",
                configError);

    }
    public void initHSDP(Configuration configuration) {
        if(mAppInfraInterface == null){
            mAppInfraInterface = new AppInfra.Builder().build(this);
        }
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        //store hsdp last envoronment
      //  HSDPInfo hsdpInfo;


        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_APPLICATION_NAME,
                URConfigurationConstants.UR,
                "CDP",
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_SECRET,
                URConfigurationConstants.UR,
                "057b97e0-f9b1-11e6-bc64-92361f002671",
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_SHARED, URConfigurationConstants.UR,
                "fe53a854-f9b0-11e6-bc64-92361f002671",
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                URConfigurationConstants.
                        HSDP_CONFIGURATION_BASE_URL,
                URConfigurationConstants.UR,
                "https://user-registration-assembly-hsdpchinadev.cn1.philips-healthsuite.com.cn",
                configError);

/*
        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        switch (configuration) {
            case EVALUATION:
     *//*           mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_APPLICATION_NAME,
                        URConfigurationConstants.UR,
                        "uGrow",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SECRET,
                        URConfigurationConstants.UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SHARED,
                        URConfigurationConstants.UR,
                        "e95f5e71-c3c0-4b52-8b12-ec297d8ae960",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_BASE_URL,
                        URConfigurationConstants.UR,
                        "https://ugrow-ds-staging.eu-west.philips-healthsuite.com",
                        configError);*//*

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case DEVELOPMENT:
          *//*      mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_APPLICATION_NAME,
                        URConfigurationConstants.UR,
                        "uGrow",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SECRET,
                        URConfigurationConstants.UR,
                        "c623685e-f02c-11e5-9ce9-5e5517507c66",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SHARED,
                        URConfigurationConstants.UR,
                        "c62362a0-f02c-11e5-9ce9-5e5517507c66",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_BASE_URL,
                        URConfigurationConstants.UR,
                        "https://ugrow-ds-development.cloud.pcftest.com",
                        configError);*//*

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_APPLICATION_NAME,
                        URConfigurationConstants.UR,
                        "CDP",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SECRET,
                        URConfigurationConstants.UR,
                        "057b97e0-f9b1-11e6-bc64-92361f002671",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SHARED,
                        URConfigurationConstants.UR,
                        "fe53a854-f9b0-11e6-bc64-92361f002671",
                        configError);

                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_BASE_URL,
                        URConfigurationConstants.UR,
                        "https://user-registration-assembly-hsdpchinadev.cn1.philips-healthsuite.com.cn",
                        configError);


                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();

                break;
            case PRODUCTION:
                SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
            case STAGING:
               *//* mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_APPLICATION_NAME,
                        URConfigurationConstants.UR,
                        "uGrow",
                        configError);
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SECRET,
                        URConfigurationConstants.UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_SHARED,
                        URConfigurationConstants.UR,
                        "fe53a854-f9b0-11e6-bc64-92361f002671",
                        configError);
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        URConfigurationConstants.
                                HSDP_CONFIGURATION_BASE_URL,
                        URConfigurationConstants.UR,
                        "https://ugrow-ds-staging.eu-west.philips-healthsuite.com",
                        configError);*//*
                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();

                break;
            case TESTING:
                prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
        }*/



    }

    public static final String SERVICE_DISCOVERY_TAG = "ServiceDiscovery";
    final String AI = "appinfra";
    private void initAppIdentity(Configuration configuration) {
        if(mAppInfraInterface == null){
            mAppInfraInterface = new AppInfra.Builder().build(this);
        }
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = mAppInfraInterface.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = mAppInfraInterface.
                getConfigInterface();

        //Dynamically set the values to appInfar and app state

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
       /* mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.micrositeId",
                AI,
                "77000",
                configError);*/

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);


        switch (configuration) {
            case EVALUATION:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "TEST",
                        configError);
                break;
        }


    /*    if ([currentConfiguration isEqualToString:@"HSDP"]) {
            DIRegistrationEnvironment environment = [self registrationEnvironmentFromString:[RegistrationUtility getAppStateString:[self.urDependencies.appInfra.appIdentity getAppState]]];
            if (environment == kRegistrationEnvironmentEval || environment == kRegistrationEnvironmentStaging) {
                [self setPropertyForKey:HSDPConfiguration_ApplicationName value:@"uGrow"];
                [self setPropertyForKey:HSDPConfiguration_Shared value:@"e95f5e71-c3c0-4b52-8b12-ec297d8ae960"];
                [self setPropertyForKey:HSDPConfiguration_Secret value:@"EB7D2C2358E4772070334CD868AA6A802164875D6BEE858D13226234350B156AC8C4917885B5552106DC7F9583CA52CB662110516F8AB02215D51778DE1EF1F3"];
                [self setPropertyForKey:HSDPConfiguration_BaseURL value:@"https://user-registration-assembly-staging.eu-west.philips-healthsuite.com"];
            }else{
                [self setPropertyForKey:HSDPConfiguration_ApplicationName value:@"CDP"];
                [self setPropertyForKey:HSDPConfiguration_Shared value:@"fe53a854-f9b0-11e6-bc64-92361f002671"];
                [self setPropertyForKey:HSDPConfiguration_Secret value:@"057b97e0-f9b1-11e6-bc64-92361f002671"];
                [self setPropertyForKey:HSDPConfiguration_BaseURL value:@"https://user-registration-assembly-hsdpchinadev.cn1.philips-healthsuite.com.cn"];
            }
        }else
        */

        AppIdentityInfo appIdentityInfo = new AppIdentityInfo();
        appIdentityInfo.setAppLocalizedNAme(mAppIdentityInterface.getLocalizedAppName());
        appIdentityInfo.setSector(mAppIdentityInterface.getSector());
        appIdentityInfo.setMicrositeId(mAppIdentityInterface.getMicrositeId());
        appIdentityInfo.setAppName(mAppIdentityInterface.getAppName());
        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        appIdentityInfo.setAppVersion(mAppIdentityInterface.getAppVersion());
        appIdentityInfo.setServiceDiscoveryEnvironment(mAppIdentityInterface.getServiceDiscoveryEnvironment());

        Log.i(SERVICE_DISCOVERY_TAG, " AppIdentity AppLocalizedNAme : " + appIdentityInfo.getAppLocalizedNAme());
        Log.i(SERVICE_DISCOVERY_TAG, " AppIdentity Sector : " + appIdentityInfo.getSector());
        Log.i(SERVICE_DISCOVERY_TAG, " AppIdentity MicrositeId : " + appIdentityInfo.getMicrositeId());
        Log.i(SERVICE_DISCOVERY_TAG, " AppIdentity AppName : " + appIdentityInfo.getAppName());
        Log.i(SERVICE_DISCOVERY_TAG, " AppIdentity AppState : " + appIdentityInfo.getAppState().toString());
        Log.i(SERVICE_DISCOVERY_TAG, " AppIdentity AppVersion : " + appIdentityInfo.getAppVersion());
        Log.i(SERVICE_DISCOVERY_TAG, " AppIdentity ServiceDiscoveryEnvironment : " + appIdentityInfo.getServiceDiscoveryEnvironment());
    }
}

