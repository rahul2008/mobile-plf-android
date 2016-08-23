
package com.philips.cdp.coppa.registration;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.utils.CoppaDependancies;
import com.philips.cdp.registration.coppa.utils.CoppaInterface;
import com.philips.cdp.registration.coppa.utils.CoppaSettings;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationCoppaApplication extends Application {


    private static volatile RegistrationCoppaApplication mRegistrationHelper = null;
    private RegistrationBaseConfiguration mRegistrationBaseConfiguration =
            new RegistrationBaseConfiguration();


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

        AppInfraSingleton.setInstance( new AppInfra.Builder().build(this));
       // RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");

        RegistrationConfiguration.getInstance().
                setPrioritisedFunction(RegistrationFunction.Registration);


        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {
            Log.i("Last known environment", restoredText);
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {
            initRegistration(Configuration.STAGING);
        }


    }


    public void initRegistration(Configuration configuration) {
        //Store current environment

        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config",
                MODE_PRIVATE).edit();
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

        //Configure PIL
        mRegistrationBaseConfiguration.getPilConfiguration().setMicrositeId("77000");
        mRegistrationBaseConfiguration.getPilConfiguration().setRegistrationEnvironment(configuration.getValue());

        //Configure Flow
        mRegistrationBaseConfiguration.getFlow().setEmailVerificationRequired(true);
        mRegistrationBaseConfiguration.getFlow().setTermsAndConditionsAcceptanceRequired(true);
        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("default", "16");
        mRegistrationBaseConfiguration.getFlow().setMinAgeLimit(ageMap);

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
        mRegistrationBaseConfiguration.getSignInProviders().setProviders(providers);

        //Configure HSDP
        //initHSDP(configuration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        CoppaDependancies urDependancies = new CoppaDependancies(AppInfraSingleton.getInstance());
        CoppaSettings urSettings = new CoppaSettings(this);
        urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
        CoppaInterface urInterface = new CoppaInterface();
        urInterface.init(urDependancies,urSettings);

        //RegistrationHelper.getInstance().initializeUserRegistration(this);
        //Tagging.init(this, "Philips Registration");

    }

}

