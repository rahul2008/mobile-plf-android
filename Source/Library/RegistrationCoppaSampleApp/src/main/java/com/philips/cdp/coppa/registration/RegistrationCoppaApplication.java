
package com.philips.cdp.coppa.registration;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.utils.CoppaDependancies;
import com.philips.cdp.registration.coppa.utils.CoppaInterface;
import com.philips.cdp.registration.coppa.utils.CoppaSettings;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationCoppaApplication extends Application {


    private static volatile RegistrationCoppaApplication mRegistrationHelper = null;



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
        RegistrationHelper.getInstance().setAppInfraInstance(AppInfraSingleton.getInstance());

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




        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.DEVELOPMENT,"8kaxdrpvkwyr7pnp987amu4aqb4wmnte");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.TESTING,"g52bfma28yjbd24hyjcswudwedcmqy7c");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.EVALUATION,"f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.STAGING,"f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.PRODUCTION,"9z23k3q8bhqyfwx78aru6bz8zksga54u");

        System.out.println("Dev : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.DEVELOPMENT));
        System.out.println("Test : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.TESTING));
        System.out.println("Evaluation : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.EVALUATION));
        System.out.println("Staging : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.STAGING));
        System.out.println("prod : "+RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.PRODUCTION));


        RegistrationConfiguration.getInstance().setMicrositeId("77000");
        RegistrationConfiguration.getInstance().setRegistrationEnvironment(configuration);

        System.out.println("Microsite Id : "+RegistrationConfiguration.getInstance().getMicrositeId());
        System.out.println("Environment : "+RegistrationConfiguration.getInstance().getRegistrationEnvironment());


        RegistrationConfiguration.getInstance().setEmailVerificationRequired(true);
        RegistrationConfiguration.getInstance().setTermsAndConditionsAcceptanceRequired(true);
        RegistrationConfiguration.getInstance().setCampainId("CL20150501_PC_TB_COPPA");

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

        //Configure HSDP
        //initHSDP(configuration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        CoppaDependancies urDependancies = new CoppaDependancies(AppInfraSingleton.getInstance());
        CoppaSettings urSettings = new CoppaSettings(this);
        CoppaInterface urInterface = new CoppaInterface();
        urInterface.init(urDependancies,urSettings);

        //RegistrationHelper.getInstance().initializeUserRegistration(this);
        //Tagging.init(this, "Philips Registration");

    }

}

