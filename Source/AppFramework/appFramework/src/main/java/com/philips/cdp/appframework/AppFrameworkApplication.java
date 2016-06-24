package com.philips.cdp.appframework;
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.appframework.modularui.UIFlowManager;
import com.philips.cdp.appframework.utility.SharedPreferenceUtility;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;

import java.util.Locale;



public class AppFrameworkApplication extends Application {
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        SharedPreferenceUtility.getInstance().Initialize(getApplicationContext());
        initializeUserRegistrationLibrary();
        UIFlowManager.populateStateBaseList();
        UIFlowManager.populateActivityMap();
        UIFlowManager.checkUserSignInAndDonePressed(getApplicationContext());
    }

    private void initializeUserRegistrationLibrary() {
        RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
        RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demoapp:home");
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(this);
        Tagging.init(this, "Philips Registration");
    }
}
