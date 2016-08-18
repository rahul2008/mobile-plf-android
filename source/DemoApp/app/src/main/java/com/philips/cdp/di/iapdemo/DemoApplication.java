package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.Locale;

public class DemoApplication extends Application {
    //Required in case Production has to be added to dynamic configuration
    // EnvironmentPreferences mAppEnvironmentPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        initAppInfra();
        initializeUserRegistration();
        new IAPDependencies(AppInfraSingleton.getInstance());

    }

    private void initializeUserRegistration() {
        //Required in case Production has to be added to dynamic configuration
        //mAppEnvironmentPreference = new EnvironmentPreferences(getApplicationContext());


        AppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().
                getAppInfraInstance().getTagging();
        aiAppTaggingInterface.createInstanceForComponent("User Registration",
                RegistrationHelper.getRegistrationApiVersion());
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);

        RegistrationConfiguration.getInstance().
                setPrioritisedFunction(RegistrationFunction.Registration);
        RLog.init(this);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(this);
    }

    private void initAppInfra() {
        AppInfraSingleton.setInstance(new AppInfra.Builder().build(this));
    }

   public AppInfraInterface getAppInfra() {
        return AppInfraSingleton.getInstance();
    }
}
