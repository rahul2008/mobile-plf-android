package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DemoApplication extends Application {
    private AppInfra appInfra;

    @Override
    public void onCreate() {
        super.onCreate();
        initAppInfra();

        new IAPDependencies(appInfra);
        new IAPSettings(this);
        AppInfraSingleton.setInstance(getAppInfra());
        RegistrationHelper.getInstance().setAppInfraInstance(getAppInfra());
        initUserRegistration(Configuration.PRODUCTION);
    }

    public void initUserRegistration(Configuration configuration) {

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

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        URDependancies urDependancies = new URDependancies(getAppInfra());
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

    public void initAppInfra() {
        appInfra = new AppInfra.Builder().build(this);
    }

    public AppInfra getAppInfra() {
        return appInfra;
    }
}
