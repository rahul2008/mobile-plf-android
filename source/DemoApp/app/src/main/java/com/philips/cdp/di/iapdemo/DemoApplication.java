package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.tagging.Tagging;
import com.squareup.leakcanary.LeakCanary;

import java.util.Locale;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeUserRegistration();
        initializeTagging();
        LeakCanary.install(this);
    }

    private void initializeTagging() {
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("IAPDemoAppsID");
        Tagging.setLaunchingPageName("IapDemoApp");
        Tagging.setDebuggable(true);
        Tagging.setComponentVersionKey(IAPAnalyticsConstant.VERSION_KEY);
        Tagging.setComponentVersionVersionValue(BuildConfig.VERSION_NAME);
        Tagging.init(getApplicationContext(), "Philips IAP demo");
    }

    private void initializeUserRegistration() {
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
    }
}
