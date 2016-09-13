package com.philips.cdp.prodreg;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.prodreg.launcher.PRDependencies;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.PRSettings;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    private static AppInfraInterface mAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        initAppInfra();
        setLocale();
        initProductRegistration();
        initializeUserRegistrationLibrary(Configuration.PRODUCTION);
    }

    private void initProductRegistration() {
        PRDependencies PRDependencies = new PRDependencies(mAppInfra);
        PRSettings PRSettings = new PRSettings(getApplicationContext());
        new PRInterface().init(PRDependencies, PRSettings);
    }

    /**
     * For doing dynamic initialisation Of User registration
     *
     * @param configuration The environment ype as required by UR
     */
    public void initializeUserRegistrationLibrary(Configuration configuration) {

        RegistrationHelper.getInstance().setAppInfraInstance(mAppInfra);
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.DEVELOPMENT, "8kaxdrpvkwyr7pnp987amu4aqb4wmnte");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.TESTING, "g52bfma28yjbd24hyjcswudwedcmqy7c");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.EVALUATION, "f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.STAGING, "f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.PRODUCTION, "9z23k3q8bhqyfwx78aru6bz8zksga54u");


        RegistrationConfiguration.getInstance().setMicrositeId("77000");
        RegistrationConfiguration.getInstance().setRegistrationEnvironment(configuration);


        RegistrationConfiguration.getInstance().setEmailVerificationRequired(true);
        RegistrationConfiguration.getInstance().setTermsAndConditionsAcceptanceRequired(true);

        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("default", "16");
        RegistrationConfiguration.getInstance().setMinAgeLimit(ageMap);


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
        URDependancies urDependancies = new URDependancies(mAppInfra);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

    private void setLocale() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    @SuppressWarnings("deprecation")
    private void initAppInfra() {
        mAppInfra = new AppInfra.Builder().build(getApplicationContext());
        AppInfraSingleton.setInstance(mAppInfra);
        RegistrationHelper.getInstance().setAppInfraInstance(mAppInfra);
    }
}
