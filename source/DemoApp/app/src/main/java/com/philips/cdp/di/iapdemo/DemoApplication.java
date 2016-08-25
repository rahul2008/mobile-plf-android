package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;

import java.util.Locale;

public class DemoApplication extends Application {
    AppInfra appInfra;

    @Override
    public void onCreate() {
        super.onCreate();
        initAppInfra();
        initUserRegistration();

        new IAPDependencies(appInfra);
        new IAPSettings(this);
    }

    public void initAppInfra() {
        appInfra = new AppInfra.Builder().build(this);
    }

    private void initUserRegistration() {
        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());

        RegistrationBaseConfiguration mRegistrationBaseConfiguration =
                new RegistrationBaseConfiguration();
        mRegistrationBaseConfiguration.getPilConfiguration().setMicrositeId("77000");
        mRegistrationBaseConfiguration.getPilConfiguration().setRegistrationEnvironment(Configuration.STAGING);

        URDependancies urDependancies = new URDependancies(appInfra);
        URSettings urSettings = new URSettings(this);

        urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

    public AppInfra getAppInfra() {
        return appInfra;
    }
}
