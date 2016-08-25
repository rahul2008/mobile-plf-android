/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.ProdRegDependencies;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AppFrameworkApplication extends Application {
    public UIFlowManager flowManager;
    private static Context mContext;
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface loggingInterface;
    public RegistrationBaseConfiguration mRegistrationBaseConfiguration =
            new RegistrationBaseConfiguration();
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        mContext = getApplicationContext();
        flowManager = new UIFlowManager();
        AppInfraSingleton.setInstance(gAppInfra = new AppInfra.Builder().build(getApplicationContext()));
        gAppInfra = AppInfraSingleton.getInstance();
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
        setLocale();

         initializeUserRegistrationLibrary(Configuration.EVALUATION);
        initializeProductRegistrationLibrary();
    }

    private void setLocale() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    @SuppressWarnings("deprecation")
    private void initializeProductRegistrationLibrary() {
        ProdRegDependencies prodRegDependencies = new ProdRegDependencies(AppInfraSingleton.getInstance());

        //TO-DO - have to take PRSettings on next release
        UappSettings uappSettings = new UappSettings(getApplicationContext());
        new PRInterface().init(prodRegDependencies, uappSettings);
    }

    public UIFlowManager getFlowManager() {
        return flowManager;
    }

    public static Context getContext() {
        return mContext;
    }

    /*private void initializeUserRegistrationLibrary() {
        AppTaggingInterface aiAppTaggingInterface = gAppInfra.getTagging();
        aiAppTaggingInterface.createInstanceForComponent("User Registration",
                RegistrationHelper.getRegistrationApiVersion());
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);

        *//*RegistrationConfiguration.getInstance().
                setPrioritisedFunction(RegistrationFunction.Registration);*//*
      //RLog.init(this);
        RegistrationHelper.getInstance().initializeUserRegistration(this);
    }*/
    public void initializeUserRegistrationLibrary(Configuration configuration) {



        //Store current environment


        //Configure JanRain
        RegistrationClientId registrationClientId = new RegistrationClientId();
        registrationClientId.setDevelopmentId("8kaxdrpvkwyr7pnp987amu4aqb4wmnte");
        registrationClientId.setTestingId("g52bfma28yjbd24hyjcswudwedcmqy7c");
        registrationClientId.setEvaluationId("f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        registrationClientId.setStagingId("f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        registrationClientId.setProductionId("9z23k3q8bhqyfwx78aru6bz8zksga54u");

        mRegistrationBaseConfiguration.getJanRainConfiguration().setClientIds(registrationClientId);

        // RegistrationDynamicConfiguration.getInstance().getJanRainConfiguration().setClientIds(registrationClientId);

        //Configure PIL

        mRegistrationBaseConfiguration.getPilConfiguration().setMicrositeId("77000");
        mRegistrationBaseConfiguration.getPilConfiguration().setRegistrationEnvironment(configuration);
      /*  RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId("77000");
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(configuration.getValue());*/

        //Configure Flow
        mRegistrationBaseConfiguration.getFlow().setEmailVerificationRequired(true);
        mRegistrationBaseConfiguration.getFlow().setTermsAndConditionsAcceptanceRequired(true);
        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("default", "16");

        mRegistrationBaseConfiguration.getFlow().setMinAgeLimit(ageMap);

        // RegistrationDynamicConfiguration.getInstance().getFlow().setMinAgeLimit(ageMap);

        //Configure Signin Providers
        HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
        ArrayList<String> values1 = new ArrayList<String>();
        ArrayList<String> values2 = new ArrayList<String>();
        ArrayList<String> values3 = new ArrayList<String>();
        values1.add("facebook");
        values1.add("googleplus");
        /*values1.add("sinaweibo");
        values1.add("qq");*/

        values2.add("facebook");
        values2.add("googleplus");
        /*values2.add("sinaweibo");
        values2.add("qq");*/

        values3.add("facebook");
        values3.add("googleplus");
        /*values3.add("sinaweibo");
        values3.add("qq");*/

        providers.put("NL", values1);
        providers.put("US", values2);
        providers.put("default", values3);

        mRegistrationBaseConfiguration.getSignInProviders().setProviders(providers);
        // RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(providers);
        //Configure HSDP
        //initHSDP(configuration);

       /* String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);*/

        URDependancies urDependancies = new URDependancies(gAppInfra);
        URSettings urSettings = new URSettings(this);
        urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies,urSettings);
        // RegistrationHelper.getInstance().initializeUserRegistration(this);
        //  Tagging.init(this, "Philips Registration");

    }

}
