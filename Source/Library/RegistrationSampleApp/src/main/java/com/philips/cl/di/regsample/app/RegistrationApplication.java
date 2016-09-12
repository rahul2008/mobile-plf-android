
package com.philips.cl.di.regsample.app;

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
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationApplication extends Application {

    public static final String SERVICE_DISCOVERY_TAG = "ServiceDiscovery";
    private static volatile RegistrationApplication mRegistrationHelper = null;

    private AppInfraInterface mAppInfraInterface;

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


        mAppInfraInterface =  new AppInfra.Builder().build(this);
        //RegistrationHelper.getInstance().setAppInfraInstance(AppInfraSingleton.getInstance());

       /* SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {

            String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
            if (restoredHSDPText != null && restoredHSDPText.equals(restoredText)) {
                initHSDP(RegUtility.getConfiguration(restoredHSDPText));
            }
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {*/
            initRegistration(Configuration.DEVELOPMENT);
        //}
    }


    public void initRegistration(Configuration configuration) {

        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.DEVELOPMENT,"8kaxdrpvkwyr7pnp987amu4aqb4wmnte");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.TESTING,"g52bfma28yjbd24hyjcswudwedcmqy7c");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.EVALUATION,"f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.STAGING,"f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.PRODUCTION,"9z23k3q8bhqyfwx78aru6bz8zksga54u");//9z23k3q8bhqyfwx78aru6bz8zksga54u

        System.out.println("******************* ENVIR ID : "+RegistrationConfiguration.getInstance().getRegistrationClientId(configuration));
        /*System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.TESTING));
        System.out.println("Evaluation : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.EVALUATION));
        System.out.println("Staging : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.STAGING));
        System.out.println("prod : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.PRODUCTION));
*/

        RegistrationConfiguration.getInstance().setMicrositeId("77000");
        RegistrationConfiguration.getInstance().setRegistrationEnvironment(configuration);

        System.out.println("Microsite Id : "+RegistrationConfiguration.getInstance().getMicrositeId());
        System.out.println("Environment : "+RegistrationConfiguration.getInstance().getRegistrationEnvironment());


        RegistrationConfiguration.getInstance().setEmailVerificationRequired(true);
        RegistrationConfiguration.getInstance().setTermsAndConditionsAcceptanceRequired(true);

        System.out.println("Email verification : "+RegistrationConfiguration.getInstance().isEmailVerificationRequired());
        System.out.println("Terms : "+RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired());


        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("default", "16");
        RegistrationConfiguration.getInstance().setMinAgeLimit(ageMap);

        System.out.println("NL age: "+RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("NL"));
        System.out.println("GB age: "+RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("GB"));
        System.out.println("default age: "+RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("default"));
        System.out.println("unknown age: "+RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("unknown"));



        //Configure Signin Providers
        HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
        ArrayList<String> values1 = new ArrayList<String>();
        ArrayList<String> values2 = new ArrayList<String>();
        ArrayList<String> values3 = new ArrayList<String>();
        values1.add("facebook");
        values1.add("googleplus");
        values1.add("sinaweibo");
        values1.add("qq");

        values2.add("facebook");
        values2.add("googleplus");
        values2.add("sinaweibo");
        values2.add("qq");

        values3.add("facebook");
        values3.add("googleplus");
        values3.add("sinaweibo");
        values3.add("qq");

        providers.put("NL", values1);
        providers.put("US", values2);
        providers.put("default", values3);
        RegistrationConfiguration.getInstance().setProviders(providers);

        System.out.println("sss NL providers: "+RegistrationConfiguration.getInstance().getProvidersForCountry("hh"));
        System.out.println("GB providers: "+RegistrationConfiguration.getInstance().getProvidersForCountry("US"));
        System.out.println("default providers: "+RegistrationConfiguration.getInstance().getProvidersForCountry("NL"));
        System.out.println("unknown providers: "+RegistrationConfiguration.getInstance().getProvidersForCountry("unknown"));
        System.out.println("unknown providers: "+RegistrationConfiguration.getInstance().getProvidersForCountry("default"));


       /* HSDPInfo hsdpInfo = new HSDPInfo();
        hsdpInfo.setApplicationName("uGrow");
        hsdpInfo.setSharedId("e95f5e71-c3c0-4b52-8b12-ec297d8ae960");
        hsdpInfo.setSecreteId("e33a4d97-6ada-491f-84e4-a2f7006625e2");
        hsdpInfo.setBaseURL("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com");

        RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.EVALUATION,hsdpInfo);


        HSDPInfo hsdpInfo1 = RegistrationConfiguration.getInstance().getHSDPInfo(Configuration.EVALUATION);


        System.out.println("HSDP: "+hsdpInfo1.getApplicationName());
        System.out.println("HSDP: "+hsdpInfo1.getSecreteId());
        System.out.println("HSDP: "+hsdpInfo1.getSharedId());
        System.out.println("HSDP: "+hsdpInfo1.getBaseURL());*/




        //Store current environment

        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();


        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        initAppIdentity();
        URDependancies urDependancies = new URDependancies(mAppInfraInterface);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies,urSettings);

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
                RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.EVALUATION,hsdpInfo);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case DEVELOPMENT:
                hsdpInfo = new HSDPInfo();
                hsdpInfo.setApplicationName("uGrow");
                hsdpInfo.setSharedId("c62362a0-f02c-11e5-9ce9-5e5517507c66");
                hsdpInfo.setSecreteId("c623685e-f02c-11e5-9ce9-5e5517507c66");
                hsdpInfo.setBaseURL("https://user-registration-assembly-testing.us-east.philips-healthsuite.com");
                RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.DEVELOPMENT,hsdpInfo);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case PRODUCTION:
                SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
            case STAGING:
                hsdpInfo = new HSDPInfo();
                hsdpInfo.setApplicationName("uGrow");
                hsdpInfo.setSharedId("e95f5e71-c3c0-4b52-8b12-ec297d8ae960");
                hsdpInfo.setSecreteId("e33a4d97-6ada-491f-84e4-a2f7006625e2");
                hsdpInfo.setBaseURL("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com");

                RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.STAGING,hsdpInfo);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
                break;
            case TESTING:
                prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
        }
    }

    private void initAppIdentity() {

        AppIdentityInterface mAppIdentityInterface;
        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
        mAppIdentityInterface = appInfra.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = appInfra.getConfigInterface();

        AppIdentityInfo appIdentityInfo = new AppIdentityInfo();
        appIdentityInfo.setAppLocalizedNAme(mAppIdentityInterface.getLocalizedAppName());
        appIdentityInfo.setSector(mAppIdentityInterface.getSector());
        appIdentityInfo.setMicrositeId(mAppIdentityInterface.getMicrositeId());
        appIdentityInfo.setAppName(mAppIdentityInterface.getAppName());
        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        appIdentityInfo.setAppVersion(mAppIdentityInterface.getAppVersion());
        appIdentityInfo.setServiceDiscoveryEnvironment(mAppIdentityInterface.getServiceDiscoveryEnvironment());


        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppLocalizedNAme : "+appIdentityInfo.getAppLocalizedNAme());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity Sector : "+ appIdentityInfo.getSector());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity MicrositeId : "+appIdentityInfo.getMicrositeId());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppName : "+ appIdentityInfo.getAppName());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppState B4: "+appIdentityInfo.getAppState().toString());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppVersion : "+ appIdentityInfo.getAppVersion());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity ServiceDiscoveryEnvironment : "+ appIdentityInfo.getServiceDiscoveryEnvironment());


        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        appConfigurationInterface.setPropertyForKey("appidentity.appState","appinfra","ACCEPTANCE",configError);

        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppState set to ACCEPTANCE : "+appIdentityInfo.getAppState().toString());

        appConfigurationInterface.setPropertyForKey("appidentity.appState","appinfra","DEVELOPMENT",configError);
        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppState set to STAGING : "+appIdentityInfo.getAppState().toString());


        /*appConfigurationInterface.setPropertyForKey("appidentity.appState","appinfra","PRODUCTION",configError);
        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppState set to PRODUCTION : "+appIdentityInfo.getAppState().toString());


        appConfigurationInterface.setPropertyForKey("appidentity.appState","appinfra","STAGING",configError);
        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppState set to  STAGING: "+appIdentityInfo.getAppState().toString());*/

        Log.i(SERVICE_DISCOVERY_TAG," AppIdentity AppState after : "+appIdentityInfo.getAppState().toString());

    }
}

