package com.philips.cdp.di.iapdemo;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.Locale;

public class DemoApplication extends Application {
    public static final String SERVICE_DISCOVERY_TAG = "ServiceDiscovery";
    final String AI = "appinfra";
    final String UR = "UserRegistration";
    private AppInfra mAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        initAppInfra();
        setLocale();
        initRegistration(Configuration.PRODUCTION);
    }

    public void initAppInfra() {
        mAppInfra = new AppInfra.Builder().build(getApplicationContext());
        RegistrationHelper.getInstance().setAppInfraInstance(mAppInfra);
    }

    private void setLocale() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    public AppInfra getAppInfra() {
        return mAppInfra;
    }

    public void initRegistration(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        getAppInfra().getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.DEVELOPMENT
                , UR,
                "8kaxdrpvkwyr7pnp987amu4aqb4wmnte",
                configError);
        getAppInfra().getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.TESTING
                , UR,
                "g52bfma28yjbd24hyjcswudwedcmqy7c",
                configError);
        getAppInfra().getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.EVALUATION
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        getAppInfra().getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.STAGING
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        getAppInfra().getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." + Configuration.PRODUCTION
                , UR,
                "9z23k3q8bhqyfwx78aru6bz8zksga54u",
                configError);

      /*  System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.DEVELOPMENT));
        System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.TESTING));
        System.out.println("Evaluation : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.EVALUATION));
        System.out.println("Staging : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.STAGING));
        System.out.println("prod : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.PRODUCTION));

*/
        getAppInfra().getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "MicrositeID",
                UR,
                "77000",
                configError);
        getAppInfra().getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "RegistrationEnvironment",
                UR,
                configuration.getValue(),
                configError);
       /* System.out.println("Microsite Id : " + RegistrationConfiguration.getInstance().getMicrositeId());
        System.out.println("Environment : " + RegistrationConfiguration.getInstance().getRegistrationEnvironment());
*/
        getAppInfra().
                getConfigInterface().setPropertyForKey("Flow." +
                        "EmailVerificationRequired",
                UR,
                "" + true,
                configError);
        getAppInfra().
                getConfigInterface().setPropertyForKey("Flow." +
                        "TermsAndConditionsAcceptanceRequired",
                UR,
                "" + true,
                configError);
       /* System.out.println("Email verification : " + RegistrationConfiguration.getInstance().isEmailVerificationRequired());
        System.out.println("Terms : " + RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired());
*/
        String minAge = "{ \"NL\":12 ,\"GB\":0,\"default\": 16}";
        getAppInfra().
                getConfigInterface().setPropertyForKey("Flow." +
                        "MinimumAgeLimit",
                UR,
                minAge,
                configError);
        ArrayList<String> providers = new ArrayList<>();
        providers.add("facebook");
        providers.add("googleplus");
        getAppInfra().
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "NL",
                UR,
                providers,
                configError);

        getAppInfra().
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "US",
                UR,
                providers,
                configError);

        getAppInfra().
                getConfigInterface().setPropertyForKey("SigninProviders." +
                        "default",
                UR,
                providers,
                configError);

       /* System.out.println("sss NL providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("hh"));
        System.out.println("GB providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("US"));
        System.out.println("default providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("NL"));
        System.out.println("unknown providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("unknown"));
        System.out.println("unknown providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("default"));
*/

        initHSDP(configuration);


        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(getAppInfra());
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

    }

    public void initHSDP(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        //store hsdp last envoronment
        HSDPInfo hsdpInfo;
        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        switch (configuration) {
            case EVALUATION:
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.ApplicationName",
                        UR,
                        "uGrow",
                        configError);

                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.Secret",
                        UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);

                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.Shared",
                        UR,
                        "e95f5e71-c3c0-4b52-8b12-ec297d8ae960",
                        configError);

                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.BaseURL",
                        UR,
                        "https://user-registration-assembly-staging.eu-west.philips-healthsuite.com",
                        configError);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case DEVELOPMENT:
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.ApplicationName",
                        UR,
                        "uGrow",
                        configError);

                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.Secret",
                        UR,
                        "c623685e-f02c-11e5-9ce9-5e5517507c66",
                        configError);

                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.Shared",
                        UR,
                        "c62362a0-f02c-11e5-9ce9-5e5517507c66",
                        configError);

                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.BaseURL",
                        UR,
                        "https://user-registration-assembly-staging.eu-west.philips-healthsuite.com",
                        configError);
                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();

                break;
            case PRODUCTION:
                SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
            case STAGING:
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.ApplicationName",
                        UR,
                        "uGrow",
                        configError);
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.Secret",
                        UR,
                        "e33a4d97-6ada-491f-84e4-a2f7006625e2",
                        configError);
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.Shared",
                        UR,
                        "e95f5e71-c3c0-4b52-8b12-ec297d8ae960",
                        configError);
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "HSDPConfiguration.BaseURL",
                        UR,
                        "https://user-registration-assembly-staging.eu-west.philips-healthsuite.com",
                        configError);
                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();

                break;
            case TESTING:
                prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
        }

        HSDPInfo hsdpInfo1 = RegistrationConfiguration.getInstance().getHSDPInfo();
        if (hsdpInfo1 != null) {
            System.out.println("HSDP: " + hsdpInfo1.getApplicationName());
            System.out.println("HSDP: " + hsdpInfo1.getSecreteId());
            System.out.println("HSDP: " + hsdpInfo1.getSharedId());
            System.out.println("HSDP: " + hsdpInfo1.getBaseURL());
        }


    }

    private void initAppIdentity(Configuration configuration) {
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = getAppInfra().getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = getAppInfra().
                getConfigInterface();

        //Dynamically set the values to appInfar and app state

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        getAppInfra().
                getConfigInterface().setPropertyForKey(
                "appidentity.micrositeId",
                AI,
                "77000",
                configError);

        getAppInfra().
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        getAppInfra().
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);


        switch (configuration) {
            case EVALUATION:
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                getAppInfra().
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                getAppInfra().
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

}
