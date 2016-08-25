package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.Locale;

public class DemoApplication extends Application {
    AppInfra appInfra;

    @Override
    public void onCreate() {
        super.onCreate();
        initAppInfra();
        userRegisterationInit();
        new IAPDependencies(appInfra);
        new IAPSettings(this);
    }


    public void initAppInfra() {
        appInfra = new AppInfra.Builder().build(this);
    }

    private void userRegisterationInit() {
        RegistrationBaseConfiguration mRegistrationBaseConfiguration = new
                RegistrationBaseConfiguration();
        URDependancies urDependancies = new URDependancies(appInfra);
        URSettings urSettings = new URSettings(this);
        urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    public AppInfra getAppInfra() {
        return appInfra;
    }
}
