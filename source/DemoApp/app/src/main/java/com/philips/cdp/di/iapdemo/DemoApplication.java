package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.Locale;

public class DemoApplication extends Application {
    //Required in case Production has to be added to dynamic configuration
    // EnvironmentPreferences mAppEnvironmentPreference;

    //private RegistrationBaseConfiguration mRegistrationBaseConfiguration = new RegistrationBaseConfiguration();

    @Override
    public void onCreate() {
        super.onCreate();
      //  AppInfra appInfra = new AppInfra.Builder().build(this);
        initAppInfra();
//        initRegistration();
        initializeUserRegistration();
        new IAPDependencies(new AppInfra.Builder().build(this));
        new IAPSettings(this);
    }

    private void initializeUserRegistration() {
        //Required in case Production has to be added to dynamic configuration
        //mAppEnvironmentPreference = new EnvironmentPreferences(getApplicationContext());


        AppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().
                getAppInfraInstance().getTagging();
        aiAppTaggingInterface.createInstanceForComponent("User Registration",
                RegistrationHelper.getRegistrationApiVersion());
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);

        RegistrationConfiguration.getInstance().
                setPrioritisedFunction(RegistrationFunction.Registration);
        RLog.init(this);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(this);
    }

//    public void initRegistration() {
//
//        AppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().
//                getAppInfraInstance().getTagging();
//        aiAppTaggingInterface.createInstanceForComponent("User Registration",
//                RegistrationHelper.getRegistrationApiVersion());
//        aiAppTaggingInterface.setPreviousPage("demoapp:home");
//        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
//
//        RegistrationConfiguration.getInstance().
//                setPrioritisedFunction(RegistrationFunction.Registration);
//        RLog.init(this);
//
//        //Configure JanRain
//        RegistrationClientId registrationClientId = new RegistrationClientId();
//        registrationClientId.setDevelopmentId("8kaxdrpvkwyr7pnp987amu4aqb4wmnte");
//        registrationClientId.setTestingId("g52bfma28yjbd24hyjcswudwedcmqy7c");
//        registrationClientId.setEvaluationId("f2stykcygm7enbwfw2u9fbg6h6syb8yd");
//        registrationClientId.setStagingId("f2stykcygm7enbwfw2u9fbg6h6syb8yd");
//        registrationClientId.setProductionId("9z23k3q8bhqyfwx78aru6bz8zksga54u");
//
//        mRegistrationBaseConfiguration.getJanRainConfiguration().setClientIds(registrationClientId);
//
//        mRegistrationBaseConfiguration.getPilConfiguration().setMicrositeId("77000");
//        mRegistrationBaseConfiguration.getPilConfiguration().setRegistrationEnvironment(Configuration.STAGING);
//
//        //Configure Flow
//        mRegistrationBaseConfiguration.getFlow().setEmailVerificationRequired(true);
//        mRegistrationBaseConfiguration.getFlow().setTermsAndConditionsAcceptanceRequired(true);
//        HashMap<String, String> ageMap = new HashMap<>();
//        ageMap.put("NL", "16");
//        ageMap.put("GB", "16");
//        ageMap.put("DEFAULT", "16");
//
//        mRegistrationBaseConfiguration.getFlow().setMinAgeLimit(ageMap);
//        HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
//        ArrayList<String> values1 = new ArrayList<String>();
//        ArrayList<String> values2 = new ArrayList<String>();
//        ArrayList<String> values3 = new ArrayList<String>();
//        values1.add("facebook");
//        values1.add("googleplus");
//
//        values2.add("facebook");
//        values2.add("googleplus");
//
//        values3.add("facebook");
//        values3.add("googleplus");
//
//        providers.put("NL", values1);
//        providers.put("US", values2);
//        providers.put("DEFAULT", values3);
//
//        mRegistrationBaseConfiguration.getSignInProviders().setProviders(providers);
//        String languageCode = Locale.getDefault().getLanguage();
//        String countryCode = Locale.getDefault().getCountry();
//
//        PILLocaleManager localeManager = new PILLocaleManager(this);
//        localeManager.setInputLocale(languageCode, countryCode);
//
//        URDependancies urDependancies = new URDependancies(AppInfraSingleton.getInstance());
//        URSettings urSettings = new URSettings(this);
//        urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
//        URInterface.getInstancence().init(urDependancies,urSettings);
//    }
//


    private void initAppInfra() {
        AppInfraSingleton.setInstance(new AppInfra.Builder().build(this));
    }

    public AppInfraInterface getAppInfra() {
        return AppInfraSingleton.getInstance();
    }
}
