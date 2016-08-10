package com.philips.cdp.prodreg;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        initAppInfra();
        initProductRegistration();
        initRegistration();
    }

    @SuppressWarnings("deprecation")
    private void initProductRegistration() {
        ProdRegUiHelper.getInstance().init(getApplicationContext(), (AppInfra) AppInfraSingleton.getInstance());
    }

    private void initRegistration() {
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();
        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
        RegistrationHelper.getInstance().initializeUserRegistration(this);
    }

    @SuppressWarnings("deprecation")
    private void initAppInfra() {
        AppInfraSingleton.setInstance(new AppInfra.Builder().build(getApplicationContext()));
    }
}
