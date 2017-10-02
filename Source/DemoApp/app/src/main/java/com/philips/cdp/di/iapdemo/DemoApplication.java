package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.squareup.leakcanary.LeakCanary;

public class DemoApplication extends Application implements ActivityLifecycleCallbacks {
    final String UR = "UserRegistration";
    private AppInfra mAppInfra;
    private Activity currentActivity;

    ThemeConfiguration themeConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();

        UIDHelper.injectCalligraphyFonts();
        getTheme().applyStyle(R.style.Theme_DLS_Blue_UltraLight, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
        registerActivityLifecycleCallbacks(this);
        LeakCanary.install(this);
        mAppInfra = new AppInfra.Builder().build(getApplicationContext());
        // HSDPConfiguration();
        initRegistration(Configuration.STAGING);
        RLog.enableLogging();
//        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
//        String restoredText = prefs.getString("reg_environment", null);
//        if (restoredText != null) {
//            String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
//            if (restoredHSDPText != null && restoredHSDPText.equals(restoredText)) {
//                initHSDP(RegUtility.getConfiguration(restoredHSDPText));
//            }
//            initRegistration(RegUtility.getConfiguration(restoredText));
//        } else {

        //}
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

        initAppIdentity(configuration);


//        UappInterface standardRegistrationInterface = new URDemouAppInterface();
//        standardRegistrationInterface.init(new URDemouAppDependencies(mAppInfraInterface), new URDemouAppSettings(this));
        URDependancies urDependancies = new URDependancies(mAppInfra);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

        RLog.enableLogging();

        //  mAppInfraInterface.getServiceDiscovery().setHomeCountry("ES");
    }

//    public void initRegistration(Configuration configuration) {
//        AppConfigurationInterface.AppConfigurationError configError = new
//                AppConfigurationInterface.AppConfigurationError();
//        if (mAppInfra == null) {
//            mAppInfra = new AppInfra.Builder().build(this);
//        }
//        mAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
//                        "RegistrationClientID." + Configuration.DEVELOPMENT
//                , "UserRegistration",
//                "4rdpm7afu7bny6xnacw32etmt7htfraa",
//                configError);
//        mAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
//                        "RegistrationClientID." + Configuration.TESTING
//                , "UserRegistration",
//                "4rdpm7afu7bny6xnacw32etmt7htfraa",
//                configError);
//        mAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
//                        "RegistrationClientID." + Configuration.EVALUATION
//                , "UserRegistration",
//                "4rdpm7afu7bny6xnacw32etmt7htfraa",
//                configError);
//        mAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
//                        "RegistrationClientID." + Configuration.STAGING
//                , "UserRegistration",
//                "4rdpm7afu7bny6xnacw32etmt7htfraa",
//                configError);
//        mAppInfra.getConfigInterface().setPropertyForKey("JanRainConfiguration." +
//                        "RegistrationClientID." + Configuration.PRODUCTION
//                , "UserRegistration",
//                "9z23k3q8bhqyfwx78aru6bz8zksga54u",
//                configError);
//
//      /*  System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.DEVELOPMENT));
//        System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.TESTING));
//        System.out.println("Evaluation : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.EVALUATION));
//        System.out.println("Staging : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.STAGING));
//        System.out.println("prod : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.PRODUCTION));
//
//*/
//        mAppInfra.getConfigInterface().setPropertyForKey("PILConfiguration." +
//                        "MicrositeID",
//                "UserRegistration",
//                "77000",
//                configError);
//        mAppInfra.getConfigInterface().setPropertyForKey("PILConfiguration." +
//                        "RegistrationEnvironment",
//                "UserRegistration",
//                configuration.getValue(),
//                configError);
//       /* System.out.println("Microsite Id : " + RegistrationConfiguration.getInstance().getMicrositeId());
//        System.out.println("Environment : " + RegistrationConfiguration.getInstance().getRegistrationEnvironment());
//*/
//        mAppInfra.
//                getConfigInterface().setPropertyForKey("Flow." +
//                        "EmailVerificationRequired",
//                "UserRegistration",
//                "" + true,
//                configError);
//        mAppInfra.
//                getConfigInterface().setPropertyForKey("Flow." +
//                        "TermsAndConditionsAcceptanceRequired",
//                "UserRegistration",
//                "" + true,
//                configError);
//       /* System.out.println("Email verification : " + RegistrationConfiguration.getInstance().isEmailVerificationRequired());
//        System.out.println("Terms : " + RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired());
//*/
//        String minAge = "{ \"NL\":12 ,\"GB\":0,\"default\": 16}";
//        mAppInfra.
//                getConfigInterface().setPropertyForKey("Flow." +
//                        "MinimumAgeLimit",
//                "UserRegistration",
//                minAge,
//                configError);
//      /*  System.out.println("NL age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("NL"));
//        System.out.println("GB age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("GB"));
//        System.out.println("default age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("default"));
//        System.out.println("unknown age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("unknown"));
//*/
//        ArrayList<String> providers = new ArrayList<String>();
//        providers.add("facebook");
//        providers.add("googleplus");
//        mAppInfra.
//                getConfigInterface().setPropertyForKey("SigninProviders." +
//                        "NL",
//                "UserRegistration",
//                providers,
//                configError);
//
//        mAppInfra.
//                getConfigInterface().setPropertyForKey("SigninProviders." +
//                        "US",
//                "UserRegistration",
//                providers,
//                configError);
//
//        mAppInfra.
//                getConfigInterface().setPropertyForKey("SigninProviders." +
//                        "default",
//                "UserRegistration",
//                providers,
//                configError);
//
//       /* System.out.println("sss NL providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("hh"));
//        System.out.println("GB providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("US"));
//        System.out.println("default providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("NL"));
//        System.out.println("unknown providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("unknown"));
//        System.out.println("unknown providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("default"));
//*/
//
//
//        //HSDP configuration
//        //initHSDP(configuration);
//
//        initAppIdentity(configuration);
//
//
//        URDependancies urDependancies = new URDependancies(mAppInfra);
//        URSettings urSettings = new URSettings(this);
//        URInterface urInterface = new URInterface();
//        urInterface.init(urDependancies, urSettings);
//
//    }

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

    private void initAppIdentity(Configuration configuration) {
        if (mAppInfra == null) {
            mAppInfra = new AppInfra.Builder().build(this);
        }
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = mAppInfra.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = mAppInfra.
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

        mAppInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        mAppInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);


        switch (configuration) {
            case EVALUATION:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                mAppInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "TEST",
                        configError);
                break;
        }

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

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
