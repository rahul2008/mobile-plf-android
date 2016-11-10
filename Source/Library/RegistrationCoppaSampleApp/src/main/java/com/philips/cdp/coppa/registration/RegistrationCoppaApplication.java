
package com.philips.cdp.coppa.registration;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import java.util.ArrayList;
import java.util.Locale;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

public class RegistrationCoppaApplication extends Application {



    private static volatile RegistrationCoppaApplication mRegistrationHelper = null;
    public static final String SERVICE_DISCOVERY_TAG = "ServiceDiscovery";
    private AppInfraInterface mAppInfraInterface;



    /**
     * @return instance of this class
     */
    public synchronized static RegistrationCoppaApplication getInstance() {
        return mRegistrationHelper;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRegistrationHelper = this;
        mAppInfraInterface = new AppInfra.Builder().build(this);
        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {
            initRegistration(Configuration.STAGING);
        }
    }

    public void initRegistration(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        mAppInfraInterface.getConfigInterface().setPropertyForKey(URConfigurationConstants.
                JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_DEVELOPMENT
                , UR,
                "8kaxdrpvkwyr7pnp987amu4aqb4wmnte",
                configError);
        mAppInfraInterface.getConfigInterface().setPropertyForKey(URConfigurationConstants.
                JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_TESTING
                , UR,
                "g52bfma28yjbd24hyjcswudwedcmqy7c",
                configError);
        mAppInfraInterface.getConfigInterface().setPropertyForKey(URConfigurationConstants.
                JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_EVALUATION
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        mAppInfraInterface.getConfigInterface().setPropertyForKey(URConfigurationConstants.
                JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_STAGING
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        mAppInfraInterface.getConfigInterface().setPropertyForKey(URConfigurationConstants.
                JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_PRODUCTION
                , UR,
                "9z23k3q8bhqyfwx78aru6bz8zksga54u",
                configError);

      /*  System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.DEVELOPMENT));
        System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.TESTING));
        System.out.println("Evaluation : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.EVALUATION));
        System.out.println("Staging : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.STAGING));
        System.out.println("prod : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.PRODUCTION));

*/
        mAppInfraInterface.getConfigInterface().setPropertyForKey(URConfigurationConstants.
                PILCONFIGURATION_MICROSITE_ID,
                UR,
                "77000",
                configError);
        mAppInfraInterface.getConfigInterface().setPropertyForKey(URConfigurationConstants.
                PILCONFIGURATION_REGISTRATION_ENVIRONMENT,
                UR,
                configuration.getValue(),
                configError);
       /* System.out.println("Microsite Id : " + RegistrationConfiguration.getInstance().getMicrositeId());
        System.out.println("Environment : " + RegistrationConfiguration.getInstance().getRegistrationEnvironment());
*/
        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.
                FLOW_EMAIL_VERIFICATION_REQUIRED,
                UR,
                "" + true,
                configError);
        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.
                FLOW_TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED,
                UR,
                "" + true,
                configError);


        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.
                PIL_CONFIGURATION_CAMPAIGN_ID,
                UR,
                "CL20150501_PC_TB_COPPA",
                configError);



       /*
         System.out.println("Terms : " + RegistrationConfiguration.getInstance().getCampaignId());
       System.out.println("Email verification : " + RegistrationConfiguration.getInstance().isEmailVerificationRequired());
        System.out.println("Terms : " + RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired());
*/
        String minAge = "{ \"NL\":12 ,\"GB\":13,\"default\": 16}";
        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.
                FLOW_MINIMUM_AGE_LIMIT,
                UR,
                minAge,
                configError);
      /*  System.out.println("NL age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("NL"));
        System.out.println("GB age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("GB"));
        System.out.println("default age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("default"));
        System.out.println("unknown age: " + RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("unknown"));
*/
        ArrayList<String> providers = new ArrayList<String>();
        providers.add("facebook");
        providers.add("googleplus");
        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        "NL",
                UR,
                providers,
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        "US",
                UR,
                providers,
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        URConfigurationConstants.DEFAULT,
                UR,
                providers,
                configError);

       /* System.out.println("sss NL providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("hh"));
        System.out.println("GB providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("US"));
        System.out.println("default providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("NL"));
        System.out.println("unknown providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("unknown"));
        System.out.println("unknown providers: " + RegistrationConfiguration.getInstance().getProvidersForCountry("default"));
*/

        //Store current environment

        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();


        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(mAppInfraInterface);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

    }

    final String AI = "appinfra";
    private void initAppIdentity(Configuration configuration) {
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = mAppInfraInterface.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = mAppInfraInterface.
                getConfigInterface();

        //Dynamically set the values to appInfar and app state

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.micrositeId",
                AI,
                "77000",
                configError);

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

