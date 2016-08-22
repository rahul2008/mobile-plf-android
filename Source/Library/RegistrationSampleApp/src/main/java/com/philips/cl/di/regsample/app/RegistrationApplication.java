
package com.philips.cl.di.regsample.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationApplication extends Application {


    private static volatile RegistrationApplication mRegistrationHelper = null;
    private RegistrationBaseConfiguration mRegistrationBaseConfiguration =
            new RegistrationBaseConfiguration();

    /**
     * @return instance of this class
     */
    public synchronized static RegistrationApplication getInstance() {
        return mRegistrationHelper;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRegistrationHelper = this;

        AppInfraSingleton.setInstance( new AppInfra.Builder().build(this));

       /* RLog.init(this);
        RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
        RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
        RLog.d(RLog.SERVICE_DISCOVERY, "Environment : " + appIdentity.getAppState());*/

       // RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");
     //  RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);


        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {

            String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
            if (restoredHSDPText != null && restoredHSDPText.equals(restoredText)) {
                initHSDP(RegUtility.getConfiguration(restoredHSDPText));
            }
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {
            initRegistration(Configuration.STAGING);
        }

    }


    public void initRegistration(Configuration configuration) {

     /*   AppConfigurationInterface configInterface = RegistrationHelper.getInstance().getAppInfraInstance().getConfigInterface();

        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        try {
            configInterface.setPropertyForKey("JanRainConfiguration.RegistrationClientID.Development","UR","8kaxdrpvkwyr7pnp987amu4aqb4wmnte",configError);
        } catch (AppConfigurationInterface.InvalidArgumentException e) {
            e.printStackTrace();
        }

        //AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        try {
            String  s = (String)configInterface.getPropertyForKey("JanRainConfiguration.RegistrationClientID.Development","UR",configError);

            System.out.println("datafromConfiguartionJson :"+s );

        } catch (AppConfigurationInterface.InvalidArgumentException e) {
            e.printStackTrace();
        }*/

        //Store current environment

        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();

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
        mRegistrationBaseConfiguration.getPilConfiguration().setRegistrationEnvironment(configuration.getValue());
      /*  RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId("77000");
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(configuration.getValue());*/

        //Configure Flow
        mRegistrationBaseConfiguration.getFlow().setEmailVerificationRequired(true);
        mRegistrationBaseConfiguration.getFlow().setTermsAndConditionsAcceptanceRequired(true);
        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("DEFAULT", "16");

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
        providers.put("DEFAULT", values3);

        mRegistrationBaseConfiguration.getSignInProviders().setProviders(providers);
       // RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(providers);
        //Configure HSDP
        //initHSDP(configuration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        URDependancies urDependancies = new URDependancies(AppInfraSingleton.getInstance());
        URSettings urSettings = new URSettings(this);
        urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies,urSettings);
        // RegistrationHelper.getInstance().initializeUserRegistration(this);
      //  Tagging.init(this, "Philips Registration");

    }

    public void initHSDP(Configuration configuration) {
        //store hsdp last envoronment
        HSDPInfo hsdpInfo;
        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        switch (configuration) {
            case EVALUATION:
                hsdpInfo = new HSDPInfo();
                hsdpInfo.setApplicationName("uGrow");
                hsdpInfo.setSharedId("e95f5e71-c3c0-4b52-8b12-ec297d8ae960");
                hsdpInfo.setSecreteId("e33a4d97-6ada-491f-84e4-a2f7006625e2");
                hsdpInfo.setBaseURL("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com");
                mRegistrationBaseConfiguration.getHsdpConfiguration().setHSDPInfo(Configuration.EVALUATION,hsdpInfo);
               // RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.EVALUATION, hsdpInfo);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case DEVELOPMENT:
                hsdpInfo = new HSDPInfo();
                hsdpInfo.setApplicationName("uGrow");
                hsdpInfo.setSharedId("c62362a0-f02c-11e5-9ce9-5e5517507c66");
                hsdpInfo.setSecreteId("c623685e-f02c-11e5-9ce9-5e5517507c66");
                hsdpInfo.setBaseURL("https://user-registration-assembly-testing.us-east.philips-healthsuite.com");
                mRegistrationBaseConfiguration.getHsdpConfiguration().setHSDPInfo(Configuration.DEVELOPMENT,hsdpInfo);

               // RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.DEVELOPMENT, hsdpInfo);
                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case PRODUCTION:
                mRegistrationBaseConfiguration.setHsdpConfiguration(null);
                SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
            case STAGING:
                hsdpInfo = new HSDPInfo();
                hsdpInfo.setApplicationName("uGrow");
                hsdpInfo.setSharedId("e95f5e71-c3c0-4b52-8b12-ec297d8ae960");
                hsdpInfo.setSecreteId("e33a4d97-6ada-491f-84e4-a2f7006625e2");
                hsdpInfo.setBaseURL("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com");
              //  RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.STAGING, hsdpInfo);

                /*hsdpInfo.setApplicationName("uGrow");
                hsdpInfo.setSharedId("41a47ab2-1234-11e5-8994-feff819cdc9f");
                hsdpInfo.setSecreteId("41a47ec2-1234-7890-2314-feff129cdc9f");
                hsdpInfo.setBaseURL("https://ugrow-ds-staging.eu-west.philips-healthsuite.com/");

                RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.STAGING, hsdpInfo);*/

                mRegistrationBaseConfiguration.getHsdpConfiguration().setHSDPInfo(Configuration.STAGING,hsdpInfo);
                // RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.STAGING, hsdpInfo);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case TESTING:
                mRegistrationBaseConfiguration.setHsdpConfiguration(null);
                prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
        }
    }
}

