/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

//import com.philips.cdp.tagging.Tagging;


public class AppFrameworkApplication extends Application {
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface loggingInterface;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        AppInfraSingleton.setInstance( new AppInfra.Builder().build(getApplicationContext()));
        gAppInfra = AppInfraSingleton.getInstance();
        loggingInterface = gAppInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);

        initializeUserRegistrationLibrary();
    }

    private void initializeUserRegistrationLibrary() {
//        AppInfraSingleton.setInstance( new AppInfra.Builder().build(this));
//        AppInfraInterface mAppInfraInterface = AppInfraSingleton.getInstance();
        AppTaggingInterface aiAppTaggingInterface = gAppInfra.getTagging();
        aiAppTaggingInterface.createInstanceForComponent("User Registration",
                RegistrationHelper.getRegistrationApiVersion());
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);

        RegistrationConfiguration.getInstance().
                setPrioritisedFunction(RegistrationFunction.Registration);
        RLog.init(this);

        //Configure JanRain
        RegistrationClientId registrationClientId = new RegistrationClientId();
        registrationClientId.setDevelopmentId("ad7nn99y2mv5berw5jxewzagazafbyhu");
        registrationClientId.setTestingId("xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7");
        registrationClientId.setEvaluationId("4r36zdbeycca933nufcknn2hnpsz6gxu");
        registrationClientId.setStagingId("f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        registrationClientId.setProductionId("mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3");
        RegistrationDynamicConfiguration.getInstance().getJanRainConfiguration().setClientIds(registrationClientId);

        //Configure PIL
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId("77000");
        HSDPInfo hsdpInfo = new HSDPInfo();
        hsdpInfo.setApplicationName("Datacore");
        hsdpInfo.setSharedId("ba404af2-ee41-4e7c-9157-fd20663f2a6c");
        hsdpInfo.setSecreteId("ad3d0618-be4d-4958-adc9-f6bcd01fde16");
        hsdpInfo.setBaseURL("https://referenceplatform-ds-platforminfradev.cloud.pcftest.com/");
        RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.DEVELOPMENT, hsdpInfo);
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.DEVELOPMENT);


        //Configure Flow
//        RegistrationDynamicConfiguration.getInstance().getFlow().setEmailVerificationRequired(true);
//        RegistrationDynamicConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(true);
//        HashMap<String, String> ageMap = new HashMap<>();
//        ageMap.put("NL", "16");
//        ageMap.put("GB", "16");
//        ageMap.put("DEFAULT", "16");
//        RegistrationDynamicConfiguration.getInstance().getFlow().setMinAgeLimit(ageMap);

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
        providers.put("DEFAULT", values3);
        RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(providers);

        //Configure HSDP
        //initHSDP(configuration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(this);

    }
}
