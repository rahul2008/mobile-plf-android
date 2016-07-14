package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.utils.AppInfraHelper;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.tagging.Tagging;
import java.util.Locale;

public class DemoApplication extends Application {
    //Required in case Production has to be added to dynamic configuration
    // EnvironmentPreferences mAppEnvironmentPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeUserRegistration();
        AppInfraHelper.getInstance().initializeAppInfra(this);
    }

    private void initializeUserRegistration() {
        //Required in case Production has to be added to dynamic configuration
        //mAppEnvironmentPreference = new EnvironmentPreferences(getApplicationContext());
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demoapp:home");

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();
        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        RegistrationHelper.getInstance().initializeUserRegistration(getApplicationContext());
        Tagging.init(getApplicationContext(), "Philips Registartion Sample demo");
        //Required in case Production has to be added to dynamic configuration
        /*if(mAppEnvironmentPreference.getSelectedEnvironmentIndex()== 3) {
            RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.PRODUCTION);
        }
        Log.i("sendhy","Registration Environment :" + RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());*/
    }
}
