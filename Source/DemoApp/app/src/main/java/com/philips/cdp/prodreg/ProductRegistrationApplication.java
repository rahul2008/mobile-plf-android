package com.philips.cdp.prodreg;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.ProdRegDependencies;
import com.philips.cdp.prodreg.launcher.ProdRegSettings;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
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

    private void initProductRegistration() {
        ProdRegDependencies prodRegDependencies = new ProdRegDependencies(ProdRegAppInfraSingleton.getInstance());
        ProdRegSettings prodRegSettings = new ProdRegSettings(getApplicationContext());
        new PRInterface().init(prodRegDependencies, prodRegSettings);
    }

    private void initRegistration() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();
        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationBaseConfiguration mRegistrationBaseConfiguration =
                new RegistrationBaseConfiguration();
        mRegistrationBaseConfiguration.getPilConfiguration().setMicrositeId("77000");
        mRegistrationBaseConfiguration.getPilConfiguration().setRegistrationEnvironment(Configuration.STAGING);
        URDependancies urDependancies = new URDependancies(ProdRegAppInfraSingleton.getInstance());
        URSettings urSettings = new URSettings(this);
        urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

    @SuppressWarnings("deprecation")
    private void initAppInfra() {
        final AppInfra build = new AppInfra.Builder().build(getApplicationContext());
        ProdRegAppInfraSingleton.setInstance(build);
        AppInfraSingleton.setInstance(build);
    }
}
