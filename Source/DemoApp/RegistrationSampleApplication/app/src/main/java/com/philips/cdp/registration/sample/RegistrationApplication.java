
package com.philips.cdp.registration.sample;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AppInfraSingleton.setInstance( new AppInfra.Builder().build(this));
        RegistrationHelper.getInstance().setAppInfraInstance(AppInfraSingleton.getInstance());

        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);

        initRegistration(Configuration.STAGING);

    }


    public void initRegistration(Configuration configuration) {

        //All the client ids
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.DEVELOPMENT,"8kaxdrpvkwyr7pnp987amu4aqb4wmnte");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.TESTING,"g52bfma28yjbd24hyjcswudwedcmqy7c");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.EVALUATION,"f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.STAGING,"f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.PRODUCTION,"9z23k3q8bhqyfwx78aru6bz8zksga54u");

        //set app Microsite id
        RegistrationConfiguration.getInstance().setMicrositeId("77000");
        //set environment
        RegistrationConfiguration.getInstance().setRegistrationEnvironment(configuration);


        //Email verification required true or false
        RegistrationConfiguration.getInstance().setEmailVerificationRequired(true);
        //Set terms and conditions required true or false if set true then min age has to be set
        RegistrationConfiguration.getInstance().setTermsAndConditionsAcceptanceRequired(true);


        //Setting the min age as per country code country code should be capital and default age has to be provided
        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("default", "16");
        RegistrationConfiguration.getInstance().setMinAgeLimit(ageMap);




        //Configure Signin Providers
        HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
        ArrayList<String> values1 = new ArrayList<String>();
        ArrayList<String> values2 = new ArrayList<String>();
        ArrayList<String> values3 = new ArrayList<String>();
        values1.add("facebook");
        values1.add("googleplus");

        values2.add("facebook");
        values2.add("googleplus");

        values3.add("facebook");
        values3.add("googleplus");

        providers.put("NL", values1);
        providers.put("US", values2);
        providers.put("default", values3);
        RegistrationConfiguration.getInstance().setProviders(providers);


        //If HSDP required
        //initHSDP(configuration);


        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        URDependancies urDependancies = new URDependancies(AppInfraSingleton.getInstance());
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies,urSettings);




    }

    public void initHSDP(Configuration configuration) {
        //store hsdp last envoronment
        HSDPInfo hsdpInfo;
        switch (configuration) {
            case EVALUATION:
                hsdpInfo = new HSDPInfo();
                hsdpInfo.setApplicationName("**");
                hsdpInfo.setSharedId("**");
                hsdpInfo.setSecreteId("**");
                hsdpInfo.setBaseURL("**");
                RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.EVALUATION,hsdpInfo);

                break;
            case DEVELOPMENT:
                hsdpInfo = new HSDPInfo();
                hsdpInfo.setApplicationName("uGrow");
                hsdpInfo.setSharedId("c62362a0-f02c-11e5-9ce9-5e5517507c66");
                hsdpInfo.setSecreteId("c623685e-f02c-11e5-9ce9-5e5517507c66");
                hsdpInfo.setBaseURL("https://user-registration-assembly-testing.us-east.philips-healthsuite.com");
                RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.DEVELOPMENT,hsdpInfo);

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

                break;
            case TESTING:
                break;
        }
    }
}
