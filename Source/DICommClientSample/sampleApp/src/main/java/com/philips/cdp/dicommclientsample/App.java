/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample;

import android.app.Application;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.Locale;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CppController cppController = CppController.getInstance();
        if (cppController == null) {
            cppController = CppController.createSharedInstance(this, new SampleKpsConfigurationInfo());
        }

        cppController.setDefaultDcsState();

        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(this, new SampleApplianceFactory(), null, cppController);
        }

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);

        RegistrationHelper.getInstance().initializeUserRegistration(this);

        RegistrationHelper.getInstance().initializeUserRegistration(this);
    }
}
