package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.tagging.Tagging;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeUserRegistration();
        initializeTagging();
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
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        RegistrationHelper.getInstance().initializeUserRegistration(getApplicationContext());
        Tagging.init(getApplicationContext(), "Philips Registartion Sample demo");
    }
}
